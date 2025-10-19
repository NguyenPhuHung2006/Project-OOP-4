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

public class GameManager extends JPanel implements Runnable {
    private static volatile GameManager gameManager;
    private Thread gameThread;
    private final int width;
    private final int height;
    private final int FPS = 60;

    // the minimum nanosecond at each frame
    private final double frameTime = 1_000_000_000.0 / FPS;

    public static GameManager getInstance() {
        if (gameManager == null) {
            synchronized (GameManager.class) {
                if (gameManager == null)
                    gameManager = new GameManager();
            }
        }
        return gameManager;
    }

    GameConfig gameConfig;

    private GameManager() {

        gameConfig = JsonLoaderUtils.loadFromJson("assets/json/GameConfig.json", GameConfig.class);

        if(gameConfig == null) {
            ExceptionHandler.handle(new ResourceLoadException("assets/json/GameConfig.json", null));
        }

        width = gameConfig.windowWidth;
        height = gameConfig.windowHeight;

        if (width <= 100 || height <= 100) {
            ExceptionHandler.handle(new InvalidGameStateException("the window size is too small", null));
        }

        this.setPreferredSize(new Dimension(width, height));

        Color backgroundColor = Color.white;
        this.setBackground(backgroundColor);
        this.setDoubleBuffered(true);

        keyboardManager = KeyboardManager.getInstance();
        mouseManager = MouseManager.getInstance();

        this.addKeyListener(keyboardManager);
        this.addMouseListener(mouseManager);
        this.addMouseMotionListener(mouseManager);
        this.addMouseWheelListener(mouseManager);
        this.setFocusable(true);
    }

    public void startGame() {
        if (gameThread == null) {
            initGame();
            gameThread = new Thread(this);
            gameThread.start();
        }
    }

    public void stopGame() {
        System.exit(0);
    }

    public boolean isRunning() {
        return gameThread != null;
    }

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
                    // convert sleepTime from millisecond to nanosecond
                    Thread.sleep(sleepTime / 1_000_000, (int) (sleepTime % 1_000_000));
                } catch (InterruptedException e) {
                    ExceptionHandler.handle(e);
                }
            }
        }
    }

    @Override
    public void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        Graphics2D graphics2D = (Graphics2D) graphics;
        renderGame(graphics2D);
    }

    SoundManager soundManager;
    KeyboardManager keyboardManager;
    ScreenManager screenManager;
    GameContext gameContext;
    MouseManager mouseManager;

    boolean initialized = false;

    public void initGame() {

        soundManager = SoundManager.getInstance();
        screenManager = ScreenManager.getInstance();
        gameContext = GameContext.getInstance();

        SoundConfig soundConfig = JsonLoaderUtils.loadFromJson(gameConfig.soundConfigPath, SoundConfig.class);

        if(soundConfig == null) {
            ExceptionHandler.handle(new ResourceLoadException(gameConfig.soundConfigPath, null));
        }

        ScreenConfig screenConfig = JsonLoaderUtils.loadFromJson(gameConfig.screenConfigPath, ScreenConfig.class);

        if(screenConfig == null) {
            ExceptionHandler.handle(new ResourceLoadException(gameConfig.screenConfigPath, null));
        }

        gameContext.setWindowWidth(width);
        gameContext.setWindowHeight(height);

        assert soundConfig != null;
        soundManager.loadFromJson(soundConfig);
        assert screenConfig != null;
        screenManager.loadFromJson(screenConfig);

        screenManager.push(ScreenType.MENU);

        initialized = true;
    }

    public void updateGame() {

        if(!initialized) {
            return;
        }
        screenManager.update();
    }

    public void renderGame(Graphics2D graphics2D) {

        if(!initialized) {
            return;
        }
        screenManager.render(graphics2D);
    }
}
