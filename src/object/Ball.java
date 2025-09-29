package object;

import main.GameManager;
import provider.GameContext;

import java.awt.image.BufferedImage;

public class Ball extends MovableObject {

    public Ball(ObjectConstant objectConstant, int speed) {

        super(objectConstant, speed);
    }

    @Override
    public void move(GameContext gameContext) {
        int windowWidth = gameContext.getBounds().getWidth();
        int windowHeight = gameContext.getBounds().getHeight();

        x += dx * speed;
        y += dy * speed;

        int radius = width / 2;
        int centerX = x + radius;
        int centerY = y + radius;



        if (x < 0) {
            x = 0;
        }
        if (y < 0) {
            y = 0;
        }
        if (x + width > windowWidth) {
            x = windowWidth - width;
        }
        if (y + height > windowHeight) {
            GameManager.getInstance().stopGame();
        }

    }

    @Override
    public void update(GameContext gameContext) {
        move(gameContext);
    }
}
