package object.movable.powerup;

import config.LevelData;
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

    public void loadFromLevel(LevelData levelData) {

        refreshPowerUps();
        powerUpsRegistry.put(PowerUpType.SLOW_BALL, new SlowBallPowerUp(levelData.slowPowerUp));
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
                timer.purge();
            } else {
                powerUp.applyEffect();
                activePowerUps.put(powerUpType, powerUp);
            }

            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    synchronized (lock) {
                        PowerUp active = activePowerUps.get(powerUpType);
                        if (active != null) {
                            active.revertEffect();
                            activePowerUps.remove(powerUpType);
                        }
                    }
                }
            }, powerUp.getDurationMs());
        }
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

    private void refreshPowerUps() {

        activePowerUps.clear();
        powerUpsRegistry.clear();
        fallingPowerUps.clear();
    }
}
