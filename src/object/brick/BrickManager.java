package object.brick;

import config.LevelConfig;
import exception.ExceptionHandler;
import exception.InvalidGameStateException;
import object.GameContext;

import java.awt.*;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.Map;

/**
 * Manages all brick objects in the game.
 * <p>
 * Responsible for loading bricks from level configuration,
 * handling updates, rendering, serialization, and game logic.
 * Implements a singleton pattern via {@link #getInstance()}.
 */
public class BrickManager {

    private static BrickManager brickManager;
    Brick[][] bricks;

    private int brickWidth;
    private int brickHeight;

    private int brickCountX;
    private int brickCountY;

    private final Map<BrickType, Brick> brickRegistry = new EnumMap<>(BrickType.class);

    private HashSet<Integer> normalBrickTextureSet = new HashSet<>();
    private HashSet<Integer> strongBrickTextureSet = new HashSet<>();
    private HashSet<Integer> powerUpBrickTextureSet = new HashSet<>();

    private int framePerRow;

    private int destroyInterval;

    private int destroyedBricksCount;
    private int totalBricksCount;

    private boolean isIncremented;

    private BrickManager() {
    }

    /**
     * Gets the singleton instance of {@code BrickManager}.
     *
     * @return the shared BrickManager instance
     */
    public static BrickManager getInstance() {
        if (brickManager == null) {
            brickManager = new BrickManager();
        }
        return brickManager;
    }

    /**
     * Loads brick data from a JSON-based level configuration.
     *
     * @param levelConfig the level configuration to load from
     */
    public void loadFromJson(LevelConfig levelConfig) {
        refresh();
        initBricks(levelConfig);
        loadBricks(levelConfig);
    }

    /**
     * Initializes brick textures and types based on configuration.
     */
    private void initBricks(LevelConfig levelConfig) {

        framePerRow = levelConfig.framePerRow;
        destroyInterval = levelConfig.destroyInterval;

        for (int normalBrickTextureIndex : levelConfig.normalBrickTextureIndices) {
            normalBrickTextureSet.add(normalBrickTextureIndex);
        }

        for (int strongBrickTextureIndex : levelConfig.strongBrickTextureIndices) {
            strongBrickTextureSet.add(strongBrickTextureIndex);
        }

        for (int powerUpBrickTextureIndex : levelConfig.powerUpBrickTextureIndices) {
            powerUpBrickTextureSet.add(powerUpBrickTextureIndex);
        }

        brickCountX = levelConfig.brickLayout[0].length;
        brickCountY = levelConfig.brickLayout.length;

        GameContext gameContext = GameContext.getInstance();

        int windowWidth = gameContext.getWindowWidth();
        int windowHeight = gameContext.getWindowHeight();

        brickWidth = windowWidth / brickCountX;
        brickHeight = windowHeight / brickCountY;

        brickRegistry.put(BrickType.NORMAL_BRICK, levelConfig.normalBrick);
        brickRegistry.put(BrickType.STRONG_BRICK, levelConfig.strongBrick);
        brickRegistry.put(BrickType.POWERUP_BRICK, levelConfig.powerUpBrick);
    }

    /**
     * Populates the game field with bricks based on layout configuration.
     */
    private void loadBricks(LevelConfig levelConfig) {

        destroyedBricksCount = 0;
        totalBricksCount = 0;

        int[][] brickLayout = levelConfig.brickLayout;

        bricks = new Brick[brickCountY][brickCountX];

        for (int y = 0; y < brickCountY; y++) {
            for (int x = 0; x < brickCountX; x++) {

                if (brickLayout[y][x] < 0)
                    continue;

                int brickTextureIndex = brickLayout[y][x] / framePerRow;

                BrickType brickType = getBrickTextureType(brickTextureIndex);

                Brick baseBrick = brickRegistry.get(brickType).clone();

                baseBrick.setX(x * brickWidth);
                baseBrick.setY(y * brickHeight);
                baseBrick.setWidth(brickWidth);
                baseBrick.setHeight(brickHeight);

                baseBrick.setRelativeSize((float) brickWidth / GameContext.getInstance().getWindowWidth());

                baseBrick.setTextureX(0);
                baseBrick.setTextureY(brickTextureIndex * baseBrick.getTextureHeight());

                assert brickType != null;
                Brick currentBrick = brickType.create(baseBrick);

                if (brickType == BrickType.POWERUP_BRICK ||
                        brickType == BrickType.NORMAL_BRICK) {
                    totalBricksCount++;
                }

                bricks[y][x] = currentBrick;
            }
        }
    }

    /**
     * Serializes all bricks to JSON.
     */
    public void serializeBricks() {

        for (int y = 0; y < brickCountY; y++) {
            for (int x = 0; x < brickCountX; x++) {
                if (bricks[y][x] != null) {
                    bricks[y][x].serializeToJson();
                }
            }
        }
    }

