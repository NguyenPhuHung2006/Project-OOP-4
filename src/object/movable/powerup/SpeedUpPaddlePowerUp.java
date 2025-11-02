package object.movable.powerup;

import object.movable.Paddle;

public class SpeedUpPaddlePowerUp extends PowerUp {

    private final float speedUpMultiplier;

    public SpeedUpPaddlePowerUp(PowerUp powerUp) {

        super(powerUp);

        SpeedUpPaddlePowerUp speedUpPaddlePowerUp = (SpeedUpPaddlePowerUp) powerUp;
        this.speedUpMultiplier = speedUpPaddlePowerUp.speedUpMultiplier;
    }

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

    @Override
    public void revertEffect() {
        Paddle paddle = gameContext.getPaddle();
        paddle.setSpeedScaled(false);
    }
}
