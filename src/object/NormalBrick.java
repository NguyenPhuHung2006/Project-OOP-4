package object;

import audio.SoundEffect;
import audio.SoundManager;

public class NormalBrick extends Brick {

    public NormalBrick(Brick brick) {
        super(brick);
    }

    @Override
    protected void handleHit() {
        SoundManager.getInstance().play(SoundEffect.NORMAL_BRICK);
        if (!frames.isEmpty()) {
            frames.removeLast();
            currentTexture = frames.isEmpty() ? null : frames.getLast();
        }

        if (frames.isEmpty()) {
            destroyed = true;
        }
    }

    @Override
    public void update() {
        hit = false;
    }
}
