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
    public void update() {

        GameContext gameContext = GameContext.getInstance();
        Paddle paddle = gameContext.getPaddle();

        if (!isMoving) {
            handleInitialMovement(paddle);
        }

        followPaddleIfAttached(paddle);

        moveAndCollideWithObject(paddle);

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
}
