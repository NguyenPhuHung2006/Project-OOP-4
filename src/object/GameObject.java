package object;

import java.awt.image.BufferedImage;

public abstract class GameObject {

    protected int x;
    protected int y;
    protected int width;
    protected int height;
    protected BufferedImage texture;

    GameObject(int x, int y, int width, int height, BufferedImage texture) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.texture = texture;
    }

    public abstract void update();

    public abstract void render(java.awt.Graphics graphics);
}
