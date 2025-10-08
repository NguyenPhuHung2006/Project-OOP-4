package main;

import input.KeyboardManager;
import object.Ball;
import object.Brick;
import object.Paddle;
import main.GameManager;

public class GameContext {
    private static GameContext instance;
    private int windowWidth;
    private int windowHeight;
    private Paddle paddle;
    private Ball ball;

    private GameManager gameManager;
    private GameContext() {}

    public static GameContext getInstance() {
        if (instance == null) {
            instance = new GameContext();
        }
        return instance;
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

<<<<<<< Updated upstream
=======
    public void setNormalBrickTypeId(int normalBrickTypeId) { this.normalBrickTypeId = normalBrickTypeId; }

    public void setStrongBrickTypeId(int strongBrickTypeId) { this.strongBrickTypeId = strongBrickTypeId; }

    public void setGameManager(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    public Brick[][] getBricks() {
        return bricks;
    }

>>>>>>> Stashed changes
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

<<<<<<< Updated upstream
=======
    public int getNormalBrickTypeId() {
        return normalBrickTypeId;
    }

    public int getStrongBrickTypeId() {
        return strongBrickTypeId;
    }

    public GameManager getGameManager() {
        return gameManager;
    }
>>>>>>> Stashed changes
}
