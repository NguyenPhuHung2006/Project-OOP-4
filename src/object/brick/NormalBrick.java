package object.brick;

import audio.SoundType;

public class NormalBrick extends Brick {

    public NormalBrick(Brick brick) {
        super(brick);
    }

    @Override
    protected void handleHit() {

        hit = false;
        soundManager.play(SoundType.NORMAL_BRICK);

        indexFrame++;

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
