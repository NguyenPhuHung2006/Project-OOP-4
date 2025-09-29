package object;

import provider.GameContext;

import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class GameObject {

    protected int x;
    protected int y;
    protected int width;
    protected int height;
    protected BufferedImage texture;

    public GameObject(ObjectConstant objectConstant) {
        this.x = objectConstant.getX();
        this.y = objectConstant.getY();
        this.width = objectConstant.getWidth();
        this.height = objectConstant.getHeight();
        this.texture = objectConstant.getTexture();
    }

    public void render(Graphics2D graphics2D) {
        if (texture != null && graphics2D != null) {
            graphics2D.drawImage(texture, x, y, null);
        }
    }

    public void render(Graphics2D graphics2D, int scaledWidth, int scaledHeight) {
        if (texture != null && graphics2D != null) {
            graphics2D.drawImage(texture, x, y, scaledWidth, scaledHeight, null);
        }
    }

    public abstract void update(GameContext gameContext);

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public BufferedImage getTexture() {
        return texture;
    }
}
