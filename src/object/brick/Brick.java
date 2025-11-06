package object.brick;

import object.GameObject;
import object.TexturedObject;

/**
 * Represents an abstract brick object in the game.
 * <p>
 * A brick has a texture, can be hit or destroyed, and is rendered
 * as part of the game field. Concrete subclasses (e.g., {@link NormalBrick},
 * {@link StrongBrick}, {@link PowerUpBrick}) define specific hit behavior.
 */
public abstract class Brick extends TexturedObject {

    protected boolean destroyed;
    protected boolean hit;

    /**
     * Creates a new brick based on another brick's properties.
     *
     * @param brick the brick to copy from
     */
    public Brick(Brick brick) {

        super(brick);
        destroyed = false;
        hit = false;
    }

    /**
     * Handles logic that occurs when this brick is hit by the ball.
     * Each brick type implements this differently.
     */
    protected abstract void handleHit();

    @Override
    public Brick clone() {

        return (Brick) super.clone();
    }

    @Override
    protected void initBounds(GameObject gameObject) {

        initTextureBounds(gameObject);
        x = gameObject.getX();
        y = gameObject.getY();
        width = gameObject.getWidth();
        height = gameObject.getHeight();
    }

    public boolean isDestroyed() {
        return destroyed;
    }

    /** @return true if this brick was recently hit. */
    public boolean isHit() { return hit; }

    /** Marks this brick as hit, triggering its update logic. */
    public void takeHit() { hit = true; }

    public void setDestroyed(boolean destroyed) {
        this.destroyed = destroyed;
    }
}
