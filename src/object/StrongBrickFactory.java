package object;

import java.awt.image.BufferedImage;

public class StrongBrickFactory extends BrickFactory {

    @Override
    public Brick createBrick(Brick brick) {
        return new StrongBrick(brick);
    }
}
