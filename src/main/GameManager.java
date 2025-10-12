package main;

import UI.Text.TextManager;
import UI.Text.TextType;
import audio.SoundEffect;
import audio.SoundManager;
import config.GameConfig;
import config.LevelData;
import exception.ExceptionHandler;
import exception.InvalidGameStateException;
import exception.ResourceLoadException;
import input.KeyboardManager;
import object.*;
import utils.JsonLoaderUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

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

    GameConfig gameConfig = JsonLoaderUtils.loadConfigFromJson("assets/json/GameConfig.json");

    private GameManager() {
        width = gameConfig.windowWidth;
        height = gameConfig.windowHeight;

        if(width <= 100 || height <= 100) {
            ExceptionHandler.handle(new InvalidGameStateException("window size is too small", null));
        }

        this.setPreferredSize(new Dimension(width, height));

        Color backgroundColor = Color.white;
        this.setBackground(backgroundColor);
        this.setDoubleBuffered(true);
        this.addKeyListener(KeyboardManager.getInstance());
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

    GameContext gameContext = GameContext.getInstance();
    BrickManager brickManager = BrickManager.getInstance();
    SoundManager soundManager = SoundManager.getInstance();
    KeyboardManager keyboardManager = KeyboardManager.getInstance();
    TextManager textManager = TextManager.getInstance();

    LevelData levelData;

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    public boolean isGameOver() {
        return gameOver;
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

    public void handleGameState() {

        if(gameWin || gameOver) {
            if(keyboardManager.isKeyPressed(KeyEvent.VK_ENTER)) {
                resetGame();
            }
            if(keyboardManager.isKeyPressed(KeyEvent.VK_ESCAPE)) {
                stopGame();
            }
        }
    }

    public void initGame() {

        levelData = JsonLoaderUtils.loadLevelFromJson("assets/json/levels/Level1.json");

        if(levelData == null) {
            ExceptionHandler.handle(new ResourceLoadException("assets/json/levels/Level1.json", null));
        }

        gameContext.loadFromLevel(levelData, gameConfig);

        textManager.loadFromLevel(levelData);

        brickManager.loadFromLevel(levelData);

        soundManager.loadFromLevel(levelData);

        initialized = true;
    }

    public void updateGame() {

        handleGameState();

        if (!initialized || gameWin || gameOver) {
            return;
        }

        gameContext.getBackground().update();
        gameContext.getPaddle().update();
        gameContext.getBall().update();
        brickManager.updateBricks();

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

        textManager.getText(TextType.BRICK_DESTROYED).render(graphics2D);
        textManager.getText(TextType.SCORE).render(graphics2D);

        brickManager.renderBricks(graphics2D);

        if(gameOver || gameWin) {
            if(gameOver) {
                textManager.getText(TextType.GAME_OVER).render(graphics2D);
            } else if(gameWin) {
                textManager.getText(TextType.GAME_WIN).render(graphics2D);
            }
            textManager.getText(TextType.PRESS_ENTER).render(graphics2D);
            textManager.getText(TextType.PRESS_ESC).render(graphics2D);
        }
    }
}
