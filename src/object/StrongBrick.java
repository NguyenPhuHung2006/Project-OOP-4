package object;

import audio.SoundEffect;
import audio.SoundManager;

public class StrongBrick extends Brick{

    public StrongBrick(Brick brick) {
        super(brick);
    }

    @Override
    protected void handleHit() {
        soundManager.play(SoundEffect.STRONG_BRICK);
    }

    @Override
    public void update() {

    }

    @Override
    protected void initScreenBounds(GameObject gameObject) {
        x = gameObject.getX();
        y = gameObject.getY();
        width = gameObject.getWidth();
        height = gameObject.getHeight();
    }
}
