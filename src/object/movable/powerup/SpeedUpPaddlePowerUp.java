package object.movable.powerup;

import object.movable.Paddle;

public class SpeedUpPaddlePowerUp extends PowerUp {

    private final float speedUpMultiplier;
    private final float originalSpeed;
    private final float speedUpSpeed;
    private final Paddle paddle;

    public SpeedUpPaddlePowerUp(PowerUp powerUp) {

        super(powerUp);

        SpeedUpPaddlePowerUp speedUpPaddlePowerUp = (SpeedUpPaddlePowerUp) powerUp;

        this.speedUpMultiplier = speedUpPaddlePowerUp.speedUpMultiplier;
        this.paddle = gameContext.getPaddle();
        this.originalSpeed = paddle.getOriginalSpeed();
        this.speedUpSpeed = this.originalSpeed * speedUpMultiplier;
    }

    @Override
    public void applyEffect() {
        float currentSpeed = paddle.getSpeed();
        if(currentSpeed <= speedUpSpeed) {
            paddle.setSpeed(speedUpSpeed);
        }
        System.out.println(paddle.getSpeed());
    }

    @Override
    public void revertEffect() {
        paddle.setSpeed(originalSpeed);
    }
}
