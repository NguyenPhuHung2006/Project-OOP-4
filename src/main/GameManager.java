package main;

import exception.ExceptionHandler;
import exception.InvalidGameStateException;
import exception.ResourceLoadException;
import input.KeyboardManager;
import object.Ball;
import object.LevelData;
import object.Paddle;
import utils.LevelLoaderUtils;

import javax.swing.*;
import java.awt.*;

public class GameManager extends JPanel implements Runnable {
    private static volatile GameManager gameManager;
    private Thread gameThread;
    private final int width = 500;
    private final int height = 500;
    private final Color backgroundColor = Color.white;
    private final int FPS = 60;
    private boolean running;

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

    private GameManager() {
        this.setPreferredSize(new Dimension(width, height));
        this.setBackground(backgroundColor);
        this.setDoubleBuffered(true);
        this.addKeyListener(KeyboardManager.getInstance());
        this.setFocusable(true);
        initGame();
    }

    public void startGame() {
        if (gameThread == null || !running) {
            running = true;
            gameThread = new Thread(this);
            gameThread.start();
        }
    }

    public void stopGame() {
        running = false;
    }

    @Override
    public void run() {

        while (running) {

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

    LevelData level;

    public void initGame() {

        try {
            level = LevelLoaderUtils.loadFromJson("assets/levels/level1.json");
        } catch (ResourceLoadException e) {
            ExceptionHandler.handle(e);
            stopGame();
        }

        Paddle paddle = new Paddle(level.paddle);
        Ball ball = new Ball(level.ball);

        // Brick[][] bricks = LevelLoaderUtils.loadMap(level);

        gameContext.setWindowWidth(width);
        gameContext.setWindowHeight(height);
        gameContext.setPaddle(paddle);
        gameContext.setBall(ball);
        // gameContext.setBricks(bricks);
    }

    public void updateGame() {
        gameContext.getPaddle().update();
        gameContext.getBall().update();
    }

    public void renderGame(Graphics2D graphics2D) {
        try {
            gameContext.getPaddle().render(graphics2D);
            gameContext.getBall().render(graphics2D);
        } catch (InvalidGameStateException e) {
            ExceptionHandler.handle(e);
            stopGame();
        }
    }
}
