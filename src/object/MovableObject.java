package object;

import main.GameContext;
import utils.IntersectUtils;

public abstract class MovableObject extends GameObject {

    protected int dx;
    protected int dy;
    protected int speed;

    MovableObject(MovableObject movableObject) {
        super(movableObject);
        this.speed = movableObject.speed;
        dx = 0;
        dy = 0;
    }

    @Override
    public MovableObject clone() {
        return (MovableObject) super.clone();
    }

    public int getSpeed() {
        return speed;
    }

    public int getDx() {
        return dx;
    }

    public int getDy() {
        return dy;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }
    
    protected void moveAndCollideWithObject(GameObject gameObject) {
        moveX();
        handleObjectCollisionX(gameObject);
        moveY();
        handleObjectCollisionY(gameObject);
    }

    protected void moveX() {
        x += dx * speed;
    }
    
    protected void moveY() {
        y += dy * speed;
    }
    
    protected void handleObjectCollisionX(GameObject gameObject) {
        
        if (IntersectUtils.intersect(this, gameObject)) {
            if (dx > 0) {
                x = gameObject.getX() - width;
            } else {
                x = gameObject.getX() + gameObject.getWidth();
            }
            dx *= -1;
        }
    }
    
    protected void handleObjectCollisionY(GameObject gameObject) {
        
        if (IntersectUtils.intersect(this, gameObject)) {
            if (dy > 0) {
                y = gameObject.getY() - height;
            } else {
                y = gameObject.getY() + gameObject.getHeight();
            }
            dy *= -1;
        }
    }

    protected void handleWindowCollision() {

        GameContext gameContext = GameContext.getInstance();
        int windowWidth = gameContext.getWindowWidth();
        int windowHeight = gameContext.getWindowHeight();

        if (x < 0 || x + width > windowWidth) {
            if (x < 0) {
                x = 0;
            } else {
                x = windowWidth - width;
            }
            dx *= -1;
        }

        if (y < 0 || y + height > windowHeight) {
            if (y < 0) {
                y = 0;
            } else {
                y = windowHeight - height;
            }
            dy *= -1;
        }
    }
}
