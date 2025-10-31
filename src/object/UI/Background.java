package object.UI;


import object.GameObject;
import object.TexturedObject;

/**
 * This class will load Background
 */
public class Background extends TexturedObject {

    public Background(Background background) {

        super(background);
    }

    @Override
    public void update() {

    }

    @Override
    protected void initBounds(GameObject gameObject) {

        initTextureBounds(gameObject);

        x = 0;
        y = 0;
        width = gameContext.getWindowWidth();
        height = gameContext.getWindowHeight();
    }

}
