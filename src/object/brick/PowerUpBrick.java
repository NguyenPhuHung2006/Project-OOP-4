package object.brick;

import audio.SoundType;
import object.GameObject;

public class PowerUpBrick extends Brick{

    public PowerUpBrick(Brick brick) {
        super(brick);
    }

    @Override
    protected void handleHit() {
        hit = false;
        soundManager.play(SoundType.POWERUP_BRICK);
        destroyed = true;
    }

    @Override
    public void update() {
        if(hit) {
            handleHit();
        }
    }

    @Override
    protected void initScreenBounds(GameObject gameObject) {
        x = gameObject.getX();
        y = gameObject.getY();
        width = gameObject.getWidth();
        height = gameObject.getHeight();
    }
}
