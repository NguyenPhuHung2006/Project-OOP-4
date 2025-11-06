package main;

import audio.SoundManager;
import config.GameConfig;
import config.ScreenConfig;
import config.SoundConfig;
import exception.ExceptionHandler;
import exception.InvalidGameStateException;
import exception.ResourceLoadException;
import input.KeyboardManager;
import input.MouseManager;
import object.GameContext;
import screen.ScreenManager;
import screen.ScreenType;
import utils.JsonLoaderUtils;

import javax.swing.*;
import java.awt.*;

/**
 * The central controller of the Arkanoid game, responsible for initializing,
 * updating, and rendering all game components.
 *
 * <p>{@code GameManager} acts as the main loop handler. It manages game timing,
 * rendering, input registration, and transitions between different screens
 * (e.g. menu, play, pause, end).</p>
 *
 * <h2>Responsibilities:</h2>
 * <ul>
 *   <li>Load configuration files ({@link GameConfig}, {@link SoundConfig}, {@link ScreenConfig})</li>
 *   <li>Initialize core subsystems like {@link ScreenManager}, {@link SoundManager}, and {@link GameContext}</li>
 *   <li>Manage a fixed-timestep game loop running at 60 FPS</li>
 *   <li>Handle input through {@link KeyboardManager} and {@link MouseManager}</li>
 * </ul>
 *
 * <h2>Example:</h2>
 * <pre>
 * GameManager manager = new GameManager();
 * manager.startGame();
 * </pre>
 *
 * @author Nguyen Phu Hung
 */
public class GameManager extends JPanel implements Runnable {

    /** Singleton-like instance of the GameManager. */
    private static volatile GameManager gameManager;

    /** The thread running the main game loop. */
    private Thread gameThread;

    /** The target frames per second. */
    private final int FPS = 60;

    /** Minimum nanoseconds per frame for fixed-timestep updates. */
    private final double frameTime = 1_000_000_000.0 / FPS;

    /** Game window width loaded from {@link GameConfig}. */
    private final int width;

    /** Game window height loaded from {@link GameConfig}. */
    private final int height;

    private GameConfig gameConfig;

    private SoundManager soundManager;
    private KeyboardManager keyboardManager;
    private ScreenManager screenManager;
    private GameContext gameContext;
    private MouseManager mouseManager;

    private boolean initialized = false;

    /**
     * Constructs and configures a new {@code GameManager}.
     *
     * <p>This constructor loads the {@link GameConfig} from JSON, validates it,
     * initializes input listeners, and prepares the rendering panel.</p>
     */
    public GameManager() {
        gameConfig = JsonLoaderUtils.loadFromJson(JsonLoaderUtils.gameConfigPath, GameConfig.class);

        if (gameConfig == null) {
            ExceptionHandler.handle(new ResourceLoadException(JsonLoaderUtils.gameConfigPath, null));
        }

        assert gameConfig != null;
        width = gameConfig.windowWidth;
        height = gameConfig.windowHeight;

        // Validate window dimensions
        if (width <= 100 || height <= 100) {
            ExceptionHandler.handle(new InvalidGameStateException("the window size is too small", null));
        }

        if (width % 100 != 0) {
            ExceptionHandler.handle(new InvalidGameStateException("the window width has to be divisible by 100", null));
        }

        if (height % 100 != 0) {
            ExceptionHandler.handle(new InvalidGameStateException("the window height has to be divisible by 100", null));
        }

        setPreferredSize(new Dimension(width, height));
        setBackground(Color.white);
        setDoubleBuffered(true);

        keyboardManager = KeyboardManager.getInstance();
        mouseManager = MouseManager.getInstance();

        addKeyListener(keyboardManager);
        addMouseListener(mouseManager);
        addMouseMotionListener(mouseManager);
        addMouseWheelListener(mouseManager);
        setFocusable(true);
    }

    /**
     * Starts the game loop on a new thread if it is not already running.
     */
    public void startGame() {
        if (gameThread == null) {
            initGame();
            gameThread = new Thread(this);
            gameThread.start();
        }
    }

    /**
     * Checks whether the game is currently running.
     *
     * @return {@code true} if the game thread is active, otherwise {@code false}
     */
    public boolean isRunning() {
        return gameThread != null;
    }

    /**
     * The main game loop, responsible for updating and rendering
     * at a fixed frame rate (60 FPS).
     */
    @Override
    public void run() {
        while (isRunning()) {
            long startFrame = System.nanoTime();

            updateGame();
            repaint();

            long endFrame = System.nanoTime();
            long elapsed = endFrame - startFrame;
            long sleepTime = (long) (frameTime - elapsed);

            if (sleepTime > 0) {
                try {
                    Thread.sleep(sleepTime / 1_000_000, (int) (sleepTime % 1_000_000));
                } catch (InterruptedException e) {
                    ExceptionHandler.handle(e);
                }
            }
        }
    }

    /**
     * Handles all custom painting operations for the game.
     *
     * @param graphics the {@link Graphics} context used for drawing
     */
    @Override
    public void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        Graphics2D graphics2D = (Graphics2D) graphics;
        renderGame(graphics2D);
    }

    /**
     * Initializes the core game systems including sounds, screens,
     * and gameplay context.
     */
    public void initGame() {
        soundManager = SoundManager.getInstance();
        SoundConfig soundConfig = JsonLoaderUtils.loadFromJson(gameConfig.soundConfigPath, SoundConfig.class);
        if (soundConfig == null) {
            ExceptionHandler.handle(new ResourceLoadException(gameConfig.soundConfigPath, null));
        }

        gameContext = GameContext.getInstance();
        gameContext.setWindowWidth(width);
        gameContext.setWindowHeight(height);

        screenManager = ScreenManager.getInstance();
        ScreenConfig screenConfig = JsonLoaderUtils.loadFromJson(gameConfig.screenConfigPath, ScreenConfig.class);
        if (screenConfig == null) {
            ExceptionHandler.handle(new ResourceLoadException(gameConfig.screenConfigPath, null));
        }

        assert soundConfig != null;
        soundManager.loadFromJson(soundConfig);

        assert screenConfig != null;
        screenManager.loadFromJson(screenConfig);

        screenManager.push(ScreenType.START);

        initialized = true;
    }

    /**
     * Updates the game logic for the current frame.
     * <p>This includes input handling, entity updates, and screen transitions.</p>
     */
    public void updateGame() {
        if (!initialized) {
            return;
        }
        screenManager.update();
    }

    /**
     * Renders all visible components for the current frame.
     *
     * @param graphics2D the graphics context used for rendering
     */
    public void renderGame(Graphics2D graphics2D) {
        if (!initialized) {
            return;
        }
        screenManager.render(graphics2D);
    }
}
