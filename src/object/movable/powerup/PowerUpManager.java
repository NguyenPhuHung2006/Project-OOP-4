package object.movable.powerup;

import config.LevelConfig;
import exception.ExceptionHandler;
import exception.InvalidGameStateException;
import object.brick.Brick;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.*;

public class PowerUpManager {
    private static PowerUpManager powerUpManager;
    private final Map<PowerUpType, PowerUp> powerUpsRegistry = new EnumMap<>(PowerUpType.class);
    private final Map<PowerUpType, PowerUp> activePowerUps = new EnumMap<>(PowerUpType.class);
    private final List<PowerUp> fallingPowerUps = new ArrayList<>();

    private final Object lock = new Object();

    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private final Map<PowerUpType, ScheduledFuture<?>> scheduledFutures = new EnumMap<>(PowerUpType.class);

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
        powerUpsRegistry.put(PowerUpType.SLOW_BALL, levelConfig.slowPowerUp);
        powerUpsRegistry.put(PowerUpType.EXPAND_PADDLE, levelConfig.expandPaddleWidthPowerUp);
    }

    public void addPowerUp(PowerUpType powerUpType, Brick brick) {

        PowerUp basePowerUp = powerUpsRegistry.get(powerUpType).clone();
        if(basePowerUp == null) {
            ExceptionHandler.handle(new InvalidGameStateException("the power up: " + powerUpType + " has not been registered", null));
        }

        PowerUp newPowerUp = powerUpType.create(basePowerUp);
        newPowerUp.setInitialPosition(brick);
        newPowerUp.setPowerUpType(powerUpType);
        fallingPowerUps.add(newPowerUp);
    }

    public void applyPowerUp(PowerUpType powerUpType, PowerUp powerUp) {

        synchronized (lock) {

            ScheduledFuture<?> prevFuture = scheduledFutures.remove(powerUpType);
            if (prevFuture != null) {
                prevFuture.cancel(false);
            }

            powerUp.applyEffect();
            activePowerUps.put(powerUpType, powerUp);

            ScheduledFuture<?> future = scheduleRevert(powerUp, powerUpType);
            scheduledFutures.put(powerUpType, future);
        }
    }

    private ScheduledFuture<?> scheduleRevert(PowerUp powerUp, PowerUpType powerUpType) {

        return scheduler.schedule(() -> {
            synchronized (lock) {
                PowerUp active = activePowerUps.get(powerUpType);
                if (active == powerUp) {
                    active.revertEffect();
                    activePowerUps.remove(powerUpType);
                    scheduledFutures.remove(powerUpType);
                }
            }
        }, powerUp.getDurationMs(), TimeUnit.MILLISECONDS);

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

        for (ScheduledFuture<?> future : scheduledFutures.values()) {
            future.cancel(false);
        }
        activePowerUps.clear();
        powerUpsRegistry.clear();
        fallingPowerUps.clear();
    }
}
