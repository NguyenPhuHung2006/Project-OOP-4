package object.movable.powerup;

import object.movable.Ball;

/**
 * A {@link PowerUp} that temporarily reduces the ball’s movement speed.
 * <p>
 * This allows the player more time to react to the ball’s trajectory.
 * </p>
 */
public class SlowBallPowerUp extends PowerUp {

    private final float slowMultiplier;

    public SlowBallPowerUp(PowerUp powerUp) {

        super(powerUp);

        SlowBallPowerUp slowBallPowerUp = (SlowBallPowerUp) powerUp;
        this.slowMultiplier = slowBallPowerUp.slowMultiplier;
    }

    /**
     * Scales the ball’s speed by a slow multiplier.
     * Has no effect if the ball’s speed is already scaled.
     */
    @Override
    public void applyEffect() {

        Ball ball = gameContext.getBall();

        if(ball.isSpeedScaled()) {
            return;
        }

        float originalSpeed = ball.getSpeed();
        float scaledSpeed = originalSpeed * slowMultiplier;
        ball.setScaledSpeed(scaledSpeed);
        ball.setSpeedScaled(true);
    }

    /**
     * Restores the ball’s original speed when the power-up expires.
     */
    @Override
    public void revertEffect() {
        Ball ball = gameContext.getBall();
        ball.setSpeedScaled(false);
    }

    @Override
    public SlowBallPowerUp clone() {
        return (SlowBallPowerUp) super.clone();
    }
}
