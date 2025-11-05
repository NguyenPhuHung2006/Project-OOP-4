package object.movable.powerup;

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

    public abstract PowerUp create(PowerUp powerUp);
}
