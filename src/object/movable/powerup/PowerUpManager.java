package object.movable.powerup;

import config.LevelConfig;
import exception.ExceptionHandler;
import exception.InvalidGameStateException;
import object.brick.Brick;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.*;

/**
 * Manages all {@link PowerUp} instances within the game, including their creation,
 * activation, timing, and visual updates.
 * <p>
 * This class follows the singleton pattern to ensure only one manager
 * instance controls all power-ups. It supports:
 * <ul>
 *   <li>Registering power-ups from level configurations.</li>
 *   <li>Handling falling power-ups that appear after bricks are destroyed.</li>
 *   <li>Applying and reverting timed power-up effects using scheduled tasks.</li>
 *   <li>Pausing and resuming power-up timers when the game is paused.</li>
 * </ul>
 * </p>
 * <p>
 * This system is optimized by tracking only active and nearby power-ups,
 * rather than iterating over every brick or entity in the game.
 * </p>
 */
public class PowerUpManager {
    private static PowerUpManager powerUpManager;
    private Map<PowerUpType, PowerUp> powerUpsRegistry = new EnumMap<>(PowerUpType.class);
    private Map<PowerUpType, PowerUp> activePowerUps = new EnumMap<>(PowerUpType.class);
    private List<PowerUp> fallingPowerUps = new ArrayList<>();

    private final Object lock = new Object();

    private final transient ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private final transient Map<PowerUpType, ScheduledFuture<?>> scheduledFutures = new EnumMap<>(PowerUpType.class);

    private Map<PowerUpType, Long> startTimes = new EnumMap<>(PowerUpType.class);
    private Map<PowerUpType, Long> remainingTimes = new EnumMap<>(PowerUpType.class);
    private boolean paused = false;

    private PowerUpManager() {
    }

    /**
     * Returns the singleton instance of the {@code PowerUpManager}.
     *
     * @return the single {@code PowerUpManager} instance
     */
    public static PowerUpManager getInstance() {
        if (powerUpManager == null) {
            powerUpManager = new PowerUpManager();
        }
        return powerUpManager;
    }

    /**
     * Loads power-up configurations from the given level configuration.
     * Replaces any existing power-up registry data.
     *
     * @param levelConfig the configuration data containing available power-ups
     */
    public void loadFromJson(LevelConfig levelConfig) {

        refresh();
        powerUpsRegistry.put(PowerUpType.SLOW_BALL, levelConfig.slowPowerUp);
        powerUpsRegistry.put(PowerUpType.EXPAND_PADDLE, levelConfig.expandPaddlePowerUp);
        powerUpsRegistry.put(PowerUpType.SPEEDUP_PADDLE, levelConfig.speedUpPaddlePowerUp);
        powerUpsRegistry.put(PowerUpType.ADD_LIVE, levelConfig.addLivePowerUp);
    }

    /**
     * Creates and spawns a new power-up at the given brick’s location.
     *
     * @param powerUpType the type of power-up to create
     * @param brick       the brick from which the power-up originates
     */
    public void addPowerUp(PowerUpType powerUpType, Brick brick) {

        PowerUp basePowerUp = powerUpsRegistry.get(powerUpType).clone();
        if (basePowerUp == null) {
            ExceptionHandler.handle(new InvalidGameStateException("the power up: " + powerUpType + " has not been registered", null));
        }

        PowerUp newPowerUp = powerUpType.create(basePowerUp);
        newPowerUp.setInitialPosition(brick);
        newPowerUp.setPowerUpType(powerUpType);
        fallingPowerUps.add(newPowerUp);
    }

    /**
     * Activates a power-up effect and schedules its automatic reversion.
     *
     * @param powerUpType the type of power-up being applied
     * @param powerUp     the specific power-up instance
     */
    public void applyPowerUp(PowerUpType powerUpType, PowerUp powerUp) {

        synchronized (lock) {

            ScheduledFuture<?> prevFuture = scheduledFutures.remove(powerUpType);
            if (prevFuture != null) {
                prevFuture.cancel(false);
            }

            long duration = powerUp.getDurationMs();
            startTimes.put(powerUpType, System.currentTimeMillis());
            remainingTimes.put(powerUpType, duration);

            powerUp.applyEffect();
            activePowerUps.put(powerUpType, powerUp);

            ScheduledFuture<?> future = scheduleRevert(powerUp, powerUpType, duration);
            scheduledFutures.put(powerUpType, future);
        }
    }

    /**
     * Schedules a task to revert the effect of a given power-up after a specified delay.
     * <p>
     * This method creates a scheduled task that will run in the future (not periodically).
     * The task is submitted to the {@link ScheduledExecutorService} and stored in a map
     * so it can be tracked or canceled if necessary.
     * </p>
     * <p>
     * When the delay (in milliseconds) has elapsed, the task retrieves the corresponding
     * {@link PowerUpType} from the {@code scheduledFutures} map. If the retrieved scheduled
     * future matches the one originally created (by reference), the power-up’s effect
     * is reverted using {@code revertEffect()}, and the power-up type is removed from
     * any active tracking structures.
     * </p>
     * <p>
     * This approach ensures that each power-up effect is reverted exactly once, and it avoids
     * unnecessary iteration through all active power-ups.
     * </p>
     *
     * @param powerUp     the power-up instance whose effect will be reverted
     * @param powerUpType the type of the power-up to schedule for reversion
     * @param delayMs     the delay in milliseconds before the reversion occurs
     * @return the {@link ScheduledFuture} representing the scheduled revert task
     */
    private ScheduledFuture<?> scheduleRevert(PowerUp powerUp, PowerUpType powerUpType, long delayMs) {

        return scheduler.schedule(() -> {
            synchronized (lock) {
                PowerUp active = activePowerUps.get(powerUpType);
                if (active == powerUp) {
                    active.revertEffect();
                    activePowerUps.remove(powerUpType);
                    scheduledFutures.remove(powerUpType);
                    startTimes.remove(powerUpType);
                    remainingTimes.remove(powerUpType);
                }
            }
        }, delayMs, TimeUnit.MILLISECONDS);
    }

