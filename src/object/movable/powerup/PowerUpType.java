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
            return new ExpandPaddleWidthPowerUp(powerUp);
        }
    },
    SPEEDUP_PADDLE {
        @Override
        public PowerUp create(PowerUp powerUp) { return new SpeedUpPaddlePowerUp(powerUp); }
    };

    public abstract PowerUp create(PowerUp powerUp);
}
