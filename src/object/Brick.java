package object;

import java.awt.image.BufferedImage;

public abstract class Brick extends GameObject {

    private int brickTypeId;
<<<<<<< Updated upstream
    protected boolean destroyed;
    protected boolean hit;
=======
    private boolean destroyed = false;
>>>>>>> Stashed changes

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

<<<<<<< Updated upstream
    public void takeHit() { hit = true; }
=======
    public boolean isDestroyed() {
        return destroyed;
    }

    public void setDestroyed(boolean destroyed) {
        this.destroyed = destroyed;
    }
>>>>>>> Stashed changes
}
