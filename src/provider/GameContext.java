package provider;

import input.KeyboardManager;
import object.Ball;
import object.Paddle;

public class GameContext {
    private final int windowWidth;
    private final int windowHeight;
    private final KeyboardManager keyboardManager;
    private final Paddle paddle;
    private final Ball ball;

    public GameContext(int windowWidth, int windowHeight, KeyboardManager keyboardManager,
                       Paddle paddle, Ball ball) {
        this.windowWidth = windowWidth;
        this.windowHeight = windowHeight;
        this.keyboardManager = keyboardManager;
        this.paddle = paddle;
        this.ball = ball;
    }

    public int getWindowWidth() {
        return windowWidth;
    }

    public int getWindowHeight() {
        return windowHeight;
    }

    public KeyboardManager getKeyboardManager() {
        return keyboardManager;
    }

    public Paddle getPaddle() {
        return paddle;
    }

    public Ball getBall() {
        return ball;
    }
}
