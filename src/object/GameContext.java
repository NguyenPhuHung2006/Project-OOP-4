package object;

import config.LevelConfig;
import object.UI.LifeCounter;
import object.movable.Ball;
import object.movable.Paddle;

import java.awt.*;

public class GameContext {
    private static GameContext gameContext;

    private int windowWidth;
    private int windowHeight;
    private int paddingX;
    private int paddingY;

    private Paddle paddle;
    private Ball ball;
    private LifeCounter lifeCounter;

    private GameContext() {
    }

    public static GameContext getInstance() {
        if (gameContext == null) {
            gameContext = new GameContext();
        }
        return gameContext;
    }

    public void loadFromJson(LevelConfig levelConfig) {

        refresh();

        paddle = new Paddle(levelConfig.paddle);
        ball = new Ball(levelConfig.ball);
        lifeCounter = new LifeCounter(levelConfig.lifeCounter);
    }

    public void updateContext() {
        paddle.update();
        ball.update();
        lifeCounter.update();
    }

    public void renderContext(Graphics2D graphics2D) {
        paddle.render(graphics2D);
        ball.render(graphics2D);
        lifeCounter.render(graphics2D);
    }

    public void resetObjectsBound() {
        paddle.resetPaddleBound();
        ball.resetBallBound(paddle);
    }

    private void refresh() {
        paddle = null;
        ball = null;
        lifeCounter = null;
    }

    public void setWindowWidth(int windowWidth) {
        this.windowWidth = windowWidth;
        paddingX = windowWidth / 100;
    }

    public void setWindowHeight(int windowHeight) {
        this.windowHeight = windowHeight;
        paddingY = windowHeight / 100;
    }

    public boolean isGameOver() {
        return lifeCounter.isOutOfLives();
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

    public LifeCounter getLifeCounter() {
        return lifeCounter;
    }

    public int getPaddingX() {
        return paddingX;
    }

    public int getPaddingY() {
        return paddingY;
    }

}
