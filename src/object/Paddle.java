package object;

import main.GameManager;
import java.awt.event.KeyEvent;
import input.KeyboardManager;
import provider.BoundProvider;
import provider.GameContext;
import provider.InputProvider;

public class Paddle extends MovableObject {
    public Paddle(ObjectConstant objectConstant, int speed) {
        super(objectConstant, speed);
    }

    @Override
    public void move(GameContext gameContext) {
        boolean leftKeyPressed = gameContext.getInput().isLeftPressed();
        boolean rightKeyPressed = gameContext.getInput().isRightPressed();
        int windowWidth = gameContext.getBounds().getWidth();

        // if both left and right key pressed or none of them is pressed, don't move
        if ((leftKeyPressed && rightKeyPressed) || (!leftKeyPressed && !rightKeyPressed)) {
            dx = 0;
        } else if (leftKeyPressed) {
            dx = -1;
        } else {
            dx = 1;
        }

        x += dx * speed;

        if (x < 0) {
            x = 0;
        }
        if (x + width > windowWidth) {
            x = windowWidth - width;
        }
    }

    @Override
    public void update(GameContext gameContext) {
        move(gameContext);
    }
}
