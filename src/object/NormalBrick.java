package object;

import audio.SoundEffect;

public class NormalBrick extends Brick {

    public NormalBrick(Brick brick) {
        super(brick);
    }

    @Override
    protected void handleHit() {
        soundManager.play(SoundEffect.NORMAL_BRICK);
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

    @Override
    protected void initScreenBounds(GameObject gameObject) {
        x = gameObject.getX();
        y = gameObject.getY();
        width = gameObject.getWidth();
        height = gameObject.getHeight();
    }
}
