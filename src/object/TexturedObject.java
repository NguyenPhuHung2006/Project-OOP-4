package object;

import exception.ExceptionHandler;
import exception.InvalidGameStateException;
import utils.RendererUtils;
import utils.TextureLoaderUtils;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public abstract class TexturedObject extends GameObject {

    private final String texturePath;
    protected int textureX = 0;
    protected int textureY;
    protected int textureWidth;
    protected int textureHeight;
    protected transient BufferedImage currentTexture;

    protected final int numberOfFrames;
    protected transient List<BufferedImage> frames;

    public List<BufferedImage> getFrames() {
        return frames;
    }

    public TexturedObject(TexturedObject texturedObject) {

        super(texturedObject);

        initScreenBounds(texturedObject);

        this.texturePath = texturedObject.texturePath;
        this.numberOfFrames = texturedObject.numberOfFrames;

        loadFrames();
    }

    private void loadFrames() {

        frames = new ArrayList<>();
        for (int i = numberOfFrames - 1; i >= 0; i--) {
            frames.add(TextureLoaderUtils.scaleTexture(textureX + i * textureWidth, textureY, textureWidth, textureHeight,
                    texturePath, width, height));
        }
        if (frames.isEmpty()) {
            ExceptionHandler.handle(new InvalidGameStateException("The number of frames is not valid", null));
        }
        currentTexture = frames.getLast();
    }

    protected void initTextureBounds(GameObject gameObject) {
        TexturedObject texturedObject = (TexturedObject) gameObject;

        textureX = texturedObject.getTextureX();
        textureY = texturedObject.getTextureY();
        textureHeight = texturedObject.getTextureHeight();
        textureWidth = texturedObject.getTextureWidth();
    }

    @Override
    public void render(Graphics2D graphics2D) {
        RendererUtils.render(currentTexture, x, y, graphics2D);
    }

    public void render(Graphics2D graphics2D, int scaledWidth, int scaledHeight) {
        RendererUtils.render(currentTexture, x, y, scaledWidth, scaledHeight, graphics2D);
    }

    @Override
    public TexturedObject clone() {
        return (TexturedObject) super.clone();
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

    public BufferedImage getTexture() {
        return currentTexture;
    }

    public String getTexturePath() {
        return texturePath;
    }

    public void setTextureX(int textureX) {
        this.textureX = textureX;
    }

    public void setTextureY(int textureY) {
        this.textureY = textureY;
    }

    public void setTextureWidth(int textureWidth) {
        this.textureWidth = textureWidth;
    }

    public void setTextureHeight(int textureHeight) {
        this.textureHeight = textureHeight;
    }

    public void setTexture(BufferedImage texture) {
        this.currentTexture = texture;
    }
}
