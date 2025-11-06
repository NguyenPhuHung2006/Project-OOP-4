
import config.GameConfig;
import object.GameContext;
import object.movable.powerup.PowerUp;
import object.movable.powerup.PowerUpManager;
import object.movable.powerup.PowerUpType;
import org.junit.jupiter.api.*;
import utils.JsonLoaderUtils;

import static org.junit.jupiter.api.Assertions.*;

import java.util.concurrent.TimeUnit;

public class PowerUpManagerTest {

    private PowerUpManager powerUpManager;
    private PowerUp basePowerUp;

    @BeforeEach
    void setup() {
        GameConfig gameConfig = JsonLoaderUtils.loadFromJson("assets/json/GameConfig.json", GameConfig.class);
        assertNotNull(gameConfig, "GameConfig should load successfully");

        GameContext gameContext = GameContext.getInstance();
        gameContext.setWindowWidth(gameConfig.windowWidth);
        gameContext.setWindowHeight(gameConfig.windowHeight);

        powerUpManager = PowerUpManager.getInstance();
        powerUpManager.revertAllPowerUps();

        basePowerUp = JsonLoaderUtils.loadFromJson("assets/json/junit_json/PowerUpManagerTest.json", TestPowerUp.class);
        assertNotNull(basePowerUp, "Test PowerUp JSON should load successfully");

        basePowerUp.setWidth(1);
        basePowerUp.setHeight(1);
    }

    @Test
    @DisplayName("PowerUp should apply immediately and revert after duration")
    void testPowerUpAppliesAndRevertsAfterDelay() throws Exception {
        TestPowerUp powerUp = new TestPowerUp(200, basePowerUp);

        powerUpManager.applyPowerUp(PowerUpType.EXPAND_PADDLE, powerUp);

        assertTrue(powerUp.isApplied(), "PowerUp should apply immediately");

        TimeUnit.MILLISECONDS.sleep(250);

        assertTrue(powerUp.isReverted(), "PowerUp should revert after its duration expires");
    }

    @Test
    @DisplayName("Reapplying a PowerUp of same type resets its timer")
    void testApplyingTwiceResetsTimer() throws Exception {
        TestPowerUp first = new TestPowerUp(300, basePowerUp);
        TestPowerUp second = new TestPowerUp(300, basePowerUp);

        powerUpManager.applyPowerUp(PowerUpType.EXPAND_PADDLE, first);
        TimeUnit.MILLISECONDS.sleep(150);

        powerUpManager.applyPowerUp(PowerUpType.EXPAND_PADDLE, second);

        TimeUnit.MILLISECONDS.sleep(250);

        assertFalse(second.isReverted(), "Second PowerUp should still be active (timer reset)");
    }

    @Test
    @DisplayName("Pausing and resuming should not trigger revert during pause")
    void testPauseAndResumePowerUps() throws Exception {
        TestPowerUp powerUp = new TestPowerUp(300, basePowerUp);
        powerUpManager.applyPowerUp(PowerUpType.EXPAND_PADDLE, powerUp);

        assertTrue(powerUp.isApplied(), "PowerUp should be applied before pausing");

        powerUpManager.pauseTimers();
        powerUpManager.resumeTimers();
        TimeUnit.MILLISECONDS.sleep(298);

        assertTrue(powerUp.isReverted(), "PowerUp should revert after resuming");
    }

    @Test
    @DisplayName("Reverting all PowerUps should cancel active effects immediately")
    void testRevertAllPowerUps() {
        TestPowerUp powerUp = new TestPowerUp(500, basePowerUp);
        powerUpManager.applyPowerUp(PowerUpType.EXPAND_PADDLE, powerUp);

        assertTrue(powerUp.isApplied(), "PowerUp should start as applied");

        powerUpManager.revertAllPowerUps();

        assertTrue(powerUp.isReverted(), "RevertAll should force all PowerUps to revert");
    }

    static class TestPowerUp extends PowerUp {
        private boolean applied = false;
        private boolean reverted = false;

        TestPowerUp(int durationMs, PowerUp template) {
            super(template);
            this.durationMs = durationMs;
        }

        @Override
        public void applyEffect() {
            applied = true;
        }

        @Override
        public void revertEffect() {
            reverted = true;
        }

        public boolean isApplied() {
            return applied;
        }

        public boolean isReverted() {
            return reverted;
        }

        @Override
        public PowerUp clone() {
            return this;
        }
    }
}
