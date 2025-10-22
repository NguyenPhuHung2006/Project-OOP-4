package object.movable;

import exception.ExceptionHandler;
import exception.InvalidGameStateException;
import object.GameContext;
import object.GameObject;
import object.TexturedObject;
import object.brick.Brick;

import utils.PhysicsUtils;
import utils.RandomUtils;

import java.awt.event.KeyEvent;

public class Ball extends MovableObject {

    boolean isMoving;

    public Ball(Ball ball) {
        super(ball);
        isMoving = false;
    }

    @Override
    public Ball clone() {

        return (Ball) super.clone();
    }

    @Override
    public void update() {

        Paddle paddle = gameContext.getPaddle();

        if (!isMoving) {
            handleInitialMovement(paddle);
        }

        moveAndCollide();

        handleWindowCollision();

        checkGameState();
    }

    @Override
    protected void initBounds(GameObject gameObject) {

        Paddle paddle = gameContext.getPaddle();

        if(paddle == null) {
            ExceptionHandler.handle(new InvalidGameStateException("the paddle should be initialized before the ball", null));
        }

        initTextureBounds(gameObject);

        gameObject.applyRelativeSize((TexturedObject) gameObject);
        gameObject.alignAbove(paddle);
        gameObject.centerHorizontallyTo(paddle);

        this.width = gameObject.getWidth();
        this.height = gameObject.getHeight();
        this.x = gameObject.getX();
        this.y = gameObject.getY();

    }

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
        if(isIntersect(paddle)) {
            if (paddle.getDx() == 1) {
                x = paddle.getX() + paddle.getWidth();
            } else {
                x = paddle.getX() - width;
            }
        }
    }

    private void checkGameState() {

        if(y + height >= gameContext.getWindowHeight()) {
            gameContext.setGameOver(true);
        }
    }

    @Override
    public void moveAndCollide() {

        Paddle paddle = gameContext.getPaddle();
        Brick[][] bricks = brickManager.getBricks();
        int tileWidth = brickManager.getBrickWidth();
        int tileHeight = brickManager.getBrickHeight();

        followPaddleIfAttached(paddle);

        moveX();
        if (isIntersect(paddle)) {
            handleObjectCollisionX(paddle);
            PhysicsUtils.bounceOffPaddle(this, paddle);
        }
        handleBricksCollisionX(bricks, tileWidth, tileHeight);

        moveY();
        if (isIntersect(paddle)) {
            handleObjectCollisionY(paddle);
            PhysicsUtils.bounceOffPaddle(this, paddle);
        }
        handleBricksCollisionY(bricks, tileWidth, tileHeight);
    }

    private boolean validBrickPosition(int tileX, int tileY, int tileBoundX, int tileBoundY) {
        return tileX >= 0 && tileY >= 0 && tileX < tileBoundX && tileY < tileBoundY;
    }

    private void handleBricksCollision(Brick[][] bricks, int tileWidth, int tileHeight, boolean checkX) {
        int topLeftTileX = (int) x / tileWidth;
        int topLeftTileY = (int) y / tileHeight;

        int bottomRightTileX = (int)(x + width) / tileWidth;
        int bottomRightTileY = (int)(y + height) / tileHeight;

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

    public void stop() {
        dx = 0;
        dy = 0;
    }
}