    /**
     * Deserializes bricks from another {@code BrickManager} instance loaded from JSON.
     *
     * @param brickManager a previously saved BrickManager state
     */
    public void deserializeBricks(BrickManager brickManager) {

        bricks = brickManager.getBricks();

        brickCountY = bricks.length;
        brickCountX = bricks[0].length;
        brickWidth = GameContext.getInstance().getWindowWidth() / brickCountX;
        brickHeight = GameContext.getInstance().getWindowHeight() / brickCountY;

        framePerRow = brickManager.framePerRow;
        destroyedBricksCount = brickManager.destroyedBricksCount;
        totalBricksCount = brickManager.totalBricksCount;
        isIncremented = brickManager.isIncremented;
        destroyInterval = brickManager.destroyInterval;

        for (int y = 0; y < brickCountY; y++) {
            for (int x = 0; x < brickCountX; x++) {
                if (bricks[y][x] != null) {
                    bricks[y][x].deserializeFromJson();
                }
            }
        }
    }

    private boolean isNormalBrickTextureIndex(int brickTextureIndex) {
        return normalBrickTextureSet.contains(brickTextureIndex);
    }

    private boolean isStrongBrickTextureIndex(int brickTextureIndex) {
        return strongBrickTextureSet.contains(brickTextureIndex);
    }

    private boolean isPowerUpBrickTextureIndex(int brickTextureIndex) {
        return powerUpBrickTextureSet.contains(brickTextureIndex);
    }

    private BrickType getBrickTextureType(int tileIndex) {

        if (isNormalBrickTextureIndex(tileIndex)) {
            return BrickType.NORMAL_BRICK;
        } else if (isStrongBrickTextureIndex(tileIndex)) {
            return BrickType.STRONG_BRICK;
        } else if (isPowerUpBrickTextureIndex(tileIndex)) {
            return BrickType.POWERUP_BRICK;
        }
        ExceptionHandler.handle(new InvalidGameStateException("index " + tileIndex + " is not valid when getting brick type", null));
        return null;
    }

    /**
     * Updates all active bricks.
     * Handles hit detection and brick removal.
     */
    public void updateBricks() {

        for (int y = 0; y < brickCountY; y++) {
            for (int x = 0; x < brickCountX; x++) {
                Brick currentBrick = bricks[y][x];
                if (currentBrick != null) {
                    currentBrick.update();
                    if (currentBrick.isDestroyed()) {
                        bricks[y][x] = null;
                        incrementDestroyedBricks();
                    }
                }
            }
        }
    }

    /**
     * Renders all active bricks.
     *
     * @param graphics2D the graphics context
     */
    public void renderBricks(Graphics2D graphics2D) {

        for (int y = 0; y < brickCountY; y++) {
            for (int x = 0; x < brickCountX; x++) {
                if (bricks[y][x] != null) {
                    Brick currentBrick = bricks[y][x];
                    currentBrick.render(graphics2D);
                }
            }
        }
    }

    private void refresh() {

        for (int y = 0; y < brickCountY; y++) {
            for (int x = 0; x < brickCountX; x++) {
                bricks[y][x] = null;
            }
        }
        brickWidth = 0;
        brickHeight = 0;
        brickCountX = 0;
        brickCountY = 0;
        brickRegistry.clear();
        normalBrickTextureSet.clear();
        strongBrickTextureSet.clear();
        powerUpBrickTextureSet.clear();
        framePerRow = 0;
        destroyInterval = 0;
        destroyedBricksCount = 0;
        totalBricksCount = 0;
        isIncremented = false;
    }

    public void setDestroyedBricksCount(int destroyedBricksCount) {
        this.destroyedBricksCount = destroyedBricksCount;
    }

    public void setTotalBricksCount(int totalBricksCount) {
        this.totalBricksCount = totalBricksCount;
    }

    public void setBricks(Brick[][] bricks) {
        this.bricks = bricks;
    }

    public int getBrickWidth() {
        return brickWidth;
    }

    public int getBrickHeight() {
        return brickHeight;
    }

    public int getTotalBricksCount() {
        return totalBricksCount;
    }

    public int getDestroyedBricksCount() {
        return destroyedBricksCount;
    }

    public Brick[][] getBricks() {
        return bricks;
    }

    public boolean isIncremented() {
        if (isIncremented) {
            isIncremented = false;
            return true;
        }
        return false;
    }

    public int incrementDestroyedBricks() {
        isIncremented = true;
        return ++destroyedBricksCount;
    }

    public int getDestroyInterval() {
        return destroyInterval;
    }

    /**
     * @return true if all destructible bricks are cleared
     */
    public boolean isCleared() {
        return destroyedBricksCount >= totalBricksCount;
    }
}
