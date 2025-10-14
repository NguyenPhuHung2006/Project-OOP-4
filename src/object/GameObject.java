package object;

import audio.SoundManager;
import input.KeyboardManager;
import object.brick.BrickManager;

import java.awt.*;

public abstract class GameObject implements Cloneable {

    protected transient float x;
    protected transient float y;
    protected transient float width;
    protected transient float height;

    protected transient GameContext gameContext = GameContext.getInstance();
    protected transient KeyboardManager keyboardManager = KeyboardManager.getInstance();
    protected transient BrickManager brickManager = BrickManager.getInstance();
    protected transient SoundManager soundManager = SoundManager.getInstance();

    public abstract void update();
    public abstract void render(Graphics2D graphics2D);
    protected abstract void initScreenBounds(GameObject gameObject);

    public GameObject(GameObject gameObject) {

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

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
