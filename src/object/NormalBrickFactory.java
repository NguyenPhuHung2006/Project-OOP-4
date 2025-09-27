package object;

import java.awt.image.BufferedImage;

public class NormalBrickFactory extends BrickFactory {

    @Override
    public Brick createBrick(int x, int y, int width, int height, BufferedImage texture) {
        return new NormalBrick(x, y, width, height, texture);
    }
}
