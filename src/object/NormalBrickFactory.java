package object;

import java.awt.image.BufferedImage;

public class NormalBrickFactory extends BrickFactory {

    @Override
    public Brick createBrick(ObjectConstant objectConstant) {
        return new NormalBrick(objectConstant);
    }
}
