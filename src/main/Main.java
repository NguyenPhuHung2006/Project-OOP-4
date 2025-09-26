package main;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setTitle("Arkanoid");

        GameManager gameManager = GameManager.getInstance();
        frame.add(gameManager);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        gameManager.startGame();
    }
}
