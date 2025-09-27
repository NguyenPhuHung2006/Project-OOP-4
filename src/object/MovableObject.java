package object;

import java.awt.*;
import java.awt.image.BufferedImage;

public class MovableObject extends GameObject {

    private int dx;
    private int dy;
    private int speed;

    MovableObject(int x, int y, int width, int height, BufferedImage texture, int speed) {
        super(x, y, width, height, texture);
        this.speed = speed;
    }

    @Override
    public void update() {

    }

    @Override
    public void render(Graphics2D graphics) {

    }
}
