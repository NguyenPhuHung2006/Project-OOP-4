import object.BrickManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BrickManagerTest {
    BrickManager brickManager;

    @BeforeEach
    void setUp() {
        brickManager = BrickManager.getInstance();
    }

    @Test
    void testDestroyedBrickCountIncreases() {
        int before = brickManager.getDestroyedBricksCount();

        int after = brickManager.incrementDestroyedBricks();

        assertEquals(before + 1, after, "Destroyed brick count should increase by 1 after using the increment method");
    }

    @Test
    void testIsLevelCleared() {
        brickManager.setDestroyedBricksCount(0);

        brickManager.setTotalBricksCount(0);

        assertEquals(true, brickManager.isCleared(), "the isCleared method is not correct");
    }

}
