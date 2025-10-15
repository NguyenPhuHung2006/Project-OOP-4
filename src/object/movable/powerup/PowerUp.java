package object.movable.powerup;

import exception.ExceptionHandler;
import exception.InvalidGameStateException;
import object.GameObject;
import object.brick.Brick;
import object.movable.MovableObject;
import utils.IntersectUtils;

public class PowerUp extends MovableObject {

    public PowerUp(PowerUp powerUp) {
        super(powerUp);
    }

    private boolean isActive = false;
    private boolean isFalling = false;
    private boolean isRemoved = false;
    PowerUpType powerUpType;

    @Override
    public void update() {
        if (isFalling) {
            moveAndCollide();
        }
        if (isActive) {
            applyPowerUpToObject();
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

        boolean isIntersectWithPaddle = IntersectUtils.intersect(this, gameContext.getPaddle());
        boolean isOutOfBound = y + height >= gameContext.getWindowHeight();
        if (isIntersectWithPaddle || isOutOfBound) {
            isFalling = false;
            isRemoved = true;
            if (isIntersectWithPaddle) {
                isActive = true;
                powerUpManager.applyPowerUp(powerUpType, this);
            }
        }
    }

    private void applyPowerUpToObject() {

    }

    public void setPowerUpType(PowerUpType powerUpType) {
        this.powerUpType = powerUpType;
    }

    public boolean isRemoved() {
        return isRemoved;
    }
}
