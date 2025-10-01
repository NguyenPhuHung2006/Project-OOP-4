package object;

import main.GameManager;
import utils.IntersectUtils;

import main.GameContext;

public class Ball extends MovableObject {

    public Ball(Ball ball) {
        super(ball);
    }

    @Override
    public void move() {

        GameContext gameContext = GameContext.getInstance();
        int windowWidth = gameContext.getWindowWidth();
        int windowHeight = gameContext.getWindowHeight();
        Paddle paddle = gameContext.getPaddle();

        x += dx * speed;

        if (IntersectUtils.intersect(this, paddle)) {
            if (dx > 0) {
                x = paddle.getX() - width;
            } else {
                x = paddle.getX() + paddle.getWidth();
            }
            dx *= -1;
        }

        y += dy * speed;

        if (IntersectUtils.intersect(this, paddle)) {
            if (dy > 0) {
                y = paddle.getY() - height;
            } else {
                y = paddle.getY() + paddle.getHeight();
            }
            dy *= -1;
        }

        if (x < 0 || x + width > windowWidth) {
            if (x < 0) {
                x = 0;
            } else {
                x = windowWidth - width;
            }
            dx *= -1;
        }
        if (y < 0 || y + height > windowHeight) {
            if(y < 0) {
                y = 0;
            } else {
                y = windowHeight - height;
            }
            dy *= -1;
        }

    }

    @Override
    public void update() {

        move();
    }
}
