package main;

import audio.SoundEffect;
import audio.SoundManager;
import exception.ExceptionHandler;
import exception.ResourceLoadException;
import input.KeyboardManager;
import object.Ball;
import object.BrickManager;
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
    }

    public void startGame() {
        if (gameThread == null || !running) {
            running = true;
            gameThread = new Thread(this);
            gameThread.start();
            initGame();
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
                    e.printStackTrace();
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


    LevelData levelData;

    public void initGame() {
        try {
            levelData = LevelLoaderUtils.loadLevelFromJson("assets/json/levels/level1.json");
        } catch (ResourceLoadException e) {
            ExceptionHandler.handle(e);
            stopGame();
        }

        Paddle paddle = new Paddle(levelData.paddle);
        Ball ball = new Ball(levelData.ball);

        gameContext.setWindowWidth(width);
        gameContext.setWindowHeight(height);
        gameContext.setPaddle(paddle);
        gameContext.setBall(ball);

        brickManager.setNormalBrickTypeId(levelData.normalBrickTypeId);
        brickManager.setStrongBrickTypeId(levelData.strongBrickTypeId);
        brickManager.initBricks(levelData);

        SoundManager soundManager = SoundManager.getInstance();
        soundManager.loadSound(SoundEffect.NORMAL_BRICK);
        soundManager.loadSound(SoundEffect.STRONG_BRICK);
        soundManager.setGlobalVolume(0.6f);
    }

    public void updateGame() {
        if(gameContext.getPaddle() != null) {
            gameContext.getPaddle().update();
            gameContext.getBall().update();
            brickManager.updateBricks();
        }
    }

    public void renderGame(Graphics2D graphics2D) {
        if(gameContext.getPaddle() != null) {
            gameContext.getPaddle().render(graphics2D);
            gameContext.getBall().render(graphics2D);
            brickManager.renderBricks(graphics2D);
        }
    }
}
