package object.UI;


import object.GameObject;
import object.TexturedObject;

/**
 * Represents the game's background layer.
 * <p>
 * This class handles the background texture that fills the entire
 * game window. It extends {@link TexturedObject} and ensures that
 * the texture scales to the window's dimensions.
 */
public class Background extends TexturedObject {

    /**
     * Constructs a {@code Background} instance by copying data
     * from another {@code Background} object.
     *
     * @param background the background object to copy
     */
    public Background(Background background) {

        super(background);
    }

    /**
     * Updates the background state.
     * <p>
     * Currently does nothing, as the background is static.
     */
    @Override
    public void update() {

    }

    /**
     * Initializes the texture bounds of the background so that it
     * fills the entire window.
     *
     * @param gameObject the base game object used for initialization
     */
    @Override
    protected void initBounds(GameObject gameObject) {

        initTextureBounds(gameObject);

        x = 0;
        y = 0;
        width = gameContext.getWindowWidth();
        height = gameContext.getWindowHeight();
        relativeSize = 1;
    }

}
