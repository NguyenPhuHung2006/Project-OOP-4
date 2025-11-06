package object.movable;

import object.GameObject;

import java.awt.event.KeyEvent;

/**
 * Represents the player's paddle in the Arkanoid game.
 * <p>
 * The paddle can move horizontally based on keyboard input
 * and interacts with the ball during gameplay.
 */
public class Paddle extends MovableObject {

    /**
     * Copy constructor for creating a new paddle based on an existing one.
     *
     * @param paddle the paddle to copy data from
     */
    public Paddle(Paddle paddle) {

        super(paddle);
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

    /**
     * Resets the paddle’s position and size based on relative configuration.
     */
    public void resetPaddleBound() {
        applyRelativeSize();
        applyRelativePositionY();
        applyRelativePositionY();
        centerHorizontally();
    }

    @Override
    public Paddle clone() {
        return (Paddle) super.clone();
    }

    /**
     * Moves the paddle horizontally and handles collisions with window boundaries.
     */
    @Override
    protected void moveAndCollide() {

        moveX();
        handleWindowCollision();
    }

    /**
     * Handles keyboard input for moving the paddle.
     */
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
