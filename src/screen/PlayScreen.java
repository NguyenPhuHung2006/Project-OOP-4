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
import utils.TextUtils;

import com.esotericsoftware.kryonet.*;

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
    private GameText numTimeText;
    private Background background;

    private String levelInitPath;
    private String levelSavePath;

    private long startTime;
    private long pauseStartTime;
    private long pauseTime;
    private long endTime;
    private long totalTimePlayed;

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

        startTime = System.currentTimeMillis();

        if (hadSavedProgress) {
            boolean canLoadProgress = handleSavedProgress();
            if(exited || canLoadProgress) {
                return;
            }
        }

        JsonLoaderUtils.clearJsonFile(levelSavePath);

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
        }
        return false;
    }

    public void saveGameProgress() {

        gameContext.serializeGameContext();
        powerUpManager.serializePowerUps();
        brickManager.serializeBricks();

        scoreText.serializeToJson();
        numScoreText.serializeToJson();
        pauseButton.serializeToJson();
        numTimeText.serializeToJson();
        background.serializeToJson();

        getTotalTimePlayedBeforeExit();

        JsonLoaderUtils.saveToJson(levelSavePath, this);

        exited = true;
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
        numTimeText = savedPlayScreen.numTimeText;
        background = savedPlayScreen.background;

        levelInitPath = savedPlayScreen.levelInitPath;
        levelSavePath = savedPlayScreen.levelSavePath;

        scoreText.deserializeFromJson();
        numScoreText.deserializeFromJson();
        pauseButton.deserializeFromJson();
        numTimeText.deserializeFromJson();
        background.deserializeFromJson();

        totalTimePlayed = savedPlayScreen.totalTimePlayed;
        isPaused = false;
        powerUpManager.resumeTimers();
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
                endTime = System.currentTimeMillis();
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

}
