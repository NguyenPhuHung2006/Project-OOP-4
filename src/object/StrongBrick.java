package object;

public class StrongBrick extends Brick{

    public StrongBrick(Brick brick) {
        super(brick);
    }

    @Override
    public void update() {

    }

    @Override
    public StrongBrick clone() {
        return (StrongBrick) super.clone();
    }
}
