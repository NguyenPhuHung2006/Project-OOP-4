package object.movable.powerup;

import object.GameObject;
import object.TexturedObject;
import object.brick.Brick;
import object.movable.MovableObject;

/**
 * Base class for all power-up types.
 * <p>
 * A {@code PowerUp} is a movable object that falls from destroyed bricks.
 * When it collides with the paddle, it applies a temporary game effect,
 * such as slowing the ball or expanding the paddle.
 * </p>
 * <p>
 * Subclasses define specific effects and their reversions
 * (via {@link #applyEffect()} and {@link #revertEffect()}).
 * </p>
 */
public abstract class PowerUp extends MovableObject {

    private boolean isFalling = false;
    private boolean isRemoved = false;
    protected int durationMs;
    private PowerUpType powerUpType;

    public PowerUp(PowerUp powerUp) {

        super(powerUp);

        durationMs = powerUp.durationMs;
    }

    /**
     * Applies the specific effect of this power-up to the game state.
     * The implementation is defined by subclasses.
     */
    public abstract void applyEffect();

    /**
     * Reverts the effect of this power-up, restoring the game state
     * to its original condition.
     */
    public abstract void revertEffect();

    @Override
    public void update() {
        if (isFalling) {
            moveAndCollide();
        }
    }

    @Override
    protected void initBounds(GameObject gameObject) {
        initTextureBounds(gameObject);

        TexturedObject texturedObject = (TexturedObject) gameObject;

        texturedObject.applyRelativeSize();

        this.width = gameObject.getWidth();
        this.height = gameObject.getHeight();

    }

    /**
     * Sets the initial position of this power-up to fall from the specified brick.
     *
     * @param brick the brick from which the power-up originates
     */
    public void setInitialPosition(Brick brick) {
        x = brick.getX() + (brick.getWidth() - width) / 2;
        y = brick.getY();
        dx = 0;
        dy = 1;
        isFalling = true;
    }

    /**
     * Updates the falling movement of this power-up and checks for collisions
     * with the paddle or screen boundaries.
     */
    @Override
    protected void moveAndCollide() {
        moveY();
        handleCollide();
    }

    /**
     * Checks for collision with the paddle or falling out of bounds.
     * If collected, applies the power-up effect; if missed, removes it.
     */
    private void handleCollide() {

        boolean isIntersectWithPaddle = isIntersect(gameContext.getPaddle());
        boolean isOutOfBound = y + height >= gameContext.getWindowHeight();
        if (isIntersectWithPaddle || isOutOfBound) {
            isFalling = false;
            isRemoved = true;
            if (isIntersectWithPaddle) {
                powerUpManager.applyPowerUp(powerUpType, this);
            }
        }
    }

    @Override
    public PowerUp clone() {
        return (PowerUp) super.clone();
    }

    public void setPowerUpType(PowerUpType powerUpType) {
        this.powerUpType = powerUpType;
    }

    public void setDurationMs(int durationMs) {
        this.durationMs = durationMs;
    }

    public boolean isRemoved() {
        return isRemoved;
    }

    public int getDurationMs() {
        return durationMs;
    }
}
