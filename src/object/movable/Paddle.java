package object.movable;

import object.GameObject;
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
    protected void initScreenBounds(GameObject gameObject) {

        width = gameContext.getWindowWidth() / 5.0f;
        height = width * (1.0f * gameObject.getTextureHeight() / gameObject.getTextureWidth());

        y = gameContext.getWindowHeight() * 20.0f / 25;

        x = (gameContext.getWindowWidth() - width) / 2;
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
