package object;

import java.awt.image.BufferedImage;

public class StrongBrickFactory extends BrickFactory {

    @Override
    public Brick createBrick(ObjectConstant objectConstant) {
        return new StrongBrick(objectConstant);
    }
}
