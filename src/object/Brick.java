package object;

import java.awt.image.BufferedImage;

public abstract class Brick extends GameObject {

    public Brick(int x, int y, int width, int height, BufferedImage texture) {
        super(x, y, width, height, texture);
    }
}
