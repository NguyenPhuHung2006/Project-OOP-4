package main;

import input.KeyboardManager;
import object.Ball;
import object.Brick;
import object.Paddle;

public class GameContext {
    private static GameContext instance;
    private int windowWidth;
    private int windowHeight;
    private Paddle paddle;
    private Ball ball;
    private Brick[][] bricks;
    private int normalBrickTypeId;
    private int strongBrickTypeId;

    public static GameContext getInstance() {
        if (instance == null) {
            instance = new GameContext();
        }
        return instance;
    }

    public void setBricks(Brick[][] bricks) {
        this.bricks = bricks;
    }

    public void setWindowWidth(int windowWidth) {
        this.windowWidth = windowWidth;
    }

    public void setWindowHeight(int windowHeight) {
        this.windowHeight = windowHeight;
    }

    public void setPaddle(Paddle paddle) {
        this.paddle = paddle;
    }

    public void setBall(Ball ball) {
        this.ball = ball;
    }

    public void setNormalBrickTypeId(int normalBrickTypeId) { this.normalBrickTypeId = normalBrickTypeId; }

    public void setStrongBrickTypeId(int strongBrickTypeId) { this.strongBrickTypeId = strongBrickTypeId; }

    public Brick[][] getBricks() {
        return bricks;
    }

    public int getWindowWidth() {
        return windowWidth;
    }

    public int getWindowHeight() {
        return windowHeight;
    }

    public Paddle getPaddle() {
        return paddle;
    }

    public Ball getBall() {
        return ball;
    }

    public int getNormalBrickTypeId() {
        return normalBrickTypeId;
    }

    public int getStrongBrickTypeId() {
        return strongBrickTypeId;
    }
}
