package object;

import main.GameManager;
import java.awt.event.KeyEvent;
import input.KeyboardManager;

public class Paddle extends MovableObject {
    public Paddle(ObjectConstant objectConstant, int speed) {
        super(objectConstant, speed);
    }

    @Override
    public void move() {
        KeyboardManager keyboardManager = KeyboardManager.getInstance();
        boolean leftKeyPressed = keyboardManager.isKeyPressed(KeyEvent.VK_LEFT);
        boolean rightKeyPressed = keyboardManager.isKeyPressed(KeyEvent.VK_RIGHT);
        int windowWidth = GameManager.getInstance().getWidth();

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
