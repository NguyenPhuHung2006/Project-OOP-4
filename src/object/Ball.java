package object;

import exception.ExceptionHandler;
import exception.InvalidGameStateException;
import input.KeyboardManager;
import main.GameManager;
import utils.IntersectUtils;

import main.GameContext;
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
        GameManager gameManager = GameManager.getInstance();

        if (y + height >= gameContext.getWindowHeight()) {
            if (gameManager != null) {
                gameManager.setGameOver(true);
            }
        }

        if (!isMoving) {
            handleInitialMovement(paddle);
        }

        moveAndCollide();

        handleWindowCollision();
    }

    @Override
    protected void initScreenBounds(GameObject gameObject) {
        if(gameContext.getPaddle() == null) {
            ExceptionHandler.handle(new InvalidGameStateException("the paddle should be initialized before the ball", null));
        }
        Paddle paddle = gameContext.getPaddle();
        width = paddle.getHeight();
        height = paddle.getHeight();
        x = paddle.getX() + (paddle.getWidth() - width) / 2;
        y = paddle.getY() - height;
    }

    private void handleInitialMovement(Paddle paddle) {
        if (keyboardManager.isKeyPressed(KeyEvent.VK_UP)) {
            isMoving = true;
            dy = -1;
            dx = (RandomUtils.nextBoolean() ? 1 : -1);
        } else {
            x = paddle.getX() + (paddle.getWidth() - width) / 2.0f;
        }
    }

    @Override
    public void moveAndCollide() {

        Paddle paddle = gameContext.getPaddle();
        Brick[][] bricks = brickManager.getBricks();
        int tileWidth = brickManager.getBrickWidth();
        int tileHeight = brickManager.getBrickHeight();

        moveX();
        if (IntersectUtils.intersect(this, paddle)) {
            handleObjectCollisionX(paddle);
            PhysicsUtils.bounceOffPaddle(this, paddle);
        }
        handleBricksCollisionX(bricks, tileWidth, tileHeight);

        moveY();
        if (IntersectUtils.intersect(this, paddle)) {
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
                if (brick != null && IntersectUtils.intersect(this, brick)) {

                    if (!brick.isHit()) {
                        brick.takeHit();
                        brick.handleHit();
                    }

                    if (brick.isDestroyed()) {
                        bricks[tileY][tileX] = null;
                        brick = null;
                        brickManager.incrementDestroyedBricks();
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
