package screen;

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
import utils.JsonLoaderUtils;

import java.awt.*;

public class PlayScreen implements Screen {

    private final GameContext gameContext;
    private final BrickManager brickManager;
    private final PowerUpManager powerUpManager;
    private final ScreenType levelId;

    private final GameText scoreText;
    private final GameText numScoreText;
    private final GameButton pauseButton;
    private final Background background;
    private final String levelInitPath;
    private final String levelSavePath;

    private long startTime;
    private long pauseStartTime;
    private long pauseTime;
    private long endTime;
    private boolean isPaused;
    private boolean exited;

    public PlayScreen(Screen screen, ScreenType screenType) {

        gameContext = GameContext.getInstance();
        brickManager = BrickManager.getInstance();
        powerUpManager = PowerUpManager.getInstance();
        this.levelId = screenType;

        init(screen);

        PlayScreen playScreen = (PlayScreen) screen;

        scoreText = new GameText(playScreen.scoreText);
        numScoreText = new GameText(playScreen.numScoreText);
        pauseButton = new GameButton(playScreen.pauseButton);
        background = new Background(playScreen.background);

        levelInitPath = playScreen.levelInitPath;
        levelSavePath = playScreen.levelSavePath;

        initObjects(levelInitPath);

        startTime = System.currentTimeMillis();

    }

    @Override
    public void init(Screen screen) {

        if (!(screen instanceof PlayScreen playScreen)) {
            return;
        }

        GameText baseScoreText = playScreen.scoreText;
        GameText baseNumScoreText = playScreen.numScoreText;
        GameButton basePauseButton = playScreen.pauseButton;

        baseScoreText.updateSizeFromFontData();
        baseScoreText.alignBottomLeft();

        baseNumScoreText.updateSizeFromFontData();
        baseNumScoreText.alignRightOf(baseScoreText);

        basePauseButton.applyRelativeSize();
        basePauseButton.centerHorizontally();
        basePauseButton.alignBottom();

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

        boolean isGameOver = gameContext.isGameOver();
        boolean isGameWin = brickManager.isCleared();

        if (isGameOver || isGameWin) {
            endTime = System.currentTimeMillis();
            powerUpManager.revertAllPowerUps();
            if (isGameOver) {
                screenManager.push(ScreenType.GAME_OVER);
            } else {
                screenManager.push(ScreenType.GAME_WIN);
            }
            return;
        }

        if (mouseManager.isLeftClicked()) {
            soundManager.play(SoundType.CLICK_BUTTON);
            if (pauseButton.isClicked(mouseManager)) {
                screenManager.push(ScreenType.PAUSE);
                powerUpManager.pauseTimers();
                isPaused = true;
                pauseStartTime = System.currentTimeMillis();
                return;
            }
        }

        gameContext.updateContext();
        brickManager.updateBricks();

        if (brickManager.isIncremented()) {
            numScoreText.setContent(String.valueOf(brickManager.getDestroyedBricksCount()));
        }

        powerUpManager.updateFallingPowerUps();

        if (gameContext.getBall().isLost()) {
            gameContext.getLifeCounter().updateLives(false);
            powerUpManager.revertAllPowerUps();
            gameContext.resetObjectsBound();
        }

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
    }

    @Override
    public void onEnter() {
        if (gameContext.isGameOver() || brickManager.isCleared() || isPaused) {
            return;
        }
        soundManager.playMusic(MusicType.PLAY_THEME, true);
    }

    @Override
    public void onExit() {
        if (gameContext.isGameOver() || brickManager.isCleared() || exited) {
            soundManager.stopMusic(MusicType.PLAY_THEME);
        } else {
            soundManager.pauseMusic(MusicType.PLAY_THEME);
        }
    }

    public ScreenType getLevelId() {
        return levelId;
    }

    public long getPauseTime() {
        return pauseTime;
    }

    public long getTotalTimePlayed() {
        return endTime - startTime - pauseTime;
    }

    public void setExited(boolean exited) {
        this.exited = exited;
    }

    public String getLevelSavePath() {
        return levelSavePath;
    }

    public void setPaused(boolean isPaused) {
        this.isPaused = isPaused;
    }

}
