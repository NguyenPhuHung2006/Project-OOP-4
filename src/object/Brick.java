package object;

import java.awt.image.BufferedImage;

public abstract class Brick extends GameObject {

    public Brick(Brick brick) {

        super(brick);
    }
}
