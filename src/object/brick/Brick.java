package object.brick;

import object.GameObject;
import object.TexturedObject;

public abstract class Brick extends TexturedObject {

    private int brickTypeId;
    protected boolean destroyed;
    protected boolean hit;

    public Brick(Brick brick) {

        super(brick);
        this.brickTypeId = brick.brickTypeId;
        destroyed = false;
        hit = false;
    }

    protected abstract void handleHit();

    @Override
    public Brick clone() {

        return (Brick) super.clone();
    }

    @Override
    protected void initScreenBounds(GameObject gameObject) {

        initTextureBounds(gameObject);
        x = gameObject.getX();
        y = gameObject.getY();
        width = gameObject.getWidth();
        height = gameObject.getHeight();
    }

    public int getBrickTypeId() {
        return brickTypeId;
    }

    public boolean isDestroyed() {
        return destroyed;
    }

    public boolean isHit() { return hit; }

    public void setBrickTypeId(int brickTypeId) {
        this.brickTypeId = brickTypeId;
    }

    public void takeHit() { hit = true; }

    public void setDestroyed(boolean destroyed) {
        this.destroyed = destroyed;
    }
}
