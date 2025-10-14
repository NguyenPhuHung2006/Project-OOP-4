package object;

import audio.SoundManager;
import exception.ExceptionHandler;
import exception.InvalidGameStateException;
import input.KeyboardManager;
import object.UI.Text.TextManager;
import object.brick.BrickManager;
import utils.RendererUtils;
import utils.TextureLoaderUtils;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public abstract class GameObject implements Cloneable {

    private final String texturePath;
    private int textureX;
    private int textureY;
    private final int textureWidth;
    private final int textureHeight;
    protected transient BufferedImage currentTexture;

    protected transient GameContext gameContext = GameContext.getInstance();
    protected transient KeyboardManager keyboardManager = KeyboardManager.getInstance();
    protected transient BrickManager brickManager = BrickManager.getInstance();
    protected transient SoundManager soundManager = SoundManager.getInstance();
    protected transient TextManager textManager = TextManager.getInstance();

    protected final int numberOfFrames;
    protected transient List<BufferedImage> frames;

    public List<BufferedImage> getFrames() {
        return frames;
    }

    protected transient float x;
    protected transient float y;
    protected transient float width;
    protected transient float height;

    public abstract void update();

    protected abstract void initScreenBounds(GameObject gameObject);

    public GameObject(GameObject gameObject) {

        this.textureX = gameObject.textureX;
        this.textureY = gameObject.textureY;
        this.textureWidth = gameObject.textureWidth;
        this.textureHeight = gameObject.textureHeight;
        this.texturePath = gameObject.texturePath;

        this.numberOfFrames = gameObject.numberOfFrames;

        initScreenBounds(gameObject);

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

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
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
