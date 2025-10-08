package object;

import input.KeyboardManager;
import main.GameManager;
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
        GameManager gameManager = gameContext.getGameManager();

        if (y + height >= gameContext.getWindowHeight()) {
            if (gameManager != null) {
                gameManager.setGameOver(true);
            }
        }

        if (!isMoving) {
            handleInitialMovement(paddle);
        }

        followPaddleIfAttached(paddle);

        moveAndCollide();

        handleWindowCollision();
        //Thêm dòng này để xử lý va chạm bóng – gạch
        handleBricksCollision();
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

<<<<<<< Updated upstream
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
=======
    /**
     * Xử lý va chạm giữa bóng (Ball) và gạch (Brick).
     * - Nếu bóng chạm gạch → phá gạch, tăng bộ đếm số gạch bị phá, đổi hướng di chuyển.
     * - Nếu có gạch rơi xuống dưới paddle → kết thúc game (Game Over).
     */
    private void handleBricksCollision() {
        GameContext gameContext = GameContext.getInstance();
        Paddle paddle = gameContext.getPaddle();
        Brick[][] bricks = gameContext.getBricks();

        if (bricks == null || paddle == null) return;

        for (Brick[] row : bricks) {
            for (Brick brick : row) {
                if (brick == null || brick.isDestroyed()) continue;

                // Nếu gạch đã rơi xuống dưới paddle => Game Over
                if (brick.getY() + brick.getHeight() > paddle.getY()) {
                    if (gameContext.getGameManager() != null) {
                        gameContext.getGameManager().setGameOver(true);
                    }
                    return;
                }

                // Kiểm tra va chạm bóng <-> gạch
                if (IntersectUtils.intersect(this, brick)) {

                    // Đánh dấu gạch bị phá
                    brick.setDestroyed(true);

                    // Báo cho GameManager tăng bộ đếm
                    if (gameContext.getGameManager() != null) {
                        gameContext.getGameManager().incrementDestroyedBricks();
                    }

                    // Đổi hướng bóng
                    dy = -dy;

                    // Đặt lại vị trí bóng để tránh bị kẹt trong gạch
                    if (dy > 0) {
                        // Bóng đi xuống -> đặt bóng dưới gạch
                        y = brick.getY() + brick.getHeight();
                    } else {
                        // Bóng đi lên -> đặt bóng trên gạch
                        y = brick.getY() - height;
                    }

                    // Sau khi va chạm với 1 gạch thì dừng lại (tránh xử lý nhiều gạch cùng lúc)
                    return;
>>>>>>> Stashed changes
                }
            }
        }
    }

<<<<<<< Updated upstream
    private void handleBricksCollisionX(Brick[][] bricks, int tileWidth, int tileHeight) {
        handleBricksCollision(bricks, tileWidth, tileHeight, true);
    }

    private void handleBricksCollisionY(Brick[][] bricks, int tileWidth, int tileHeight) {
        handleBricksCollision(bricks, tileWidth, tileHeight, false);
    }

}
=======
}
>>>>>>> Stashed changes
