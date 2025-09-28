package object;

import java.awt.image.BufferedImage;

public abstract class BrickFactory {
    public abstract Brick createBrick(ObjectConstant objectConstant);
}
