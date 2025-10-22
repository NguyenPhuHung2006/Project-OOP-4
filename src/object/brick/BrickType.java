package object.brick;

public enum BrickType {
    NORMAL_BRICK {
        @Override
        public Brick create(Brick brick) {
            return new NormalBrick(brick);
        }
    },
    STRONG_BRICK {
        @Override
        public Brick create(Brick brick) {
            return new StrongBrick(brick);
        }
    },
    POWERUP_BRICK {
        @Override
        public Brick create(Brick brick) {
            return new PowerUpBrick(brick);
        }
    };

    public abstract Brick create(Brick baseBrick);

}
