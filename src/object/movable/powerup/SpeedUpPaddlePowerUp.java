package object.movable.powerup;

import object.movable.Paddle;

/**
 * A {@link PowerUp} that temporarily increases the paddle’s movement speed.
 * <p>
 * Useful for making the paddle more responsive, though harder to control precisely.
 * </p>
 */
public class SpeedUpPaddlePowerUp extends PowerUp {

    private final float speedUpMultiplier;

    public SpeedUpPaddlePowerUp(PowerUp powerUp) {

        super(powerUp);

        SpeedUpPaddlePowerUp speedUpPaddlePowerUp = (SpeedUpPaddlePowerUp) powerUp;
        this.speedUpMultiplier = speedUpPaddlePowerUp.speedUpMultiplier;
    }

    /**
     * Increases the paddle’s speed by the defined multiplier.
     */
    @Override
    public void applyEffect() {
        Paddle paddle = gameContext.getPaddle();

        if(paddle.isSpeedScaled()) {
            return;
        }

        float originalSpeed = paddle.getSpeed();
        float scaledSpeed = originalSpeed * speedUpMultiplier;
        paddle.setScaledSpeed(scaledSpeed);
        paddle.setSpeedScaled(true);
    }

    /**
     * Restores the paddle’s original speed when the power-up expires.
     */
    @Override
    public void revertEffect() {
        Paddle paddle = gameContext.getPaddle();
        paddle.setSpeedScaled(false);
    }
}
