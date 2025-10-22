package object.movable.powerup;

import object.movable.Ball;

public class SlowBallPowerUp extends PowerUp {

    private final float slowMultiplier;
    private float originalSpeed;
    private final Ball ball;

    public SlowBallPowerUp(SlowBallPowerUp slowBallPowerUp) {

        super(slowBallPowerUp);

        this.slowMultiplier = slowBallPowerUp.slowMultiplier;
        this.ball = gameContext.getBall();
    }

    @Override
    public void applyEffect() {
        originalSpeed = ball.getSpeed();
        ball.setSpeed(originalSpeed * slowMultiplier);
    }

    @Override
    public void revertEffect() {
        ball.setSpeed(originalSpeed);
    }

    @Override
    public SlowBallPowerUp clone() {
        return (SlowBallPowerUp) super.clone();
    }
}
