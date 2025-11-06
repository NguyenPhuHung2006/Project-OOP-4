package object;

import exception.ExceptionHandler;
import exception.InvalidGameStateException;
import utils.RendererUtils;
import utils.TextureLoaderUtils;

import java.util.ArrayList;
import java.util.List;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * The {@code TexturedObject} class extends {@link GameObject} and represents
 * an object that uses textures or sprite frames for visual rendering.
 * <p>
 * This class handles texture loading, scaling, and animated frame updates.
 */

public abstract class TexturedObject extends GameObject {

    private final String texturePath;
    protected int textureX = 0;
    protected int textureY;
    protected int textureWidth;
    protected int textureHeight;
    protected transient BufferedImage currentTexture;

    protected int indexFrame = 0;

    protected final int numberOfFrames;
    protected transient List<BufferedImage> frames;

    /**
     * Constructs a new {@code TexturedObject} by copying properties from another instance.
     *
     * @param texturedObject the object to copy
     */
    public TexturedObject(TexturedObject texturedObject) {

        super(texturedObject);

        initBounds(texturedObject);

        this.texturePath = texturedObject.texturePath;
        this.numberOfFrames = texturedObject.numberOfFrames;

        loadFrames();
    }

    /** Loads and scales all texture frames for rendering. */
    private void loadFrames() {

        frames = new ArrayList<>();

        for (int i = 0; i < numberOfFrames; i++) {
            frames.add(TextureLoaderUtils.scaleTexture(
                    textureX + i * textureWidth,
                    textureY,
                    textureWidth,
                    textureHeight,
                    texturePath,
                    width,
                    height));
        }
        if (indexFrame >= frames.size() || indexFrame < 0) {
            ExceptionHandler.handle(new InvalidGameStateException(
                    "Invalid frame index: " + indexFrame, null));
        }
        currentTexture = frames.get(indexFrame);
    }

    @Override
    public void serializeToJson() {

        relativeX = x / windowWidth;
        relativeY = y / windowHeight;
    }

    @Override
    public void deserializeFromJson() {

        x = relativeX * windowWidth;
        y = relativeY * windowHeight;

        applyRelativeSize();
        loadFrames();
    }

    /**
     * Copies texture bounds (sprite sheet coordinates) from another {@code GameObject}.
     *
     * @param gameObject the source object
     */
    protected void initTextureBounds(GameObject gameObject) {
        TexturedObject texturedObject = (TexturedObject) gameObject;

        textureX = texturedObject.getTextureX();
        textureY = texturedObject.getTextureY();
        textureHeight = texturedObject.getTextureHeight();
        textureWidth = texturedObject.getTextureWidth();
    }

    /** Calculates actual display size based on relative scaling. */
    public void applyRelativeSize() {

        this.width = windowWidth * relativeSize;
        this.height = textureHeight * width / textureWidth;

        this.originalWidth = width;
        this.originalHeight = height;
    }

    @Override
    public void render(Graphics2D graphics2D) {
        if(isSizeScaled) {
            RendererUtils.render(currentTexture, x, y, (int)width, (int)height, graphics2D);
        } else {
            RendererUtils.render(currentTexture, x, y, graphics2D);
        }
    }

    @Override
    public TexturedObject clone() {
        return (TexturedObject) super.clone();
    }

    // --- Getters and setters ---

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

    public boolean isSizeScaled() {
        return isSizeScaled;
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

    public int getNumberOfFrames() {
        return numberOfFrames;
    }

}
