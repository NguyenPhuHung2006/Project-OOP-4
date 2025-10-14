package object;

import config.GameConfig;
import config.LevelData;
import object.Background;
import object.movable.Ball;
import object.movable.Paddle;

public class GameContext {
    private static GameContext gameContext;
    private int windowWidth;
    private int windowHeight;
    private Paddle paddle;
    private Ball ball;
    private Background background;

    private GameContext() {
    }

    public static GameContext getInstance() {
        if (gameContext == null) {
            gameContext = new GameContext();
        }
        return gameContext;
    }

    public void loadFromLevel(LevelData levelData, GameConfig gameConfig) {

        gameContext.setWindowWidth(gameConfig.windowWidth);
        gameContext.setWindowHeight(gameConfig.windowHeight);

        Paddle paddle = new Paddle(levelData.paddle);
        gameContext.setPaddle(paddle);

        Ball ball = new Ball(levelData.ball);
        gameContext.setBall(ball);

        Background background = new Background(levelData.background);
        gameContext.setBackground(background);
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

    public void setBackground(Background background) {
        this.background = background;
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

    public Background getBackground() {
        return background;
    }
}
