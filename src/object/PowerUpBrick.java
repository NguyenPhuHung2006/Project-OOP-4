package object;

public class PowerUpBrick extends Brick{

    public PowerUpBrick(Brick brick) {
        super(brick);
    }

    @Override
    protected void handleHit() {

    }

    @Override
    public void update() {

    }

    @Override
    protected void initScreenBounds(GameObject gameObject) {
        x = gameObject.getX();
        y = gameObject.getY();
        width = gameObject.getWidth();
        height = gameObject.getHeight();
    }
}
