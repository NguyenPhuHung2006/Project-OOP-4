package object.movable;

import audio.SoundType;
import object.GameObject;
import object.TexturedObject;

public abstract class MovableObject extends TexturedObject {

    protected float dx;
    protected float dy;
    protected float speed;

    public void setDx(int dx) {
        this.dx = dx;
    }

    public void setDy(int dy) {
        this.dy = dy;
    }

    protected MovableObject(MovableObject movableObject) {
        super(movableObject);
        this.speed = movableObject.speed;
        dx = 0;
        dy = 0;
    }

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

    protected abstract void moveAndCollide();

    protected void moveX() {
        x += dx * speed;
    }

    protected void moveY() {
        y += dy * speed;
    }
}
