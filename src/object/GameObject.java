package object;

import java.awt.*;
import java.awt.image.BufferedImage;
import utils.TextureLoaderUtils;

public abstract class GameObject {

    protected String texturePath;
    protected int textureX;
    protected int textureY;
    protected int textureWidth;
    protected int textureHeight;
    protected transient BufferedImage texture;

    protected int x;
    protected int y;
    protected int width;
    protected int height;

    public GameObject(GameObject gameObject) {

        this.x = gameObject.x;
        this.y = gameObject.y;
        this.width = gameObject.width;
        this.height = gameObject.height;

        this.textureX = gameObject.textureX;
        this.textureY = gameObject.textureY;
        this.textureWidth = gameObject.textureWidth;
        this.textureHeight = gameObject.textureHeight;
        this.texturePath = gameObject.texturePath;
        this.texture = TextureLoaderUtils.scaleTexture(textureX, textureY, textureWidth, textureHeight, texturePath, width, height);
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
