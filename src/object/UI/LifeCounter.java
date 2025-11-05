package object.UI;

import object.GameObject;
import object.TexturedObject;

public class LifeCounter extends TexturedObject {

    private int lives;
    private final int totalLives;

    private final int liveInterval;

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

    public void updateLives(boolean isIncreasing) {
        if(isIncreasing) {
            lives++;
            lives = Math.min(lives, totalLives);
        } else {
            lives -= liveInterval;
        }
        updateFrames();
    }

    private void updateFrames() {

        if (lives <= totalLives && lives >= 1) {
            indexFrame = totalLives - lives;
            currentTexture = frames.get(indexFrame);
        }
    }

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
