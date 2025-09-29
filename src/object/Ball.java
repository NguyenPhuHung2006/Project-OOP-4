package object;

import main.GameManager;
import utils.IntersectUtils;

import main.GameContext;

public class Ball extends MovableObject {

    public Ball(ObjectConstant objectConstant, int speed) {

        super(objectConstant, speed);
    }

    @Override
    public void move() {
        GameContext gameContext = GameContext.getInstance();
        int windowWidth = gameContext.getWindowWidth();
        int windowHeight = gameContext.getWindowHeight();
        Paddle paddle = gameContext.getPaddle();

        int lastX = x;
        int lastY = y;

        x += dx * speed;
        if(IntersectUtils.intersect(this, paddle)) {
            dx *= -1;
        }

        y += dy * speed;
        if(IntersectUtils.intersect(this, paddle)) {
            dy *= -1;
        }

        x = lastX + dx * speed;
        y = lastY + dy * speed;

        if (x < 0) {
            x = 0;
        }
        if (y < 0) {
            y = 0;
        }
        if (x + width > windowWidth) {
            x = windowWidth - width;
        }
        if (y + height > windowHeight) {
            GameManager.getInstance().stopGame();
        }
    }

    @Override
    public void update() {

        move();
    }
}
