package object.movable.powerup;

import object.GameObject;
import object.TexturedObject;
import object.brick.Brick;
import object.movable.MovableObject;

public abstract class PowerUp extends MovableObject {

    private boolean isFalling = false;
    private boolean isRemoved = false;
    protected int durationMs;
    private PowerUpType powerUpType;

    public PowerUp(PowerUp powerUp) {

        super(powerUp);

        durationMs = powerUp.durationMs;
    }

    public abstract void applyEffect();
    public abstract void revertEffect();

    @Override
    public void update() {
        if (isFalling) {
            moveAndCollide();
        }
    }

    @Override
    protected void initBounds(GameObject gameObject) {
        initTextureBounds(gameObject);

        TexturedObject texturedObject = (TexturedObject) gameObject;

        texturedObject.applyRelativeSize();

        this.width = gameObject.getWidth();
        this.height = gameObject.getHeight();

    }

    public void setInitialPosition(Brick brick) {
        x = brick.getX() + (brick.getWidth() - width) / 2;
        y = brick.getY();
        dx = 0;
        dy = 1;
        isFalling = true;
    }

    @Override
    protected void moveAndCollide() {
        moveY();
        handleCollide();
    }

    private void handleCollide() {

        boolean isIntersectWithPaddle = isIntersect(gameContext.getPaddle());
        boolean isOutOfBound = y + height >= gameContext.getWindowHeight();
        if (isIntersectWithPaddle || isOutOfBound) {
            isFalling = false;
            isRemoved = true;
            if (isIntersectWithPaddle) {
                powerUpManager.applyPowerUp(powerUpType, this);
            }
        }
    }

    @Override
    public PowerUp clone() {
        return (PowerUp) super.clone();
    }

    public void setPowerUpType(PowerUpType powerUpType) {
        this.powerUpType = powerUpType;
    }

    public void setDurationMs(int durationMs) {
        this.durationMs = durationMs;
    }

    public boolean isRemoved() {
        return isRemoved;
    }

    public int getDurationMs() {
        return durationMs;
    }
}
