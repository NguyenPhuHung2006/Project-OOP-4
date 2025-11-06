package object.UI;

import input.MouseManager;
import object.GameObject;
import object.TexturedObject;

/**
 * Represents a clickable UI button within the game.
 * <p>
 * A {@code GameButton} detects mouse clicks based on its texture
 * bounds and interacts with the {@link MouseManager}.
 */
public class GameButton extends TexturedObject {

    /**
     * Constructs a {@code GameButton} by copying data
     * from a {@link TexturedObject}.
     *
     * @param texturedObject the textured object to copy
     */
    public GameButton(TexturedObject texturedObject) {
        super(texturedObject);
    }

    @Override
    public void update() {

    }

    /**
     * Checks if the button has been clicked.
     *
     * @param mouseManager the current mouse manager handling input
     * @return {@code true} if the button area was clicked; otherwise {@code false}
     */
    public boolean isClicked(MouseManager mouseManager) {
        int clickedX = mouseManager.getClickX();
        int clickedY = mouseManager.getClickY();
        return clickedX > x && clickedY > y && clickedX < x + width && clickedY < y + height;
    }

    /**
     * Initializes the button’s bounds based on the given game object.
     *
     * @param gameObject the object containing position and size data
     */
    @Override
    protected void initBounds(GameObject gameObject) {

        initTextureBounds(gameObject);

        x = gameObject.getX();
        y = gameObject.getY();
        width = gameObject.getWidth();
        height = gameObject.getHeight();
    }
}
