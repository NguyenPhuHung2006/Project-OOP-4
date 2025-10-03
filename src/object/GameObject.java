package object;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.awt.image.BufferedImage;

import exception.ExceptionHandler;
import exception.InvalidGameStateException;
import utils.RendererUtils;
import utils.TextureLoaderUtils;

public abstract class GameObject implements Cloneable {

    private final String texturePath;
    private int textureX;
    private int textureY;
    private final int textureWidth;
    private final int textureHeight;
    protected transient BufferedImage currentTexture;

    protected final int numberOfFrames;
    protected transient List<BufferedImage> frames;

    public List<BufferedImage> getFrames() {
        return frames;
    }

    protected int x;
    protected int y;
    protected int width;
    protected int height;

    public abstract void update();

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

        try {
            loadFrames();
        } catch (InvalidGameStateException e) {
            ExceptionHandler.handle(e);
        }
    }

    private void loadFrames() throws InvalidGameStateException {

        frames = new ArrayList<>();
        for (int i = numberOfFrames - 1; i >= 0; i--) {
            frames.add(TextureLoaderUtils.scaleTexture(textureX + i * textureWidth, textureY, textureWidth, textureHeight,
                    texturePath, width, height));
        }
        if (frames.isEmpty()) {
            throw new InvalidGameStateException("The number of frames is not valid", null);
        }
        currentTexture = frames.getLast();
    }

    public void render(Graphics2D graphics2D) {
        RendererUtils.render(currentTexture, x, y, graphics2D);
    }

    public void render(Graphics2D graphics2D, int scaledWidth, int scaledHeight) {
        RendererUtils.render(currentTexture, x, y, scaledWidth, scaledHeight, graphics2D);
    }

    @Override
    public GameObject clone() {
        try {
            return (GameObject) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
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

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setTexture(BufferedImage texture) {
        this.currentTexture = texture;
    }

}
