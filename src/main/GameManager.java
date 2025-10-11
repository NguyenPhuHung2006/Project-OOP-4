package main;

import audio.SoundEffect;
import audio.SoundManager;
import exception.ExceptionHandler;
import exception.ResourceLoadException;
import input.KeyboardManager;
import object.*;
import utils.LevelLoaderUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

public class GameManager extends JPanel implements Runnable {
    private static volatile GameManager gameManager;
    private Thread gameThread;
    private final int width = 500;
    private final int height = 500;
    private final int FPS = 60;
    private int destroyedBricksCount = 0; // đếm số gạch bị phá
    private boolean gameOver = false;
    private boolean gameWin = false;
    private boolean running;
    private Background background;

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
        // Load background
        Color backgroundColor = Color.white;
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
        background = new Background(levelData.background);

        background.setX(0);
        background.setY(0);
        background.setWidth(width);
        background.setHeight(height);

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
        // Khi Game Over hoặc You Win
        if (gameOver || gameWin) {
            KeyboardManager keyboard = KeyboardManager.getInstance();

            // Nhấn ENTER để chơi lại
            if (keyboard.isKeyPressed(KeyEvent.VK_ENTER)) {
                resetGame();
            }

            // Nhấn ESC để thoát game
            if (keyboard.isKeyPressed(KeyEvent.VK_ESCAPE)) {
                System.exit(0);
            }

            return;
        }
        gameContext.getPaddle().update();
        gameContext.getBall().update();
        brickManager.updateBricks();

        checkWinCondition();
    }

    private void checkWinCondition() {
        Brick[][] bricks = BrickManager.getInstance().getBricks();
        for (Brick[] row : bricks) {
            for (Brick brick : row) {
                if (brick != null && !brick.isDestroyed()) {
                    return; // vẫn còn gạch → chưa thắng
                }
            }
        }
        gameWin = true;
        // Dừng bóng ngay lập tức
        gameContext.getBall().setDx(0);
        gameContext.getBall().setDy(0);
    }

    public void resetGame() {
        destroyedBricksCount = 0;
        gameOver = false;
        gameWin = false;

        GameContext gameContext = GameContext.getInstance();

        // Tải lại level từ file JSON
        try {
            levelData = LevelLoaderUtils.loadLevelFromJson("assets/json/levels/level1.json");
        } catch (ResourceLoadException e) {
            ExceptionHandler.handle(e);
            stopGame();
        }

        Paddle paddle = new Paddle(levelData.paddle);
        Ball ball = new Ball(levelData.ball);

        gameContext.setPaddle(paddle);
        gameContext.setBall(ball);
        brickManager.initBricks(levelData);
    }


    public void renderGame(Graphics2D graphics2D) {

        background.render(graphics2D);
        brickManager.renderBricks(graphics2D);
        gameContext.getPaddle().render(graphics2D);
        gameContext.getBall().render(graphics2D);

        brickManager.renderBricks(graphics2D);

        graphics2D.setColor(Color.BLACK);
        graphics2D.setFont(new Font("Arial", Font.BOLD, 18));
        graphics2D.drawString("Bricks destroyed: " + destroyedBricksCount, width - 200, 25);

        // Nếu game kết thúc (thắng hoặc thua)
        if (gameOver || gameWin) {
            String message = gameOver ? "GAME OVER" : "YOU WIN!";
            Color messageColor = gameOver ? Color.RED : Color.GREEN;

            graphics2D.setFont(new Font("Arial", Font.BOLD, 40));
            FontMetrics fm = graphics2D.getFontMetrics();
            int textWidth = fm.stringWidth(message);
            int textHeight = fm.getHeight();

            // Vẽ dòng chữ chính giữa
            graphics2D.setColor(messageColor);
            graphics2D.drawString(
                    message,
                    (width - textWidth) / 2,
                    (height + textHeight) / 2
            );

            graphics2D.setFont(new Font("Arial", Font.PLAIN, 20));
            graphics2D.setColor(Color.BLACK);
            String hintRestart = "Press ENTER to restart";
            String hintExit = "Press ESC to exit";
            int hintRestartWidth = graphics2D.getFontMetrics().stringWidth(hintRestart);
            int hintExitWidth = graphics2D.getFontMetrics().stringWidth(hintExit);

            graphics2D.drawString(hintRestart, (width - hintRestartWidth) / 2, (height + textHeight) / 2 + 40);
            graphics2D.drawString(hintExit, (width - hintExitWidth) / 2, (height + textHeight) / 2 + 70);
        }
    }

    public void incrementDestroyedBricks() {
        destroyedBricksCount++;
    }

    public int getDestroyedBricksCount() {
        return destroyedBricksCount;
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    public boolean isGameOver() {
        return gameOver;
    }

}
