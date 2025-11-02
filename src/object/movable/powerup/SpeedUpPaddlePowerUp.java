package object.movable.powerup;

import object.movable.Paddle;

public class SpeedUpPaddlePowerUp extends PowerUp {

    private final float speedUpMultiplier;
    private final float originalSpeed;
    private final float speedUpSpeed;

    public SpeedUpPaddlePowerUp(PowerUp powerUp) {

        super(powerUp);

        SpeedUpPaddlePowerUp speedUpPaddlePowerUp = (SpeedUpPaddlePowerUp) powerUp;

        this.speedUpMultiplier = speedUpPaddlePowerUp.speedUpMultiplier;

        Paddle paddle = gameContext.getPaddle();
        this.originalSpeed = paddle.getOriginalSpeed();
        this.speedUpSpeed = this.originalSpeed * speedUpMultiplier;
    }

    @Override
    public void applyEffect() {
        Paddle paddle = gameContext.getPaddle();
        float currentSpeed = paddle.getSpeed();
        if(currentSpeed <= speedUpSpeed) {
            paddle.setSpeed(speedUpSpeed);
        }
    }

    @Override
    public void revertEffect() {
        Paddle paddle = gameContext.getPaddle();
        paddle.setSpeed(originalSpeed);
    }
}
