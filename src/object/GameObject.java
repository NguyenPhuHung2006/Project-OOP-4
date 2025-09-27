package object;

import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class GameObject {

    protected int x;
    protected int y;
    protected int width;
    protected int height;
    protected BufferedImage texture;

    private static BufferedImage scaleTexture(BufferedImage originalTexture, int width, int height) {
        if (originalTexture == null) {
            return null;
        }

        BufferedImage scaledTexture = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics2D = scaledTexture.createGraphics();

        graphics2D.drawImage(originalTexture.getScaledInstance(width, height, Image.SCALE_SMOOTH), 0, 0, null);
        graphics2D.dispose();

        return scaledTexture;
    }

    public GameObject(int x, int y, int width, int height, BufferedImage texture) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.texture = scaleTexture(texture, width, height);
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

    public abstract void update();
}
