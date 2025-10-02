package object;

import java.awt.image.BufferedImage;

public abstract class Brick extends GameObject {

    private int brickTypeId;

    public Brick(Brick brick) {

        super(brick);
        this.brickTypeId = brick.brickTypeId;
    }

    @Override
    public Brick clone() {

        return (Brick) super.clone();
    }

    public int getBrickTypeId() {
        return brickTypeId;
    }

    public void setBrickTypeId(int brickTypeId) {
        this.brickTypeId = brickTypeId;
    }
}
