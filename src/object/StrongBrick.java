package object;

public class StrongBrick extends Brick{

    public StrongBrick(Brick brick) {
        super(brick);
    }

    @Override
    protected void handleHit() {

    }

    @Override
    public void update() {

    }

    @Override
    public StrongBrick clone() {
        return (StrongBrick) super.clone();
    }
}
