package provider;

import object.Ball;
import object.Paddle;

public class GameContext {
    private final InputProvider input;
    private final BoundProvider bound;
    private final Paddle paddle;
    private final Ball ball;

    public GameContext(InputProvider input, BoundProvider bound, Paddle paddle, Ball ball) {
        this.input = input;
        this.bound = bound;
        this.paddle = paddle;
        this.ball = ball;
    }

    public InputProvider getInput() {
        return input;
    }

    public BoundProvider getBounds() {
        return bound;
    }

    public Paddle getPaddle() { return paddle; }

    public Ball getBall() { return ball; }
}
