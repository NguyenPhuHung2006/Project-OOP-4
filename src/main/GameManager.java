package main;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

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
        this.setFocusable(true);
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
                    e.printStackTrace();
                }
            }
        }
    }

    public void paintComponent(Graphics graphic) {
        super.paintComponent(graphic);
        renderGame((Graphics2D) graphic);
    }

    public void updateGame() {

    }

    public void renderGame(Graphics graphic2D) {

    }
}
