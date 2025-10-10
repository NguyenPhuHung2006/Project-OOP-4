package object;

public class StrongBrick extends Brick{

    public StrongBrick(Brick brick) {
        super(brick);
    }

    @Override
    protected void handleHit() {
        SoundManager.getInstance().play(SoundEffect.STRONG_BRICK);
    }

    @Override
    public void update() {

    }
}
