package object;

import java.awt.image.BufferedImage;

public class NormalBrickFactory extends BrickFactory {

    @Override
    public Brick createBrick(Brick brick) {
        return new NormalBrick(brick);
    }
}
