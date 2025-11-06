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

/**
 * The {@code PlayScreen} class serves as an abstract base class for
 * all gameplay screens (e.g., single-player and multiplayer modes).
 * <p>
 * It manages the common game logic, including initialization of
 * UI elements, object updates, rendering, game state transitions,
 * score tracking, and pause handling.
 * </p>
 *
 * <p>This class implements {@link Screen} and provides a foundation
 * for extending specialized gameplay screens with unique behaviors.</p>
 */
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

    /**
     * Constructs a {@code PlayScreen} using a base screen and screen type.
     * Initializes common gameplay elements, timers, and level data.
     *
     * @param screen      the base screen used as a template for UI layout
     * @param screenType  the type or level identifier for this screen
     */
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

    /**
     * Initializes layout and positions of UI elements such as
     * score text, pause button, and time display.
     *
     * @param screen the source screen used for layout reference
     */
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

    /**
     * Initializes all in-game objects such as bricks and power-ups
     * based on the given level configuration file.
     *
     * @param levelPath path to the JSON configuration file
     */
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

    /**
     * Updates the game logic, including player input, pause handling,
     * score updates, and checking for end-game conditions.
     */
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

    /**
     * Renders all gameplay elements including the background,
     * bricks, power-ups, and UI elements such as score and timer.
     *
     * @param graphics2D the graphics context to render onto
     */
    @Override
    public void render(Graphics2D graphics2D) {

        background.render(graphics2D);

        brickManager.renderBricks(graphics2D);

        gameContext.renderContext(graphics2D);

        powerUpManager.renderPowerUps(graphics2D);

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
