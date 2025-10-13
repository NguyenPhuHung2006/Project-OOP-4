package object;

import audio.SoundType;

public class NormalBrick extends Brick {

    public NormalBrick(Brick brick) {
        super(brick);
    }

    @Override
    protected void handleHit() {
        hit = false;
        soundManager.play(SoundType.NORMAL_BRICK);
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
