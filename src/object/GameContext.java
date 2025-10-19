package object;

import config.GameConfig;
import config.LevelConfig;
import object.movable.Ball;
import object.movable.Paddle;

public class GameContext {
    private static GameContext gameContext;
    private int windowWidth;
    private int windowHeight;
    private Paddle paddle;
    private Ball ball;

    private GameContext() {
    }

    public static GameContext getInstance() {
        if (gameContext == null) {
            gameContext = new GameContext();
        }
        return gameContext;
    }

    public void loadFromJson(LevelConfig levelConfig) {

        Paddle paddle = new Paddle(levelConfig.paddle);
        gameContext.setPaddle(paddle);

        Ball ball = new Ball(levelConfig.ball);
        gameContext.setBall(ball);
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

}
