package object;

import audio.SoundManager;
import input.KeyboardManager;
import object.brick.BrickManager;
import object.movable.powerup.PowerUpManager;

import java.awt.*;

public abstract class GameObject implements Cloneable {

    protected transient float x;
    protected transient float y;
    protected transient float width;
    protected transient float height;

    protected float relativeSize;
    protected float relativeX;
    protected float relativeY;

    protected transient GameContext gameContext = GameContext.getInstance();
    protected transient KeyboardManager keyboardManager = KeyboardManager.getInstance();
    protected transient BrickManager brickManager = BrickManager.getInstance();
    protected transient SoundManager soundManager = SoundManager.getInstance();
    protected transient PowerUpManager powerUpManager = PowerUpManager.getInstance();

    public abstract void update();

    public abstract void render(Graphics2D graphics2D);

    protected abstract void initBounds(GameObject gameObject);

    public GameObject(GameObject gameObject) {

    }

    public boolean isIntersect(GameObject otherObject) {
        float x1 = getX();
        float y1 = getY();
        float w1 = getWidth();
        float h1 = getHeight();

        float x2 = otherObject.getX();
        float y2 = otherObject.getY();
        float w2 = otherObject.getWidth();
        float h2 = otherObject.getHeight();

        return x1 < x2 + w2 &&
                x1 + w1 > x2 &&
                y1 < y2 + h2 &&
                y1 + h1 > y2;
    }

    @Override
    public GameObject clone() {
        try {
            return (GameObject) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public float getRelativeSize() {
        return relativeSize;
    }

    public void setRelativeSize(float relativeSize) {
        this.relativeSize = relativeSize;
    }

    public float getRelativeX() {
        return relativeX;
    }

    public void setRelativeX(float relativeX) {
        this.relativeX = relativeX;
    }

    public float getRelativeY() {
        return relativeY;
    }

    public void setRelativeY(float relativeY) {
        this.relativeY = relativeY;
    }

}
