package object;

import java.awt.image.BufferedImage;

public class Ball extends MovableObject {

    public Ball(int x, int y, int radius, int speed, BufferedImage texture) {
        super(x, y, 2 * radius, 2 * radius, speed, texture);
    }

    @Override
    public void move() {

    }

    @Override
    public void update() {

    }
}
