package object.movable;

import object.GameObject;
import object.TexturedObject;

import java.awt.event.KeyEvent;

public class Paddle extends MovableObject {

    private final float originWidth;
    private final float originalSpeed;

    public Paddle(Paddle paddle) {

        super(paddle);
        this.originWidth = width;
        this.originalSpeed = speed;
    }

    @Override
    public void update() {

        handleInput();
        moveAndCollide();
    }

    @Override
    protected void initBounds(GameObject gameObject) {

        initTextureBounds(gameObject);

        TexturedObject texturedObject = (TexturedObject) gameObject;

        texturedObject.applyRelativeSize();
        gameObject.applyRelativePositionY();
        gameObject.centerHorizontally();

        this.width = gameObject.getWidth();
        this.height = gameObject.getHeight();
        this.x = gameObject.getX();
        this.y = gameObject.getY();

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

    public float getOriginalWidth() {
        return originWidth;
    }

    public float getOriginalSpeed() {return originalSpeed;}

}
