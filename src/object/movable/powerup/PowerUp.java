package object.movable.powerup;

import exception.ExceptionHandler;
import exception.InvalidGameStateException;
import object.GameObject;
import object.TexturedObject;
import object.brick.Brick;
import object.movable.MovableObject;

public class PowerUp extends MovableObject {

    public PowerUp(PowerUp powerUp) {
        super(powerUp);
    }

    @Override
    public void update() {
        moveAndCollide();
    }

    @Override
    protected void initBounds(GameObject gameObject) {
        initTextureBounds(gameObject);

        int brickHeight = brickManager.getBrickHeight();
        int brickWidth = brickManager.getBrickWidth();

        if(brickHeight == 0 || brickWidth == 0) {
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
    }

    @Override
    protected void moveAndCollide() {
        moveY();
    }
}
