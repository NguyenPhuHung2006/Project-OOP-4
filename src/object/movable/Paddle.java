package object.movable;

import object.GameObject;

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
        
        Paddle basePaddle = (Paddle) gameObject;

        relativeSize = basePaddle.getRelativeSize();
        relativeY = basePaddle.getRelativeY();
        
        basePaddle.resetPaddleBound();

        this.width = basePaddle.getWidth();
        this.height = basePaddle.getHeight();
        this.x = basePaddle.getX();
        this.y = basePaddle.getY();

    }
    
    public void resetPaddleBound() {
        applyRelativeSize();
        applyRelativePositionY();
        applyRelativePositionY();
        centerHorizontally();
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
