package object.brick;

/**
 * Represents different types of bricks in the game.
 * Each type can create its own concrete {@link Brick} implementation.
 */
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

    /**
     * Creates a new instance of this brick type.
     *
     * @param baseBrick a template brick with shared properties
     * @return a new {@link Brick} instance of the corresponding type
     */
    public abstract Brick create(Brick baseBrick);

}
