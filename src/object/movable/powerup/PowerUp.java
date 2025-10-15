package object.movable.powerup;

import exception.ExceptionHandler;
import exception.InvalidGameStateException;
import object.GameObject;
import object.brick.Brick;
import object.movable.MovableObject;

public abstract class PowerUp extends MovableObject {

    private boolean isActive = false;
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
        if(isActive) {
            powerUpManager.applyPowerUp(powerUpType, this);
        }
    }

    @Override
    protected void initBounds(GameObject gameObject) {
        initTextureBounds(gameObject);

        int brickHeight = brickManager.getBrickHeight();
        int brickWidth = brickManager.getBrickWidth();

        if (brickHeight == 0 || brickWidth == 0) {
            ExceptionHandler.handle(new InvalidGameStateException(
                    "the bricks should be initialized before the power up or the brick size is not valid", null));
        }

        width = 0.5f * brickWidth;
        height = (width * textureHeight) / textureWidth;
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
                isActive = true;
            }
        }
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
