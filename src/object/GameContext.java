package object;

import config.LevelConfig;
import object.UI.LifeCounter;
import object.movable.Ball;
import object.movable.Paddle;

import java.awt.*;

/**
 * The {@code GameContext} class serves as a singleton container
 * holding all core game objects (such as the paddle, ball, and life counter)
 * and global state parameters like window dimensions, scaling, and padding.
 * <p>
 * This class centralizes management of the main gameplay objects
 * and handles their initialization, rendering, and serialization.
 */
public class GameContext {
    private static GameContext gameContext;

    private int windowWidth;
    private int windowHeight;
    private int paddingX;
    private int paddingY;
    private float scaled;

    private Paddle paddle;
    private Ball ball;
    private LifeCounter lifeCounter;

    /** Private constructor to enforce singleton pattern. */
    private GameContext() {
    }

    /**
     * Returns the singleton instance of the {@code GameContext}.
     *
     * @return the shared {@code GameContext} instance
     */
    public static GameContext getInstance() {
        if (gameContext == null) {
            gameContext = new GameContext();
        }
        return gameContext;
    }

    /**
     * Loads the game objects (paddle, ball, and life counter)
     * based on configuration data from a {@link LevelConfig}.
     *
     * @param levelConfig the level configuration containing object parameters
     */
    public void loadFromJson(LevelConfig levelConfig) {
        refresh();
        paddle = new Paddle(levelConfig.paddle);
        ball = new Ball(levelConfig.ball);
        lifeCounter = new LifeCounter(levelConfig.lifeCounter);
    }

    /** Updates all core gameplay objects. */
    public void updateContext() {
        paddle.update();
        ball.update();
        lifeCounter.update();
    }

    /**
     * Renders all core gameplay objects.
     *
     * @param graphics2D the graphics context used for rendering
     */
    public void renderContext(Graphics2D graphics2D) {
        paddle.render(graphics2D);
        ball.render(graphics2D);
        lifeCounter.render(graphics2D);
    }

    /** Serializes the current state of all game objects into JSON. */
    public void serializeGameContext() {
        paddle.serializeToJson();
        ball.serializeToJson();
        lifeCounter.serializeToJson();
    }

    /**
     * Restores all game objects from a previously serialized {@code GameContext}.
     *
     * @param gameContext the context to deserialize from
     */
    public void deserializeGameContext(GameContext gameContext) {
        paddle = gameContext.getPaddle();
        ball = gameContext.getBall();
        lifeCounter = gameContext.getLifeCounter();

        paddle.deserializeFromJson();
        ball.deserializeFromJson();
        lifeCounter.deserializeFromJson();

        ball.setPaddle(paddle);
    }

    /** Resets the bounds of paddle and ball to their initial positions. */
    public void resetObjectsBound() {
        paddle.resetPaddleBound();
        ball.resetBallBound(paddle);
    }

    /** Clears existing game object references. */
    private void refresh() {
        paddle = null;
        ball = null;
        lifeCounter = null;
    }

    /**
     * Sets the window width and automatically recalculates padding and scale.
     *
     * @param windowWidth the width of the window
     */
    public void setWindowWidth(int windowWidth) {
        this.windowWidth = windowWidth;
        paddingX = windowWidth / 100;
        scaled = windowWidth / 1000f;
    }

    /**
     * Sets the window height and recalculates vertical padding.
     *
     * @param windowHeight the height of the window
     */
    public void setWindowHeight(int windowHeight) {
        this.windowHeight = windowHeight;
        paddingY = windowHeight / 100;
    }

    /**
     * Checks whether the game is over (i.e., the player has no remaining lives).
     *
     * @return {@code true} if all lives are lost, otherwise {@code false}
     */
    public boolean isGameOver() {
        return lifeCounter.isOutOfLives();
    }

    // --- Getters and Setters ---
    public int getWindowWidth() {
        return windowWidth;
    }
    public int getWindowHeight() {
        return windowHeight;
    }
    public int getPaddingX() {
        return paddingX;
    }
    public int getPaddingY() {
        return paddingY;
    }
    public float getScaled() {
        return scaled;
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

    public void setPaddle(Paddle paddle) {
        this.paddle = paddle;
    }
    public void setBall(Ball ball) {
        this.ball = ball;
    }
    public void setLifeCounter(LifeCounter lifeCounter) {
        this.lifeCounter = lifeCounter;
    }
}
