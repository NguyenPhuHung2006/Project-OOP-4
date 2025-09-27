package object;

import java.awt.image.BufferedImage;

public class StrongBrickFactory extends BrickFactory {

    @Override
    public Brick createBrick(int x, int y, int width, int height, BufferedImage texture) {
        return new StrongBrick(x, y, width, height, texture);
    }
}
