package provider;

import object.Paddle;

public class GameContext {
    private final InputProvider input;
    private final BoundProvider bound;
    private final Paddle paddle;

    public GameContext(InputProvider input, BoundProvider bound, Paddle paddle) {
        this.input = input;
        this.bound = bound;
        this.paddle = paddle;
    }

    public InputProvider getInput() {
        return input;
    }

    public BoundProvider getBounds() {
        return bound;
    }

    public Paddle getPaddle() { return paddle; }
}
