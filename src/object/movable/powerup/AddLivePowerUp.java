package object.movable.powerup;

import object.UI.LifeCounter;

public class AddLivePowerUp extends PowerUp {
    public AddLivePowerUp(PowerUp powerUp) {
        super(powerUp);
    }

    @Override
    public void applyEffect() {
        LifeCounter lifeCounter = gameContext.getLifeCounter();
        lifeCounter.updateLives(true);
    }

    @Override
    public void revertEffect() {

    }

    @Override
    public AddLivePowerUp clone() {
        return (AddLivePowerUp) super.clone();
    }
}
