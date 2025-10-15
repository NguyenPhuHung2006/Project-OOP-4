package object.brick;

import object.GameObject;
import object.TexturedObject;

public abstract class Brick extends TexturedObject {

    protected boolean destroyed;
    protected boolean hit;
    private BrickType brickType;

    public Brick(Brick brick) {

        super(brick);
        destroyed = false;
        hit = false;
    }

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

    public boolean isHit() { return hit; }

    public void takeHit() { hit = true; }

    public void setDestroyed(boolean destroyed) {
        this.destroyed = destroyed;
    }

    public BrickType getBrickType() {
        return brickType;
    }
}