    /**
     * Pauses all active power-up timers, storing their remaining durations.
     * This is typically called when the game is paused.
     */
    public void pauseTimers() {
        synchronized (lock) {
            if (paused) {
                return;
            }
            paused = true;

            saveScheduledPowerUps();
        }
    }

    /**
     * Resumes all paused power-up timers, restoring their effects and
     * continuing their countdowns.
     */
    public void resumeTimers() {
        synchronized (lock) {
            if (!paused) {
                return;
            }
            paused = false;

            resumeActivePowerUps();
        }
    }

    /**
     * Handles cleanup when the game is paused or saved to JSON.
     * <p>
     * Iterates through all active power-ups, canceling their scheduled revert tasks.
     * For each power-up, the elapsed time since activation is calculated, and the
     * remaining duration is determined as {@code duration - elapsedTime}.
     * This remaining time is stored in a map, where the key is the power-up type
     * and the value is the remaining duration.
     * </p>
     */
    private void saveScheduledPowerUps() {
        long now = System.currentTimeMillis();
        for (var entry : activePowerUps.entrySet()) {
            PowerUpType type = entry.getKey();
            PowerUp powerUp = entry.getValue();

            ScheduledFuture<?> future = scheduledFutures.remove(type);
            if (future != null) {
                future.cancel(false);
            }

            Long start = startTimes.get(type);
            Long duration = remainingTimes.get(type);
            if (start != null && duration != null) {
                long elapsed = now - start;
                long remaining = Math.max(duration - elapsed, 0);
                remainingTimes.put(type, remaining);
            }
            powerUp.revertEffect();
        }
    }

    /**
     * Restores all active power-ups when the game is resumed or deserialized from JSON.
     * <p>
     * Iterates through previously active power-ups, retrieving each power-up type
     * and its remaining duration from the saved {@code remainingTime} map (which may
     * be deserialized from JSON). The {@code startTime} is reset to the current time,
     * and a new scheduled task is created using the remaining duration. Each new task
     * is then stored in the {@code scheduledFutures} map.
     * </p>
     */
    private void resumeActivePowerUps() {
        for (var entry : activePowerUps.entrySet()) {
            PowerUpType type = entry.getKey();
            PowerUp powerUp = entry.getValue();
            Long remaining = remainingTimes.get(type);
            if (remaining != null && remaining > 0) {
                powerUp.applyEffect();
                startTimes.put(type, System.currentTimeMillis());
                ScheduledFuture<?> future = scheduleRevert(powerUp, type, remaining);
                scheduledFutures.put(type, future);
            }
        }
    }

    /**
     * Cancels all active power-ups and reverts their effects immediately.
     */
    public void revertAllPowerUps() {
        synchronized (lock) {
            for (ScheduledFuture<?> future : scheduledFutures.values()) {
                future.cancel(false);
            }
            scheduledFutures.clear();

            for (PowerUp powerUp : activePowerUps.values()) {
                powerUp.revertEffect();
            }

            activePowerUps.clear();
            startTimes.clear();
            remainingTimes.clear();
        }
    }

    /**
     * Serializes all falling power-ups to preserve their state,
     * especially when the game window size changes.
     */
    public void serializePowerUps() {
        for (PowerUp fallingPowerUp : fallingPowerUps) {
            fallingPowerUp.serializeToJson();
        }
    }

    /**
     * Deserializes power-up data loaded from JSON and adjusts it
     * to match the current window size.
     *
     * @param powerUpManager the {@link PowerUpManager} instance
     *                       deserialized from JSON when loading game progress
     */
    public void deserializePowerUps(PowerUpManager powerUpManager) {

        powerUpsRegistry = powerUpManager.powerUpsRegistry;
        fallingPowerUps = powerUpManager.fallingPowerUps;
        activePowerUps = powerUpManager.activePowerUps;
        startTimes = powerUpManager.startTimes;
        remainingTimes = powerUpManager.remainingTimes;
        paused = true;

        for (PowerUp fallingPowerUp : fallingPowerUps) {
            fallingPowerUp.deserializeFromJson();
        }
    }

    /**
     * Updates the position of all falling power-ups and removes any that
     * have been collected or moved out of bounds.
     */
    public void updateFallingPowerUps() {
        for (PowerUp fallingPowerUp : fallingPowerUps) {
            fallingPowerUp.update();
        }
        fallingPowerUps.removeIf(PowerUp::isRemoved);
    }

    /**
     * Renders all visible falling power-ups on the screen.
     *
     * @param graphics2D the graphics context to draw on
     */
    public void renderPowerUps(Graphics2D graphics2D) {
        for (PowerUp fallingPowerUp : fallingPowerUps) {
            fallingPowerUp.render(graphics2D);
        }
    }

    private void refresh() {

        for (ScheduledFuture<?> future : scheduledFutures.values()) {
            future.cancel(false);
        }
        activePowerUps.clear();
        powerUpsRegistry.clear();
        fallingPowerUps.clear();
    }
}
