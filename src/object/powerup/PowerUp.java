package object.powerup;

import object.GameObject;
import object.TexturedObject;

public class PowerUp extends TexturedObject {

    public PowerUp(PowerUp powerUp) {
        super(powerUp);
    }

    @Override
    public void update() {

    }

    @Override
    protected void initScreenBounds(GameObject gameObject) {
        initTextureBounds(gameObject);
    }
}
