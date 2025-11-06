package object.brick;

import audio.SoundType;

/**
 * Represents a normal brick that is destroyed after one or more hits,
 * depending on its animation frame.
 */
public class NormalBrick extends Brick {

    public NormalBrick(Brick brick) {
        super(brick);
    }

    @Override
    protected void handleHit() {

        hit = false;
        soundManager.play(SoundType.NORMAL_BRICK);

        indexFrame += brickManager.getDestroyInterval();

        if(indexFrame < frames.size()) {
            currentTexture = frames.get(indexFrame);
        } else {
            destroyed = true;
        }

    }

    @Override
    public void update() {
        if(hit) {
            handleHit();
        }
    }
}
