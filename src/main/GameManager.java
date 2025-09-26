package main;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class GameManager extends JPanel implements Runnable
{
    private static volatile GameManager gameManager;
    private Thread gameThread;
    private final int width = 500;
    private final int height = 500;
    private final Color backgroundColor = Color.white;

    public static GameManager getInstance()
    {
        if(gameManager == null)
        {
            synchronized(GameManager.class)
            {
                if(gameManager == null)
                    gameManager = new GameManager();
            }
        }
        return gameManager;
    }

    private GameManager()
    {
        this.setPreferredSize(new Dimension(width, height));
        this.setBackground(backgroundColor);
        this.setDoubleBuffered(true);
        this.setFocusable(true);
    }

    public void startGame()
    {
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run()
    {
        while(gameThread != null)
        {
            updateGame();
            repaint();
        }
    }

    public void updateGame()
    {

    }

    public void renderGame(Graphics graphic2D)
    {

    }

    public void paintComponent(Graphics graphic)
    {
        super.paintComponent(graphic);
        Graphics2D graphic2D = (Graphics2D) graphic.create();

        try {
            renderGame(graphic2D);
        } finally {
            graphic2D.dispose();
        }
    }
}
