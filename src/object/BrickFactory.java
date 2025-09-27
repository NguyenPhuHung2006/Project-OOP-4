package object;

import java.awt.image.BufferedImage;

public abstract class BrickFactory {
    public abstract Brick createBrick(int x, int y, int width, int height, BufferedImage texture);
}
