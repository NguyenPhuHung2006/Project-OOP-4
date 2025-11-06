package object.brick;

import audio.SoundType;

/**
 * Represents a strong brick that only plays a sound when hit
 * and is never destroyed.
 */
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
}
