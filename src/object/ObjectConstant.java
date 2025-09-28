package object;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public enum ObjectConstant {

    PADDLE("/assets/paddle.png",
            0, 0, 96, 24,
            100, 100, 200, 50);

    private final String path;
    private final int textureX;
    private final int textureY;
    private final int textureWidth;
    private final int textureHeight;
    private final BufferedImage texture;

    private final int x;
    private final int y;
    private final int width;
    private final int height;

    private BufferedImage loadTexture() {
        try {
            BufferedImage fullImage = ImageIO.read(ObjectConstant.class.getResource(path));
            if (fullImage == null) {
                return null;
            }
            return fullImage.getSubimage(textureX, textureY, textureWidth, textureHeight);

        } catch (IOException | IllegalArgumentException e) {
            e.printStackTrace();
            return null;
        }
    }

    private BufferedImage scaleTexture() {

        BufferedImage originalTexture = loadTexture();

        if (originalTexture == null) {
            return null;
        }

        BufferedImage scaledTexture = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics2D = scaledTexture.createGraphics();

        graphics2D.drawImage(originalTexture.getScaledInstance(width, height, Image.SCALE_SMOOTH), 0, 0, null);
        graphics2D.dispose();

        return scaledTexture;
    }

    ObjectConstant(String path,
                   int textureX, int textureY, int textureWidth, int textureHeight,
                   int x, int y, int width, int height) {
        this.path = path;
        this.textureX = textureX;
        this.textureY = textureY;
        this.textureWidth = textureWidth;
        this.textureHeight = textureHeight;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.texture = scaleTexture();
    }

    public String getPath() {
        return path;
    }

    public BufferedImage getTexture() {
        return texture;
    }

    public int getTextureX() {
        return textureX;
    }

    public int getTextureY() {
        return textureY;
    }

    public int getTextureWidth() {
        return textureWidth;
    }

    public int getTextureHeight() {
        return textureHeight;
    }

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

}
