package object.movable.powerup;

import object.movable.Ball;

public class SlowBallPowerUp extends PowerUp {

    private final float slowMultiplier;
    private final float originalSpeed;
    private final float slowedSpeed;

    public SlowBallPowerUp(PowerUp powerUp) {

        super(powerUp);

        SlowBallPowerUp slowBallPowerUp = (SlowBallPowerUp) powerUp;

        this.slowMultiplier = slowBallPowerUp.slowMultiplier;
        Ball ball = gameContext.getBall();
        this.originalSpeed = ball.getOriginSpeed();
        this.slowedSpeed = originalSpeed * slowMultiplier;
    }

    @Override
    public void applyEffect() {

        Ball ball = gameContext.getBall();
        float currentSpeed = ball.getSpeed();

        if(currentSpeed >= slowedSpeed) {
            ball.setSpeed(slowedSpeed);
        }
    }

    @Override
    public void revertEffect() {

        Ball ball = gameContext.getBall();
        ball.setSpeed(originalSpeed);
    }

    @Override
    public SlowBallPowerUp clone() {
        return (SlowBallPowerUp) super.clone();
    }
}
