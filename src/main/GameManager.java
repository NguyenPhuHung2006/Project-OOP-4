package main;

import audio.SoundManager;
import config.GameConfig;
import config.LevelData;
import exception.ExceptionHandler;
import exception.InvalidGameStateException;
import exception.ResourceLoadException;
import input.KeyboardManager;
import object.*;
import object.brick.BrickManager;
import object.movable.powerup.PowerUpManager;
import screen.ScreenManager;
import utils.JsonLoaderUtils;

import javax.swing.*;
import java.awt.*;

public class GameManager extends JPanel implements Runnable {
    private static volatile GameManager gameManager;
    private Thread gameThread;
    private final int width;
    private final int height;
    private final int FPS = 60;
    private boolean gameOver = false;
    private boolean gameWin = false;

    private volatile boolean initialized = false;

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
    GameContext gameContext;
    BrickManager brickManager;
    SoundManager soundManager;
    KeyboardManager keyboardManager;
    PowerUpManager powerUpManager;
    ScreenManager screenManager;

    private GameManager() {

        gameConfig = JsonLoaderUtils.loadConfigFromJson("assets/json/GameConfig.json");

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
        this.addKeyListener(KeyboardManager.getInstance());
        this.setFocusable(true);

        gameContext = GameContext.getInstance();
        brickManager = BrickManager.getInstance();
        soundManager = SoundManager.getInstance();
        keyboardManager = KeyboardManager.getInstance();
        powerUpManager = PowerUpManager.getInstance();
        screenManager = ScreenManager.getInstance();
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

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public boolean isGameWin() {
        return gameWin;
    }

    private void checkGameCondition() {

        if (brickManager.isCleared()) {
            gameWin = true;
            gameContext.getBall().stop();
        }
    }

    public void resetGame() {

        gameOver = false;
        gameWin = false;

        initGame();
    }

    LevelData levelData;

    public void initGame() {

        levelData = JsonLoaderUtils.loadLevelFromJson(gameConfig.levelPath);

        if (levelData == null) {
            ExceptionHandler.handle(new ResourceLoadException(gameConfig.levelPath, null));
        }

        gameContext.loadFromJson(levelData, gameConfig);
        brickManager.loadFromJson(levelData);
        soundManager.loadFromJson(levelData);
        powerUpManager.loadFromJson(levelData);

        initialized = true;
    }

    public void updateGame() {

        keyboardManager.handleGameState();

        if (!initialized || gameWin || gameOver) {
            return;
        }

        gameContext.getBackground().update();
        gameContext.getPaddle().update();
        gameContext.getBall().update();
        brickManager.updateBricks();
        powerUpManager.updateFallingPowerUps();
        powerUpManager.updateActivePowerUps();

        checkGameCondition();
    }

    public void renderGame(Graphics2D graphics2D) {

        if (!initialized) {
            return;
        }

        gameContext.getBackground().render(graphics2D);
        brickManager.renderBricks(graphics2D);
        gameContext.getPaddle().render(graphics2D);
        gameContext.getBall().render(graphics2D);

        powerUpManager.renderPowerUps(graphics2D);

        brickManager.renderBricks(graphics2D);
    }
}
