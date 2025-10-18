package object.brick;

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
}
