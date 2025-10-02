package object;

public class NormalBrick extends Brick {

    public NormalBrick(Brick brick) {
        super(brick);
    }

    @Override
    public void update() {

    }

    @Override
    public NormalBrick clone() {
        return (NormalBrick) super.clone();
    }
}
