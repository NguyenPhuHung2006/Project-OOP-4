package object;

import main.GameContext;

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

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public abstract void move();
}
