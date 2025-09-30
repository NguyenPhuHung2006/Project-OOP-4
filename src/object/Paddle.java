package object;

import input.KeyboardManager;
import main.GameContext;

import java.awt.event.KeyEvent;

public class Paddle extends MovableObject {

    public Paddle(Paddle paddle) {
        super(paddle);
    }

    @Override
    public void move() {
        GameContext gameContext = GameContext.getInstance();
        KeyboardManager keyboardManager = KeyboardManager.getInstance();

        boolean leftKeyPressed = keyboardManager.isKeyPressed(KeyEvent.VK_LEFT);
        boolean rightKeyPressed = keyboardManager.isKeyPressed(KeyEvent.VK_RIGHT);
        int windowWidth = gameContext.getWindowWidth();

        // if both left and right key pressed or none of them is pressed, don't move
        if ((leftKeyPressed && rightKeyPressed) || (!leftKeyPressed && !rightKeyPressed)) {
            dx = 0;
        } else if (leftKeyPressed) {
            dx = -1;
        } else {
            dx = 1;
        }

        x += dx * speed;

        if (x < 0) {
            x = 0;
        }
        if (x + width > windowWidth) {
            x = windowWidth - width;
        }
    }

    @Override
    public void update() {

        move();
    }
}
