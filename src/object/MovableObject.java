package object;

import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class MovableObject extends GameObject {

    private int dx;
    private int dy;
    private int speed;

    MovableObject(int x, int y, int width, int height, int speed, BufferedImage texture) {
        super(x, y, width, height, texture);
        this.speed = speed;
        dx = 0;
        dy = 0;
    }

    public abstract void move();
}
