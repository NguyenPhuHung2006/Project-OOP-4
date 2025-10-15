package object.powerup;

import config.LevelData;
import exception.ExceptionHandler;
import exception.InvalidGameStateException;

import java.util.EnumMap;
import java.util.Map;

public class PowerUpManager {
    private static PowerUpManager powerUpManager;
    private final Map<PowerUpType, PowerUp> powerUps = new EnumMap<>(PowerUpType.class);

    private PowerUpManager() {
    }

    public static PowerUpManager getInstance() {
        if (powerUpManager == null) {
            powerUpManager = new PowerUpManager();
        }
        return powerUpManager;
    }

    private void loadPowerUp(PowerUpType powerUpType, PowerUp powerUp) {
        if(powerUps.containsKey(powerUpType)) {
            ExceptionHandler.handle(new InvalidGameStateException("the power type " + powerUpType + " is loaded twice", null));
        }
        powerUps.put(powerUpType, new PowerUp(powerUp));
    }

    public PowerUp getPowerUp(PowerUpType powerUpType) {
        return powerUps.get(powerUpType);
    }

    public void loadFromLevel(LevelData levelData) {
        loadPowerUp(PowerUpType.SLOW, levelData.slowPowerUp);
    }
}
