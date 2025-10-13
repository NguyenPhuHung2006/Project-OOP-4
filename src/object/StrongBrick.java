package object;

import audio.SoundType;

public class StrongBrick extends Brick{

    public StrongBrick(Brick brick) {
        super(brick);
    }

    @Override
    protected void handleHit() {
        hit = false;
        soundManager.play(SoundType.STRONG_BRICK);
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
