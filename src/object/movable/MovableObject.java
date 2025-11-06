package object.movable;

import audio.SoundType;
import object.GameObject;
import object.TexturedObject;

/**
 * Represents an abstract game object that can move within the game world.
 * Provides basic movement, collision handling, and speed scaling logic.
 * <p>
 * Classes extending this must implement {@link #moveAndCollide()} to define
 * custom movement and collision behavior.
 */
public abstract class MovableObject extends TexturedObject {

    protected float dx;
    protected float dy;
    protected float speed;

    protected float scaledSpeed;
    protected boolean isSpeedScaled;

    public void setDx(int dx) {
        this.dx = dx;
    }

    public void setDy(int dy) {
        this.dy = dy;
    }

    /**
     * Copy constructor for creating a new MovableObject based on another instance.
     *
     * @param movableObject the object to copy data from
     */
    protected MovableObject(MovableObject movableObject) {
        super(movableObject);
        this.speed = movableObject.speed;
        dx = 0;
        dy = 0;
    }

    /**
     * Handles horizontal collision with another {@link GameObject}.
     *
     * @param gameObject the object to check collision against
     */
    protected void handleObjectCollisionX(GameObject gameObject) {

        if (gameObject == null) {
            return;
        }

        if (isIntersect(gameObject)) {
            if (dx > 0) {
                x = gameObject.getX() - width;
            } else {
                x = gameObject.getX() + gameObject.getWidth();
            }
            dx *= -1;
        }
    }

    /**
     * Handles vertical collision with another {@link GameObject}.
     *
     * @param gameObject the object to check collision against
     */
    protected void handleObjectCollisionY(GameObject gameObject) {

        if (gameObject == null) {
            return;
        }

        if (isIntersect(gameObject)) {
            if (dy > 0) {
                y = gameObject.getY() - height;
            } else {
                y = gameObject.getY() + gameObject.getHeight();
            }
            dy *= -1;
        }
    }

    /**
     * Handles collision with the game window boundaries.
     * Plays sound effects if applicable (for example, when bouncing a ball).
     */
    protected void handleWindowCollision() {

        boolean isBall = this instanceof Ball;

        if (x < 0 || x + width > windowWidth) {
            if (x < 0) {
                x = 0;
            } else {
                x = windowWidth - width;
            }
            if (isBall) {
                soundManager.play(SoundType.WINDOW_WALL);
            }
            dx *= -1;
        }

        if (y < 0 || y + height > windowHeight) {
            if (y < 0) {
                y = 0;
            } else {
                y = windowHeight - height;
            }
            if (isBall) {
                soundManager.play(SoundType.WINDOW_WALL);
            }
            dy *= -1;
        }
    }

    /**
     * Defines how this object moves and handles collisions.
     * Must be implemented by subclasses.
     */
    protected abstract void moveAndCollide();

    @Override
    public MovableObject clone() {
        return (MovableObject) super.clone();
    }

    public float getSpeed() {
        return speed;
    }

    public float getDx() { return dx; }

    public float getDy() {
        return dy;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public void setDx(float dx) {
        this.dx = dx;
    }

    public void setDy(float dy) {
        this.dy = dy;
    }

    protected void moveX() {
        x += dx * (isSpeedScaled ? scaledSpeed : speed) * scaled;
    }

    protected void moveY() {
        y += dy * (isSpeedScaled ? scaledSpeed : speed) * scaled;
    }

    public float getScaledSpeed() {
        return scaledSpeed;
    }

    public void setScaledSpeed(float scaledSpeed) {
        this.scaledSpeed = scaledSpeed;
    }

    public boolean isSpeedScaled() {
        return isSpeedScaled;
    }

    public void setSpeedScaled(boolean isSpeedScaled) {
        this.isSpeedScaled = isSpeedScaled;
    }

}
