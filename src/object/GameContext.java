package object;

import config.LevelConfig;
import object.movable.Ball;
import object.movable.Paddle;

public class GameContext {
    private static GameContext gameContext;

    private int windowWidth;
    private int windowHeight;
    private int paddingX;
    private int paddingY;

    private Paddle paddle;
    private Ball ball;

    private boolean gameWin;
    private boolean gameOver;

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
        Paddle paddle = new Paddle(levelConfig.paddle);
        gameContext.setPaddle(paddle);

        Ball ball = new Ball(levelConfig.ball);
        gameContext.setBall(ball);

        gameOver = false;
        gameWin = false;
    }

    public void updateContext() {
        paddle.update();
        ball.update();
    }

    private void refresh() {
        paddle = null;
        ball = null;
        gameOver = false;
        gameWin = false;
    }

    public void setWindowWidth(int windowWidth) {
        this.windowWidth = windowWidth;
        paddingX = windowWidth / 100;
    }

    public void setWindowHeight(int windowHeight) {
        this.windowHeight = windowHeight;
        paddingY = windowHeight / 100;
    }

    public void setPaddle(Paddle paddle) {
        this.paddle = paddle;
    }

    public void setBall(Ball ball) {
        this.ball = ball;
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    public void setGameWin(boolean gameWin) {
        this.gameWin = gameWin;
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

    public int getPaddingX() {
        return paddingX;
    }

    public int getPaddingY() {
        return paddingY;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public boolean isGameWin() {
        return gameWin;
    }

}
