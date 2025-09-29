package provider;

public class GameContext {
    private final InputProvider input;
    private final BoundProvider bound;

    public GameContext(InputProvider input, BoundProvider bound) {
        this.input = input;
        this.bound = bound;
    }

    public InputProvider getInput() {
        return input;
    }

    public BoundProvider getBounds() {
        return bound;
    }
}
