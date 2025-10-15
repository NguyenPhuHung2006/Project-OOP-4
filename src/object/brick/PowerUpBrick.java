package object.brick;

import audio.SoundType;
import object.movable.powerup.PowerUpType;

public class PowerUpBrick extends Brick {

    public PowerUpBrick(Brick brick) {
        super(brick);
    }

    @Override
    protected void handleHit() {
        hit = false;
        soundManager.play(SoundType.POWERUP_BRICK);
        destroyed = true;

        powerUpManager.addPowerUp(PowerUpType.SLOW_BALL, this);
    }

    @Override
    public void update() {
        if(hit) {
            handleHit();
        }
    }
}
