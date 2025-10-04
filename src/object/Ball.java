package object;

import input.KeyboardManager;
import utils.IntersectUtils;

import main.GameContext;
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

        GameContext gameContext = GameContext.getInstance();

        Paddle paddle = gameContext.getPaddle();

        if (!isMoving) {
            handleInitialMovement(paddle);
        }

        followPaddleIfAttached(paddle);

        moveAndCollide();

        handleWindowCollision();
    }

    private void handleInitialMovement(Paddle paddle) {
        KeyboardManager keyboardManager = KeyboardManager.getInstance();
        if (keyboardManager.isKeyPressed(KeyEvent.VK_UP)) {
            isMoving = true;
            dy = -1;
            dx = (RandomUtils.nextBoolean() ? 1 : -1);
        } else {
            x += paddle.getDx() * paddle.getSpeed();
            y += paddle.getDy() * paddle.getSpeed();
        }
    }

    private void followPaddleIfAttached(Paddle paddle) {
        if (IntersectUtils.intersect(this, paddle)) {
            if (paddle.getDx() == 1) {
                x = paddle.getX() + paddle.getWidth();
            } else {
                x = paddle.getX() - width;
            }
        }
    }

    @Override
    public void moveAndCollide() {

        GameContext gameContext = GameContext.getInstance();
        BrickManager brickManager = BrickManager.getInstance();

        Paddle paddle = gameContext.getPaddle();
        Brick[][] bricks = brickManager.getBricks();
        int tileWidth = brickManager.getBrickWidth();
        int tileHeight = brickManager.getBrickHeight();

        moveX();
        handleObjectCollisionX(paddle);
        handleBricksCollisionX(bricks, tileWidth, tileHeight);
        moveY();
        handleObjectCollisionY(paddle);
        handleBricksCollisionY(bricks, tileWidth, tileHeight);
    }

    private boolean validBrickPosition(int tileX, int tileY, int tileBoundX, int tileBoundY) {
        return tileX >= 0 && tileY >= 0 && tileX < tileBoundX && tileY < tileBoundY;
    }

    private void handleBricksCollision(Brick[][] bricks, int tileWidth, int tileHeight, boolean checkX) {
        int topLeftTileX = x / tileWidth;
        int topLeftTileY = y / tileHeight;

        int bottomRightTileX = (x + width) / tileWidth;
        int bottomRightTileY = (y + height) / tileHeight;

        int tileBoundX = bricks[0].length;
        int tileBoundY = bricks.length;

        int[][] corners = {
                { topLeftTileX, topLeftTileY },
                { bottomRightTileX, topLeftTileY },
                { topLeftTileX, bottomRightTileY },
                { bottomRightTileX, bottomRightTileY }
        };

        for (int[] corner : corners) {
            int tileX = corner[0];
            int tileY = corner[1];

            if (validBrickPosition(tileX, tileY, tileBoundX, tileBoundY)) {
                Brick brick = bricks[tileY][tileX];
                if (brick != null && IntersectUtils.intersect(this, brick)) {

                    if (checkX) {
                        handleObjectCollisionX(brick);
                    } else {
                        handleObjectCollisionY(brick);
                    }

                    if(!brick.isHit()) {
                        brick.takeHit();
                        brick.handleHit();
                    }

                    if(brick.isDestroyed()) {
                        bricks[tileY][tileX] = null;
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

}