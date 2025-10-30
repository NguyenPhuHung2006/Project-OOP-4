package object;

import exception.ExceptionHandler;
import exception.InvalidGameStateException;
import utils.RendererUtils;
import utils.TextureLoaderUtils;

import java.util.LinkedList;
import java.util.Queue;

import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class TexturedObject extends GameObject {

    private final String texturePath;
    protected int textureX = 0;
    protected int textureY;
    protected int textureWidth;
    protected int textureHeight;
    protected transient BufferedImage currentTexture;

    protected int frameX = 0;

    protected final int numberOfFrames;
    protected transient Queue<BufferedImage> frames;

    protected boolean isScaled;

    public Queue<BufferedImage> getFrames() {
        return frames;
    }

    public TexturedObject(TexturedObject texturedObject) {

        super(texturedObject);

        initBounds(texturedObject);

        this.texturePath = texturedObject.texturePath;
        this.numberOfFrames = texturedObject.numberOfFrames;

        loadFrames();
    }

    private void loadFrames() {
        frames = new LinkedList<>();
        for (int i = frameX; i < numberOfFrames; i++) {
            frames.add(TextureLoaderUtils.scaleTexture(textureX + i * textureWidth, textureY, textureWidth, textureHeight,
                    texturePath, width, height));
        }
        if (frames.isEmpty()) {
            ExceptionHandler.handle(new InvalidGameStateException("The number of frames " + numberOfFrames + " is not valid", null));
        }
        currentTexture = frames.peek();
    }

    protected void initTextureBounds(GameObject gameObject) {
        TexturedObject texturedObject = (TexturedObject) gameObject;

        textureX = texturedObject.getTextureX();
        textureY = texturedObject.getTextureY();
        textureHeight = texturedObject.getTextureHeight();
        textureWidth = texturedObject.getTextureWidth();
    }

    public void applyRelativeSize() {

        this.width = windowWidth * relativeSize;
        this.height = textureHeight * width / textureWidth;
    }

    @Override
    public void render(Graphics2D graphics2D) {
        if(isScaled) {
            RendererUtils.render(currentTexture, x, y, (int)width, (int)height, graphics2D);
        } else {
            RendererUtils.render(currentTexture, x, y, graphics2D);
        }
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

    public boolean isScaled() {
        return isScaled;
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

    public void setScaled(boolean isScaled) {
        this.isScaled = isScaled;
    }
}
