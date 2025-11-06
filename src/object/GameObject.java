package object;

import audio.SoundManager;
import input.KeyboardManager;
import object.brick.BrickManager;
import object.movable.powerup.PowerUpManager;

import java.awt.*;

/**
 * The {@code GameObject} class defines the base structure and behavior
 * of all renderable and updatable objects in the game.
 * <p>
 * It provides core properties like position, dimensions, alignment,
 * collision detection, and references to shared managers.
 * Subclasses must implement {@link #update()}, {@link #render(Graphics2D)},
 * and {@link #initBounds(GameObject)}.
 */

public abstract class GameObject implements Cloneable {

    protected float x;
    protected float y;
    protected float width;
    protected float height;

    protected float relativeSize;
    protected float relativeX;
    protected float relativeY;

    protected static GameContext gameContext;
    protected static int windowWidth;
    protected static int windowHeight;
    protected static int paddingX;
    protected static int paddingY;
    protected static float scaled;

    protected float originalWidth;
    protected float originalHeight;
    protected boolean isSizeScaled;

    protected static KeyboardManager keyboardManager;
    protected static BrickManager brickManager;
    protected static SoundManager soundManager;
    protected static PowerUpManager powerUpManager;

    static {
        updateStatics();
    }

    /**
     * Initializes static references for commonly used managers and global context values.
     * <p>
     * This method should be called whenever:
     * <ul>
     *   <li>the game window or global context is refreshed, or</li>
     *   <li>an object is deserialized from JSON.</li>
     * </ul>
     */
    public static void updateStatics() {
        gameContext = GameContext.getInstance();
        windowWidth = gameContext.getWindowWidth();
        windowHeight = gameContext.getWindowHeight();
        paddingX = gameContext.getPaddingX();
        paddingY = gameContext.getPaddingY();
        scaled = gameContext.getScaled();

        keyboardManager = KeyboardManager.getInstance();
        brickManager = BrickManager.getInstance();
        soundManager = SoundManager.getInstance();
        powerUpManager = PowerUpManager.getInstance();
    }

    /**
     * Constructs a new {@code GameObject} by copying size-related data from another object.
     *
     * @param gameObject the object to copy data from
     */
    public GameObject(GameObject gameObject) {
        relativeSize = gameObject.getRelativeSize();
    }

    /** Updates the object logic each frame. */
    public abstract void update();

    /**
     * Renders the object on the screen.
     *
     * @param graphics2D the graphics context
     */
    public abstract void render(Graphics2D graphics2D);

    /**
     * Initializes the bounding box and dimensions of this object based on an object loaded from JSON.
     *
     * @param gameObject the reference object
     */
    protected abstract void initBounds(GameObject gameObject);

    /**
     * Checks if this object intersects another.
     *
     * @param otherObject the object to check collision with
     * @return {@code true} if the bounding boxes overlap, otherwise {@code false}
     */
    public boolean isIntersect(GameObject otherObject) {
        float x1 = getX();
        float y1 = getY();
        float w1 = getWidth();
        float h1 = getHeight();

        float x2 = otherObject.getX();
        float y2 = otherObject.getY();
        float w2 = otherObject.getWidth();
        float h2 = otherObject.getHeight();

        return x1 < x2 + w2 &&
                x1 + w1 > x2 &&
                y1 < y2 + h2 &&
                y1 + h1 > y2;
    }

    /** Serializes this object’s relative position and properties to JSON. */
    public abstract void serializeToJson();

    /** Restores this object’s properties from serialized JSON data. */
    public abstract void deserializeFromJson();

    // --- Alignment and positioning utilities ---
    public void alignLeftOf(GameObject target) {
        this.x = target.x - this.width - paddingX;
        this.y = target.y;
    }

    public void alignRightOf(GameObject target) {
        this.x = target.x + target.width + paddingX;
        this.y = target.y;
    }

    public void alignAbove(GameObject target) {
        this.y = target.y - this.height - paddingY;
        this.x = target.x;
    }

    public void alignBelow(GameObject target) {
        this.y = target.y + target.height + paddingY;
        this.x = target.x;
    }

    public void centerHorizontally() {
        this.x = (windowWidth - this.width) / 2f;
    }

    public void centerVertically() {
        this.y = (windowHeight - this.height) / 2f;
    }

    public void centerHorizontallyTo(GameObject target) {
        this.x = target.x + (target.width - this.width) / 2f;
    }

    public void centerVerticallyTo(GameObject target) {
        this.y = target.y + (target.height - this.height) / 2f;
    }

    public void center() {

        centerHorizontally();
        centerVertically();
    }

    public void applyRelativePositionX() {
        this.x = windowWidth * relativeX;
    }

    public void applyRelativePositionY() {
        this.y = windowHeight * relativeY;
    }

    public void applyRelativePosition() {

        applyRelativePositionX();
        applyRelativePositionY();
    }

    public void alignTopLeft() {
        x = paddingX;
        y = paddingY;
    }

    public void alignTopRight() {
        x = windowWidth - width - paddingX;
        y = paddingY;
    }

    public void alignBottomLeft() {
        x = paddingX;
        y = windowHeight - height - paddingY;
    }

    public void alignBottomRight() {
        x = windowWidth - width - paddingX;
        y = windowHeight - height - paddingY;
    }

    public void alignTop() {
        y = paddingY;
    }

    public void alignBottom() {
        y = windowHeight - height - paddingY;
    }

    public void alignLeft() {
        x = paddingX;
    }

    public void alignRight() {
        x = windowWidth - width - paddingX;
    }

    public void translateX(float deltaX) {
        this.x += deltaX;
    }

    public void translateY(float deltaY) {
        this.y += deltaY;
    }


    @Override
    public GameObject clone() {
        try {
            return (GameObject) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    // --- Getters and setters ---

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

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public float getRelativeSize() {
        return relativeSize;
    }

    public void setRelativeSize(float relativeSize) {
        this.relativeSize = relativeSize;
    }

    public float getRelativeX() {
        return relativeX;
    }

    public void setRelativeX(float relativeX) {
        this.relativeX = relativeX;
    }

    public float getRelativeY() {
        return relativeY;
    }

    public void setRelativeY(float relativeY) {
        this.relativeY = relativeY;
    }

    public void setSizeScaled(boolean isScaled) {
        this.isSizeScaled = isScaled;
    }

    public float getOriginalWidth() {
        return originalWidth;
    }

    public void setOriginalWidth(float originalWidth) {
        this.originalWidth = originalWidth;
    }

    public float getOriginalHeight() {
        return originalHeight;
    }

    public void setOriginalHeight(float originalHeight) {
        this.originalHeight = originalHeight;
    }

}
