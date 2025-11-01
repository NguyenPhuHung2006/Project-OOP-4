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

import javax.swing.*;
import java.awt.*;

public class PlayScreen implements Screen {

    private final GameContext gameContext;
    private final BrickManager brickManager;
    private final PowerUpManager powerUpManager;
    private final ScreenType levelId;

    private GameText scoreText;
    private GameText numScoreText;
    private GameButton pauseButton;
    private Background background;
    private final String levelInitPath;
    private final String levelSavePath;

    private long startTime;
    private long pauseStartTime;
    private long pauseTime;
    private long endTime;
    private boolean isPaused;
    private boolean exited;

    public PlayScreen(Screen screen, ScreenType screenType) {

        PlayScreen playScreen = (PlayScreen) screen;

        gameContext = GameContext.getInstance();
        brickManager = BrickManager.getInstance();
        powerUpManager = PowerUpManager.getInstance();
        levelId = screenType;
        levelInitPath = playScreen.levelInitPath;
        levelSavePath = playScreen.levelSavePath;

        boolean hadSavedProgress = JsonLoaderUtils.isJsonDataAvailable(levelSavePath);

        if (hadSavedProgress) {
            boolean canLoadProgress = handleSavedProgress();
            if(exited || canLoadProgress) {
                return;
            }
        }

        init(screen);

        scoreText = new GameText(playScreen.scoreText);
        numScoreText = new GameText(playScreen.numScoreText);
        pauseButton = new GameButton(playScreen.pauseButton);
        background = new Background(playScreen.background);

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

    private boolean handleSavedProgress() {
        int option = JOptionPane.showConfirmDialog(
                null,
                "Do you want to continue playing the saved progress",
                "WARNING",
                JOptionPane.YES_NO_OPTION
        );
        if(option == JOptionPane.YES_OPTION) {
            loadSavedProgress();
            return true;
        } else if(option == JOptionPane.CLOSED_OPTION) {
            exited = true;
            screenManager.pop();
        }
        return false;
    }

    private void loadSavedProgress() {

        PlayScreen savedPlayScreen = JsonLoaderUtils.loadFromJson(levelSavePath, PlayScreen.class);

        assert savedPlayScreen != null;

        gameContext.deserializeGameContext(savedPlayScreen.gameContext);
        brickManager.deserializeBricks(savedPlayScreen.brickManager);
        powerUpManager.deserializePowerUps(savedPlayScreen.powerUpManager);

        scoreText = savedPlayScreen.scoreText;
        numScoreText = savedPlayScreen.numScoreText;
        pauseButton = savedPlayScreen.pauseButton;
        background = savedPlayScreen.background;

        scoreText.deserializeFromJson();
        numScoreText.deserializeFromJson();
        pauseButton.deserializeFromJson();
        background.deserializeFromJson();
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
                powerUpManager.pauseTimers();
                isPaused = true;
                pauseStartTime = System.currentTimeMillis();
                screenManager.push(ScreenType.PAUSE);
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

        if (exited || gameContext.isGameOver() || brickManager.isCleared()) {
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

    public long getTotalTimePlayed() {
        return endTime - startTime - pauseTime;
    }

    public void setExited(boolean exited) {
        this.exited = exited;
    }

    public String getLevelSavePath() {
        return levelSavePath;
    }

    public GameText getScoreText() {
        return scoreText;
    }

    public GameText getNumScoreText() {
        return numScoreText;
    }

    public GameButton getPauseButton() {
        return pauseButton;
    }

    public Background getBackground() {
        return background;
    }

}
