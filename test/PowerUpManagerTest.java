import config.GameConfig;
import object.GameContext;
import object.movable.powerup.*;
import org.junit.jupiter.api.*;
import utils.JsonLoaderUtils;

import static org.junit.jupiter.api.Assertions.*;

import java.util.concurrent.TimeUnit;

public class PowerUpManagerTest {

    PowerUpManager powerUpManager;
    PowerUp testPowerUp;

    @BeforeEach
    void setup() {

        GameConfig gameConfig = JsonLoaderUtils.initFromJson("assets/json/GameConfig.json", GameConfig.class);

        GameContext gameContext = GameContext.getInstance();
        assertNotNull(gameConfig);
        gameContext.setWindowWidth(gameConfig.windowWidth);
        gameContext.setWindowHeight(gameConfig.windowHeight);

        powerUpManager = PowerUpManager.getInstance();
        testPowerUp = JsonLoaderUtils.initFromJson("assets/json/junit_json/PowerUpManagerTest.json", TestPowerUp.class);

        assertNotNull(testPowerUp);
        testPowerUp.setWidth(1);
        testPowerUp.setHeight(1);
    }

    @Test
    void testPowerUpAppliesAndRevertsAfterDelay() throws Exception {
        TestPowerUp powerUp = new TestPowerUp(200, testPowerUp);

        powerUpManager.applyPowerUp(PowerUpType.EXPAND_PADDLE, powerUp);

        assertTrue(powerUp.isApplied(), "Power-up should apply immediately");

        TimeUnit.MILLISECONDS.sleep(250);

        assertTrue(powerUp.isReverted(), "Power-up should revert after delay");
    }

    @Test
    void testApplyingTwiceResetsTimer() throws Exception {
        TestPowerUp powerUp1 = new TestPowerUp(300, testPowerUp);
        TestPowerUp powerUp2 = new TestPowerUp(300, testPowerUp);

        powerUpManager.applyPowerUp(PowerUpType.EXPAND_PADDLE, powerUp1);
        TimeUnit.MILLISECONDS.sleep(150);

        powerUpManager.applyPowerUp(PowerUpType.EXPAND_PADDLE, powerUp2);

        TimeUnit.MILLISECONDS.sleep(250);

        assertFalse(powerUp2.isReverted(), "Second power-up should still be active");
    }

    static class TestPowerUp extends PowerUp {
        private boolean applied = false;
        private boolean reverted = false;
        private final int duration;

        TestPowerUp(int durationMs, PowerUp testPowerUp) {
            super(testPowerUp);
            this.duration = durationMs;
        }

        @Override
        public void applyEffect() {
            applied = true;
        }

        @Override
        public void revertEffect() {
            reverted = true;
        }

        @Override
        public int getDurationMs() {
            return duration;
        }

        public boolean isApplied() { return applied; }
        public boolean isReverted() { return reverted; }

        @Override
        public PowerUp clone() { return this; } // simple no-op clone
    }
}
