package object;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

import main.GameContext;

import javax.imageio.ImageIO;

public abstract class GameObject {

    private String texturePath;
    private int textureX;
    private int textureY;
    private int textureWidth;
    private int textureHeight;
    protected transient BufferedImage texture;

    protected int x;
    protected int y;
    protected int width;
    protected int height;

    public GameObject(GameObject gameObject) {
        this.textureX = gameObject.textureX;
        this.textureY = gameObject.textureY;
        this.textureWidth = gameObject.textureWidth;
        this.textureHeight = gameObject.textureHeight;
        this.texturePath = gameObject.texturePath;

        this.x = gameObject.x;
        this.y = gameObject.y;
        this.width = gameObject.width;
        this.height = gameObject.height;

        this.texture = scaleTexture();
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

    private BufferedImage loadTexture() {
        try {
            BufferedImage fullImage = ImageIO.read(GameObject.class.getResource(texturePath));
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

    public abstract void update();
    
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

    public BufferedImage getTexture() {
        return texture;
    }

    public String getTexturePath() {
        return texturePath;
    }
}
