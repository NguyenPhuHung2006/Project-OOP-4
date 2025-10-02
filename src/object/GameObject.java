package object;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.awt.image.BufferedImage;

import exception.ExceptionHandler;
import exception.InvalidGameStateException;
import utils.RendererUtils;
import utils.TextureLoaderUtils;

public abstract class GameObject {

    private final String texturePath;
    private final int textureX;
    private final int textureY;
    private final int textureWidth;
    private final int textureHeight;

    private final int numberOfFrames;
    private transient List<BufferedImage> frames;
    private final int endFrameOffset;

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

        this.numberOfFrames = gameObject.numberOfFrames;
        this.endFrameOffset = gameObject.endFrameOffset;

        loadFrames();
    }

    private void loadFrames() {

        frames = new ArrayList<>();
        for(int i = numberOfFrames - 1; i >= 0; i--) {
            frames.add(TextureLoaderUtils.scaleTexture(textureX + i * textureWidth, textureY, textureWidth, textureHeight,
                    texturePath, width, height));
        }
    }

    public void render(Graphics2D graphics2D) throws InvalidGameStateException {
        if(frames.isEmpty()) {
            throw new InvalidGameStateException("Tried to render with no frames loaded", null);
        }
        RendererUtils.render(frames.getLast(), x, y, graphics2D);
    }

    public void render(Graphics2D graphics2D, int scaledWidth, int scaledHeight) throws InvalidGameStateException {
        if(frames.isEmpty()) {
            throw new InvalidGameStateException("Tried to render with no frames loaded", null);
        }
        RendererUtils.render(frames.getLast(), x, y, scaledWidth, scaledHeight, graphics2D);
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
        return frames.getLast();
    }

    public String getTexturePath() {
        return texturePath;
    }
}
