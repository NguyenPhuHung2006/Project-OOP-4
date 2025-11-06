package object.movable.powerup;

/**
 * Enumeration of all available power-up types in the game.
 * <p>
 * Each enum constant provides a factory method to create an instance
 * of its respective {@link PowerUp} subclass.
 * </p>
 */
public enum PowerUpType {
    SLOW_BALL {
        @Override
        public PowerUp create(PowerUp powerUp) {
            return new SlowBallPowerUp(powerUp);
        }
    },
    EXPAND_PADDLE {
        @Override
        public PowerUp create(PowerUp powerUp) {
            return new ExpandPaddlePowerUp(powerUp);
        }
    },
    SPEEDUP_PADDLE {
        @Override
        public PowerUp create(PowerUp powerUp) { return new SpeedUpPaddlePowerUp(powerUp); }
    },
    ADD_LIVE {
        @Override
        public PowerUp create(PowerUp powerUp) { return new AddLivePowerUp(powerUp); }
    };

    /**
     * Creates a new {@link PowerUp} instance of this type based on a template.
     *
     * @param powerUp the base power-up used as a template
     * @return a newly created power-up of this type
     */
    public abstract PowerUp create(PowerUp powerUp);
}
