package object.brick;

import audio.SoundType;
import object.movable.powerup.PowerUpType;
import utils.RandomUtils;

public class PowerUpBrick extends Brick {

    public PowerUpBrick(Brick brick) {
        super(brick);
    }

    @Override
    protected void handleHit() {
        hit = false;
        soundManager.play(SoundType.POWERUP_BRICK);
        destroyed = true;

        powerUpManager.addPowerUp(RandomUtils.nextEnum(PowerUpType.class), this);
    }

    @Override
    public void update() {
        if(hit) {
            handleHit();
        }
    }
}
