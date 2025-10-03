package object;

import input.KeyboardManager;
import main.GameContext;

import java.awt.event.KeyEvent;

public class Paddle extends MovableObject {

    public Paddle(Paddle paddle) {
        super(paddle);
    }

    @Override
    public void update() {

        handleInput();
        moveAndCollide();
    }

    @Override
    public Paddle clone() {
        return (Paddle)super.clone();
    }

    @Override
    protected void moveAndCollide() {

        moveX();
        handleWindowCollision();
    }

    private void handleInput() {
        KeyboardManager keyboardManager = KeyboardManager.getInstance();

        boolean leftKeyPressed = keyboardManager.isKeyPressed(KeyEvent.VK_LEFT);
        boolean rightKeyPressed = keyboardManager.isKeyPressed(KeyEvent.VK_RIGHT);

        if ((leftKeyPressed && rightKeyPressed) || (!leftKeyPressed && !rightKeyPressed)) {
            dx = 0;
        } else if (leftKeyPressed) {
            dx = -1;
        } else {
            dx = 1;
        }
    }
}
