package object.movable.powerup;

import object.movable.Ball;

public class SlowBallPowerUp extends PowerUp {

    private final float slowMultiplier;

    public SlowBallPowerUp(PowerUp powerUp) {

        super(powerUp);

        SlowBallPowerUp slowBallPowerUp = (SlowBallPowerUp) powerUp;
        this.slowMultiplier = slowBallPowerUp.slowMultiplier;
    }

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
