package object;

import provider.GameContext;

import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class MovableObject extends GameObject {

    protected int dx;
    protected int dy;
    protected int speed;

    MovableObject(ObjectConstant objectConstant, int speed) {
        super(objectConstant);
        this.speed = speed;
        dx = 0;
        dy = 0;
    }

    public abstract void move(GameContext gameContext);
}
