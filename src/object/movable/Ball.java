package object.movable;

import audio.SoundType;
import exception.ExceptionHandler;
import exception.InvalidGameStateException;
import object.GameObject;
import object.brick.Brick;

import utils.PhysicsUtils;
import utils.RandomUtils;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents the ball in the Arkanoid game.
 * <p>
 * The ball moves, bounces off the paddle, bricks, and window walls.
 * It also detects when it falls below the screen (i.e., is lost).
 */
public class Ball extends MovableObject {

    private boolean isMoving;
    private boolean isBallLost;

    private Paddle paddle;

    /**
     * Copy constructor for creating a new ball based on an existing one.
     *
     * @param ball the ball to copy from
     */
    public Ball(Ball ball) {
        super(ball);
        isMoving = false;
    }

    @Override
    public Ball clone() {

        return (Ball) super.clone();
    }

    /**
     * Updates the ball's state each frame — handles movement, collisions,
     * and checks for ball loss.
     */
    @Override
    public void update() {

        Paddle paddle = gameContext.getPaddle();

        if (!isMoving) {
            handleInitialMovement(paddle);
        }

        moveAndCollide();

        handleWindowCollision();

        checkBallState();
    }

    @Override
    protected void initBounds(GameObject gameObject) {

        paddle = gameContext.getPaddle();

        if (paddle == null) {
            ExceptionHandler.handle(new InvalidGameStateException("the paddle should be initialized before the ball", null));
        }

        initTextureBounds(gameObject);

        Ball baseBall = (Ball) gameObject;

        relativeSize = baseBall.getRelativeSize();

        baseBall.resetBallBound(paddle);

        this.width = baseBall.getWidth();
        this.height = baseBall.getHeight();
        this.x = baseBall.getX();
        this.y = baseBall.getY();
    }

    /**
     * Resets the ball to sit above the paddle and stops its movement.
     *
     * @param paddle the paddle to align with
     */
    public void resetBallBound(Paddle paddle) {
        isMoving = false;
        stop();
        applyRelativeSize();
        alignAbove(paddle);
        centerHorizontallyTo(paddle);
        translateY(paddingY);
    }

    /**
     * Handles the initial launch of the ball when the UP key is pressed.
     *
     * @param paddle the paddle used as a reference point
     */
    private void handleInitialMovement(Paddle paddle) {
        if (keyboardManager.isKeyPressed(KeyEvent.VK_UP)) {
            isMoving = true;
            dy = -1;
            dx = RandomUtils.nextFloat(-0.8f, 0.8f);
        } else {
            x = paddle.getX() + (paddle.getWidth() - width) / 2.0f;
        }
    }

    private void followPaddleIfAttached(Paddle paddle) {
        if (isIntersect(paddle)) {
            if (paddle.getDx() == 1) {
                x = paddle.getX() + paddle.getWidth();
            } else {
                x = paddle.getX() - width;
            }
        }
    }

    /**
     * Moves the ball and handles collisions with the paddle and bricks.
     */
    @Override
    public void moveAndCollide() {

        Brick[][] bricks = brickManager.getBricks();
        int tileWidth = brickManager.getBrickWidth();
        int tileHeight = brickManager.getBrickHeight();

        followPaddleIfAttached(paddle);

        moveX();
        if (isIntersect(paddle)) {
            handleObjectCollisionX(paddle);
            PhysicsUtils.bounceOffPaddle(this, paddle);
            soundManager.play(SoundType.PLAYER_PADDLE);
        }
        handleBricksCollisionX(bricks, tileWidth, tileHeight);

        moveY();
        if (isIntersect(paddle)) {
            handleObjectCollisionY(paddle);
            PhysicsUtils.bounceOffPaddle(this, paddle);
            soundManager.play(SoundType.PLAYER_PADDLE);
        }
        handleBricksCollisionY(bricks, tileWidth, tileHeight);
    }

    private boolean validBrickPosition(int tileX, int tileY, int tileBoundX, int tileBoundY) {
        return tileX >= 0 && tileY >= 0 && tileX < tileBoundX && tileY < tileBoundY;
    }

    private void handleBricksCollision(Brick[][] bricks, int tileWidth, int tileHeight, boolean checkX) {
        int topLeftTileX = (int) x / tileWidth;
        int topLeftTileY = (int) y / tileHeight;

        int bottomRightTileX = (int) (x + width) / tileWidth;
        int bottomRightTileY = (int) (y + height) / tileHeight;

        int tileBoundX = bricks[0].length;
        int tileBoundY = bricks.length;

        int[][] corners = {
                {topLeftTileX, topLeftTileY},
                {bottomRightTileX, topLeftTileY},
                {topLeftTileX, bottomRightTileY},
                {bottomRightTileX, bottomRightTileY}
        };

        for (int[] corner : corners) {
            int tileX = corner[0];
            int tileY = corner[1];

            if (validBrickPosition(tileX, tileY, tileBoundX, tileBoundY)) {
                Brick brick = bricks[tileY][tileX];
                if (brick != null && isIntersect(brick)) {

                    if (!brick.isHit()) {
                        brick.takeHit();
                    }

                    if (checkX) {
                        handleObjectCollisionX(brick);
                    } else {
                        handleObjectCollisionY(brick);
                    }

                    break;
                }
            }
        }
    }

    private void handleBricksCollisionX(Brick[][] bricks, int tileWidth, int tileHeight) {
        handleBricksCollision(bricks, tileWidth, tileHeight, true);
    }

    private void handleBricksCollisionY(Brick[][] bricks, int tileWidth, int tileHeight) {
        handleBricksCollision(bricks, tileWidth, tileHeight, false);
    }

    /**
     * Checks if the ball has been lost (fallen below the screen).
     */
    private void checkBallState() {

        if (y + height >= gameContext.getWindowHeight()) {
            isBallLost = true;
        }

        if (isIntersect(paddle)) {
            y = paddle.getY() + paddle.getHeight() + 1;
            dy = Math.abs(dy);
        }
    }

    public void stop() {
        dx = 0;
        dy = 0;
    }

    /**
     * @return true if the ball is lost (and resets the flag)
     */
    public boolean isLost() {
        if (isBallLost) {
            isBallLost = false;
            return true;
        }
        return false;
    }

    public void setPaddle(Paddle paddle) {
        this.paddle = paddle;
    }
}
