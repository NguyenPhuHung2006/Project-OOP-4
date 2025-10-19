package object.UI;

import input.MouseManager;
import object.GameObject;
import object.TexturedObject;

public class GameButton extends TexturedObject {

    public GameButton(TexturedObject texturedObject) {
        super(texturedObject);
    }

    @Override
    public void update() {

    }

    public boolean isClicked(MouseManager mouseManager) {
        int clickedX = mouseManager.getClickX();
        int clickedY = mouseManager.getClickY();
        return clickedX > x && clickedY > y && clickedX < x + width && clickedY < y + height;
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
