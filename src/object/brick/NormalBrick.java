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
        if (!frames.isEmpty()) {
            frames.poll();
            currentTexture = frames.isEmpty() ? null : frames.peek();
            frameX += textureWidth;
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
}
