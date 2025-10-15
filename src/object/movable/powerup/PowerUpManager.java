package object.movable.powerup;

import config.LevelData;
import exception.ExceptionHandler;
import exception.InvalidGameStateException;
import object.brick.Brick;

import java.awt.*;
import java.util.*;
import java.util.List;

public class PowerUpManager {
    private static PowerUpManager powerUpManager;
    private final Map<PowerUpType, PowerUp> powerUpsRegistry = new EnumMap<>(PowerUpType.class);
    private final Map<PowerUpType, PowerUp> activePowerUps = new EnumMap<>(PowerUpType.class);
    private final List<PowerUp> fallingPowerUps = new ArrayList<>();

    private PowerUpManager() {
    }

    public static PowerUpManager getInstance() {
        if (powerUpManager == null) {
            powerUpManager = new PowerUpManager();
        }
        return powerUpManager;
    }

    public void loadFromLevel(LevelData levelData) {

        refreshPowerUps();
        loadPowerUp(PowerUpType.SLOW, levelData.slowPowerUp);
    }

    private void loadPowerUp(PowerUpType powerUpType, PowerUp powerUp) {

        if (powerUpsRegistry.containsKey(powerUpType)) {
            ExceptionHandler.handle(new InvalidGameStateException("the power type " + powerUpType + " is loaded twice", null));
        }
        powerUpsRegistry.put(powerUpType, new PowerUp(powerUp));
    }

    public void addPowerUp(PowerUpType powerUpType, Brick brick) {

        PowerUp newPowerUp = (PowerUp) powerUpsRegistry.get(powerUpType).clone();
        newPowerUp.setInitialPosition(brick);
        newPowerUp.setPowerUpType(powerUpType);
        fallingPowerUps.add(newPowerUp);
    }

    public void applyPowerUp(PowerUpType powerUpType, PowerUp powerUp) {

        activePowerUps.put(powerUpType, powerUp);
    }

    public void updateActivePowerUps() {
        for (PowerUp activePowerUp : activePowerUps.values()) {
            if (activePowerUp != null) {
                activePowerUp.update();
            }
        }
    }

    public void updateFallingPowerUps() {
        for (PowerUp fallingPowerUp : fallingPowerUps) {
            fallingPowerUp.update();
        }
        fallingPowerUps.removeIf(PowerUp::isRemoved);
    }

    public void renderPowerUps(Graphics2D graphics2D) {
        for (PowerUp fallingPowerUp : fallingPowerUps) {
            fallingPowerUp.render(graphics2D);
        }
    }

    public void disableActivePowerUp(PowerUpType powerUpType) {
        activePowerUps.put(powerUpType, null);
    }

    private void refreshPowerUps() {

        activePowerUps.clear();
        powerUpsRegistry.clear();
        fallingPowerUps.clear();
    }
}
