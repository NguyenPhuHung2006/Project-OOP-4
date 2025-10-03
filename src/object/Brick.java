package object;

import java.awt.image.BufferedImage;

public abstract class Brick extends GameObject {

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
}
