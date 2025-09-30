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

    }

    @Override
    public void update() {

        move();
    }
}
