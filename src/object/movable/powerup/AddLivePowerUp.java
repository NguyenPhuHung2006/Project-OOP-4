package object.movable.powerup;

import object.UI.LifeCounter;

/**
 * A {@link PowerUp} that grants the player one extra life.
 * <p>
 * This effect is instantaneous and does not expire.
 * </p>
 */
public class AddLivePowerUp extends PowerUp {
    public AddLivePowerUp(PowerUp powerUp) {
        super(powerUp);
    }

    /**
     * Adds one life to the player's life counter.
     */
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
