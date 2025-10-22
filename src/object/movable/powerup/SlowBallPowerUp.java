package object.movable.powerup;

import object.movable.Ball;

public class SlowBallPowerUp extends PowerUp {

    private final float slowMultiplier;
    private float originalSpeed;

    public SlowBallPowerUp(SlowBallPowerUp slowBallPowerUp) {

        super(slowBallPowerUp);
        this.slowMultiplier = slowBallPowerUp.slowMultiplier;
    }

    @Override
    public void applyEffect() {
        Ball ball = gameContext.getBall();
        originalSpeed = ball.getSpeed();
        ball.setSpeed(originalSpeed * slowMultiplier);
    }

    @Override
    public void revertEffect() {
        gameContext.getBall().setSpeed(originalSpeed);
    }

    @Override
    public SlowBallPowerUp clone() {
        return (SlowBallPowerUp) super.clone();
    }
}
