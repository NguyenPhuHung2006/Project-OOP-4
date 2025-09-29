package object;

import input.KeyboardManager;
import main.GameContext;

import java.awt.event.KeyEvent;

public class Paddle extends MovableObject {
    public Paddle(ObjectConstant objectConstant, int speed) {
        super(objectConstant, speed);
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

        int newX = x + dx * speed;

        if (newX < 0) {
            newX = 0;
        }
        if (newX + width > windowWidth) {
            newX = windowWidth - width;
        }

        x = newX;
    }

    @Override
    public void update() {

        move();
    }
}
