package object.movable.powerup;

import config.LevelConfig;
import object.brick.Brick;

import java.awt.*;
import java.util.*;
import java.util.List;

public class PowerUpManager {
    private static PowerUpManager powerUpManager;
    private final Map<PowerUpType, PowerUp> powerUpsRegistry = new EnumMap<>(PowerUpType.class);
    private final Map<PowerUpType, PowerUp> activePowerUps = new EnumMap<>(PowerUpType.class);
    private final List<PowerUp> fallingPowerUps = new ArrayList<>();

    private final Object lock = new Object();
    private final Timer timer = new Timer(true);

    private PowerUpManager() {
    }

    public static PowerUpManager getInstance() {
        if (powerUpManager == null) {
            powerUpManager = new PowerUpManager();
        }
        return powerUpManager;
    }

    public void loadFromJson(LevelConfig levelConfig) {

        refreshPowerUps();
        powerUpsRegistry.put(PowerUpType.SLOW_BALL, new SlowBallPowerUp(levelConfig.slowPowerUp));
    }

    public void addPowerUp(PowerUpType powerUpType, Brick brick) {

        PowerUp newPowerUp = powerUpsRegistry.get(powerUpType).clone();
        newPowerUp.setInitialPosition(brick);
        newPowerUp.setPowerUpType(powerUpType);
        fallingPowerUps.add(newPowerUp);
    }

    public void applyPowerUp(PowerUpType powerUpType, PowerUp powerUp) {

        synchronized (lock) {
            PowerUp existing = activePowerUps.get(powerUpType);

            if (existing != null) {
                existing.revertEffect();
                existing.applyEffect();
                scheduleRevert(existing, powerUpType, existing.getDurationMs());
                return;
            }

            // New activation
            powerUp.applyEffect();
            activePowerUps.put(powerUpType, powerUp);
            scheduleRevert(powerUp, powerUpType, powerUp.getDurationMs());
        }
    }

    private void scheduleRevert(PowerUp powerUp, PowerUpType type, int durationMs) {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                synchronized (lock) {
                    PowerUp active = activePowerUps.get(type);
                    if (active == powerUp) {
                        active.revertEffect();
                        activePowerUps.remove(type);
                    }
                }
            }
        }, durationMs);
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

    private void refreshPowerUps() {

        activePowerUps.clear();
        powerUpsRegistry.clear();
        fallingPowerUps.clear();
    }
}
