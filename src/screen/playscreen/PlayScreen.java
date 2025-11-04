package screen.playscreen;

import audio.MusicType;
import audio.SoundType;
import config.LevelConfig;
import exception.ExceptionHandler;
import exception.ResourceLoadException;
import object.GameContext;
import object.UI.Background;
import object.UI.GameButton;
import object.UI.Text.GameText;
import object.brick.BrickManager;
import object.movable.powerup.PowerUpManager;
import screen.Screen;
import screen.ScreenType;
import utils.JsonLoaderUtils;
import utils.TextUtils;

import java.awt.*;

public abstract class PlayScreen implements Screen {

    protected final GameContext gameContext;
    protected final BrickManager brickManager;
    protected final PowerUpManager powerUpManager;
    protected final ScreenType levelId;

    protected GameText scoreText;
    protected GameText numScoreText;
    protected GameButton pauseButton;
    protected GameText numTimeText;
    protected Background background;

    protected String levelInitPath;
    protected String levelSavePath;

    protected long startTime;
    protected long pauseStartTime;
    protected long pauseTime;
    protected long endTime;
    protected long totalTimePlayed;

    protected boolean isPaused;
    protected boolean exited;

    protected boolean isGameOver;
    protected boolean isGameWin;

    protected abstract void handlePauseGame();
    protected abstract boolean handleSavedProgress();
    protected abstract void handleScore();
    protected abstract void handleGameEnd();

    public PlayScreen(Screen screen, ScreenType screenType) {

        PlayScreen playScreen = (PlayScreen) screen;

        gameContext = GameContext.getInstance();
        brickManager = BrickManager.getInstance();
        powerUpManager = PowerUpManager.getInstance();
        levelId = screenType;
        levelInitPath = playScreen.levelInitPath;
        levelSavePath = playScreen.levelSavePath;

        startTime = System.currentTimeMillis();

        boolean canLoadProgress = handleSavedProgress();

        startTime = System.currentTimeMillis();

        if(exited || canLoadProgress) {
            return;
        }

        init(screen);

        scoreText = new GameText(playScreen.scoreText);
        numScoreText = new GameText(playScreen.numScoreText);
        pauseButton = new GameButton(playScreen.pauseButton);
        numTimeText = new GameText(playScreen.numTimeText);
        background = new Background(playScreen.background);

        initObjects(levelInitPath);

    }

    @Override
    public void init(Screen screen) {

        if (!(screen instanceof PlayScreen playScreen)) {
            return;
        }

        GameText baseScoreText = playScreen.scoreText;
        GameText baseNumScoreText = playScreen.numScoreText;
        GameButton basePauseButton = playScreen.pauseButton;
        GameText baseNumTimeText = playScreen.numTimeText;

        baseScoreText.updateSizeFromFontData();
        baseScoreText.alignBottomLeft();

        baseNumScoreText.updateSizeFromFontData();
        baseNumScoreText.alignRightOf(baseScoreText);

        basePauseButton.applyRelativeSize();
        basePauseButton.centerHorizontally();
        basePauseButton.alignBottom();

        baseNumTimeText.updateSizeFromFontData();
        baseNumTimeText.alignRightOf(basePauseButton);
        baseNumTimeText.alignBottom();
        baseNumTimeText.translateX(spacingX);
    }

    private void initObjects(String levelPath) {

        LevelConfig levelConfig = JsonLoaderUtils.loadFromJson(levelPath, LevelConfig.class);

        if (levelConfig == null) {
            ExceptionHandler.handle(new ResourceLoadException(levelPath, null));
        }

        assert levelConfig != null;
        gameContext.loadFromJson(levelConfig);
        brickManager.loadFromJson(levelConfig);
        powerUpManager.loadFromJson(levelConfig);

        numScoreText.setContent(String.valueOf(0));
    }

    @Override
    public void update() {

        if (isPaused) {
            powerUpManager.resumeTimers();
            long currentTime = System.currentTimeMillis();
            pauseTime += currentTime - pauseStartTime;
            isPaused = false;
        }

        long currentTimePlayed = getCurrentTimePlayed();
        numTimeText.setContent(TextUtils.convertMillisToTimeUnit(currentTimePlayed));

        if (mouseManager.isLeftClicked()) {
            soundManager.play(SoundType.CLICK_BUTTON);
            if (pauseButton.isClicked(mouseManager)) {
                powerUpManager.pauseTimers();
                isPaused = true;
                endTime = System.currentTimeMillis();
                pauseStartTime = System.currentTimeMillis();
                handlePauseGame();
                return;
            }
        }

        gameContext.updateContext();
        brickManager.updateBricks();
        powerUpManager.updateFallingPowerUps();

        if (gameContext.getBall().isLost()) {
            gameContext.getLifeCounter().updateLives(false);
            powerUpManager.revertAllPowerUps();
            gameContext.resetObjectsBound();
        }

        handleScore();
        handleGameEnd();
    }

    @Override
    public void render(Graphics2D graphics2D) {

        background.render(graphics2D);

        brickManager.renderBricks(graphics2D);
        gameContext.renderContext(graphics2D);

        powerUpManager.renderPowerUps(graphics2D);

        brickManager.renderBricks(graphics2D);

        scoreText.render(graphics2D);
        numScoreText.render(graphics2D);
        pauseButton.render(graphics2D);
        numTimeText.render(graphics2D);
    }

    @Override
    public void onEnter() {

        if (gameContext.isGameOver() || brickManager.isCleared()) {
            return;
        }

        if (isPaused) {
            soundManager.resumeMusic(MusicType.PLAY_THEME);
        } else {
            soundManager.playMusic(MusicType.PLAY_THEME, true);
        }
    }

    @Override
    public void onExit() {

        if (exited || isGameOver || isGameWin) {
            soundManager.stopMusic(MusicType.PLAY_THEME);
        } else if (isPaused) {
            soundManager.pauseMusic(MusicType.PLAY_THEME);
        }
    }

    public ScreenType getLevelId() {
        return levelId;
    }

    public long getPauseTime() {
        return pauseTime;
    }

    public long getTotalTimePlayedBeforeExit() {
        totalTimePlayed += endTime - startTime - pauseTime;
        return totalTimePlayed;
    }

    private long getCurrentTimePlayed() {
        return totalTimePlayed + System.currentTimeMillis() - startTime - pauseTime;
    }

    public boolean isExited() {
        return exited;
    }

    public void setExited(boolean exited) {
        this.exited = exited;
    }

    public String getLevelSavePath() {
        return levelSavePath;
    }

    public void setGameOver(boolean gameOver) {
        this.isGameOver = gameOver;
    }

}
