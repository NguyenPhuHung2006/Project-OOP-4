package object.movable.powerup;

import config.LevelData;
import exception.ExceptionHandler;
import exception.InvalidGameStateException;
import object.brick.Brick;

import java.awt.*;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class PowerUpManager {
    private static PowerUpManager powerUpManager;
    private final Map<PowerUpType, PowerUp> powerUpsRegistry = new EnumMap<>(PowerUpType.class);

    private List<PowerUp> activePowerUps = new ArrayList<>();

    private PowerUpManager() {
    }

    public static PowerUpManager getInstance() {
        if (powerUpManager == null) {
            powerUpManager = new PowerUpManager();
        }
        return powerUpManager;
    }

    private void loadPowerUp(PowerUpType powerUpType, PowerUp powerUp) {
        if(powerUpsRegistry.containsKey(powerUpType)) {
            ExceptionHandler.handle(new InvalidGameStateException("the power type " + powerUpType + " is loaded twice", null));
        }
        powerUpsRegistry.put(powerUpType, new PowerUp(powerUp));
    }

    public void addPowerUps(PowerUpType powerUpType, Brick brick) {

        PowerUp newPowerUp = (PowerUp) powerUpsRegistry.get(powerUpType).clone();
        newPowerUp.setInitialPosition(brick);
        activePowerUps.add(newPowerUp);
    }

    public void updatePowerUps() {
        for(PowerUp activePowerUp : activePowerUps) {
            activePowerUp.update();
        }
    }

    public void renderPowerUps(Graphics2D graphics2D) {
        for(PowerUp activePowerUp : activePowerUps) {
            activePowerUp.render(graphics2D);
        }
    }

    public PowerUp getPowerUp(PowerUpType powerUpType) {
        return powerUpsRegistry.get(powerUpType);
    }

    public void loadFromLevel(LevelData levelData) {
        loadPowerUp(PowerUpType.SLOW, levelData.slowPowerUp);
    }
}
