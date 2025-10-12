package main;

import UI.Text.GameText;
import UI.Text.TextManager;
import UI.Text.TextType;
import audio.SoundEffect;
import audio.SoundManager;
import exception.ExceptionHandler;
import input.KeyboardManager;
import object.*;
import utils.LevelLoaderUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

public class GameManager extends JPanel implements Runnable {
    private static volatile GameManager gameManager;
    private Thread gameThread;
    private final int width = 600;
    private final int height = 600;
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

    private GameManager() {
        this.setPreferredSize(new Dimension(width, height));
        // Load background
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

        levelData = LevelLoaderUtils.loadLevelFromJson("assets/json/levels/level1.json");

        gameContext.setWindowWidth(width);
        gameContext.setWindowHeight(height);

        Paddle paddle = new Paddle(levelData.paddle);
        gameContext.setPaddle(paddle);

        Ball ball = new Ball(levelData.ball);
        gameContext.setBall(ball);

        Background background = new Background(levelData.background);
        gameContext.setBackground(background);

        brickManager.setNormalBrickTypeId(levelData.normalBrickTypeId);
        brickManager.setStrongBrickTypeId(levelData.strongBrickTypeId);
        brickManager.initBricks(levelData);

        soundManager.loadSound(SoundEffect.NORMAL_BRICK, levelData.normalBrickSoundPath);
        soundManager.loadSound(SoundEffect.STRONG_BRICK, levelData.strongBrickSoundPath);
        soundManager.setGlobalVolume(0.6f);



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

        brickManager.renderBricks(graphics2D);

        graphics2D.setColor(Color.BLACK);
        graphics2D.setFont(new Font("Arial", Font.BOLD, 18));
        graphics2D.drawString("Bricks destroyed: " + brickManager.getDestroyedBricksCount(), width - 200, 25);

        if (gameOver || gameWin) {
            String message = gameOver ? "GAME OVER" : "YOU WIN!";
            Color messageColor = gameOver ? Color.RED : Color.GREEN;

            graphics2D.setFont(new Font("Arial", Font.BOLD, 40));
            FontMetrics fm = graphics2D.getFontMetrics();
            int textWidth = fm.stringWidth(message);
            int textHeight = fm.getHeight();

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
}
