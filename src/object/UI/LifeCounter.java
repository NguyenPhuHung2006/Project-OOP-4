package object.UI;

import object.GameObject;
import object.TexturedObject;

public class LifeCounter extends TexturedObject {

    private int lives;
    private final int totalLives;

    public LifeCounter(TexturedObject texturedObject) {

        super(texturedObject);
        lives = texturedObject.getNumberOfFrames();
        totalLives = texturedObject.getNumberOfFrames();
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
        } else {
            lives--;
        }
        updateFrames();
    }

    private void updateFrames() {

        if (lives <= totalLives && lives >= 1) {
            currentTexture = frames.get(totalLives - lives);
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

}
