package object.movable.powerup;

import object.movable.Ball;

public class SlowBallPowerUp extends PowerUp {

    private final float slowMultiplier;
    private final float originalSpeed;
    private final float slowedSpeed;
    private final Ball ball;

    public SlowBallPowerUp(SlowBallPowerUp slowBallPowerUp) {

        super(slowBallPowerUp);

        this.slowMultiplier = slowBallPowerUp.slowMultiplier;
        this.ball = gameContext.getBall();
        this.originalSpeed = ball.getOriginSpeed();
        this.slowedSpeed = originalSpeed * slowMultiplier;
    }

    @Override
    public void applyEffect() {

        float currentSpeed = ball.getSpeed();

        if(currentSpeed == slowedSpeed) {
            return;
        }

        ball.setSpeed(originalSpeed * slowMultiplier);
    }

    @Override
    public void revertEffect() {

        if(ball.getSpeed() != originalSpeed) {
            return;
        }

        ball.setSpeed(originalSpeed);
    }

    @Override
    public SlowBallPowerUp clone() {
        return (SlowBallPowerUp) super.clone();
    }
}
