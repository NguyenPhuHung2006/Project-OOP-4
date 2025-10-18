package object.UI;

import object.GameObject;
import object.TexturedObject;

public class Button extends TexturedObject {

    public Button(TexturedObject texturedObject) {
        super(texturedObject);
    }

    @Override
    public void update() {

    }

    @Override
    protected void initBounds(GameObject gameObject) {

        initTextureBounds(gameObject);

        x = gameObject.getX();
        y = gameObject.getY();
        width = gameObject.getWidth();
        height = gameObject.getHeight();
    }
}
