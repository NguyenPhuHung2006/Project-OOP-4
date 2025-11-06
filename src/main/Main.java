package main;

import javax.swing.*;

/**
 * The main entry point for the Arkanoid game.
 *
 * <p>Creates and configures the main application window, attaches the
 * {@link GameManager}, and starts the game loop.</p>
 *
 * <p>This class should be executed directly to run the game:</p>
 *
 * @author Nguyen Phu Hung
 */
public class Main {

    /**
     * Launches the game by initializing the window and starting the game loop.
     *
     * @param args command-line arguments (unused)
     */
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setTitle("Arkanoid");

        GameManager gameManager = new GameManager();
        frame.add(gameManager);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        gameManager.startGame();
    }
}
