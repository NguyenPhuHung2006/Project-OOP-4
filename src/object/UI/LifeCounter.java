package object.UI;

import object.GameObject;
import object.TexturedObject;

/**
 * Displays and manages the player's remaining lives.
 * <p>
 * The {@code LifeCounter} tracks the number of lives left,
 * updates visual frames accordingly, and provides logic for
 * increasing or decreasing the life count.
 */
public class LifeCounter extends TexturedObject {

    private int lives;
    private final int totalLives;

    private final int liveInterval;

    /**
     * Constructs a {@code LifeCounter} from another textured object.
     *
     * @param texturedObject the source textured object to copy
     */
    public LifeCounter(TexturedObject texturedObject) {

        super(texturedObject);
        lives = texturedObject.getNumberOfFrames();
        totalLives = texturedObject.getNumberOfFrames();

        LifeCounter lifeCounter = (LifeCounter) texturedObject;
        liveInterval = lifeCounter.liveInterval;
    }

    @Override
    public void update() {

    }

    /**
     * Initializes the bounds of this life counter based on a reference object.
     *
     * @param gameObject the object used for position and size initialization
     */
    @Override
    protected void initBounds(GameObject gameObject) {

        initTextureBounds(gameObject);

        TexturedObject texturedObject = (TexturedObject) gameObject;
        
        texturedObject.applyRelativeSize();
        texturedObject.alignBottomRight();

        x = texturedObject.getX();
        y = texturedObject.getY();
        width = texturedObject.getWidth();
        height = texturedObject.getHeight();
    }

    /**
     * Updates the number of lives and adjusts the display frame.
     *
     * @param isIncreasing {@code true} to add lives, {@code false} to decrease
     */
    public void updateLives(boolean isIncreasing) {
        if(isIncreasing) {
            lives++;
            lives = Math.min(lives, totalLives);
        } else {
            lives -= liveInterval;
        }
        updateFrames();
    }

    /**
     * Updates the texture frame based on the current number of lives.
     */
    private void updateFrames() {

        if (lives <= totalLives && lives >= 1) {
            indexFrame = totalLives - lives;
            currentTexture = frames.get(indexFrame);
        }
    }

    /**
     * Checks whether the player has no remaining lives.
     *
     * @return {@code true} if lives are zero or less
     */
    public boolean isOutOfLives() {
        return lives <= 0;
    }

    public int getLives() {
        return lives;
    }

    public void setLives(int lives) {
        this.lives = lives;
    }

    public int getLiveInterval() {
        return liveInterval;
    }
}
