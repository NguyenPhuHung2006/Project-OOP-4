package object.brick;

import config.LevelConfig;
import exception.ExceptionHandler;
import exception.InvalidGameStateException;
import object.GameContext;

import java.awt.*;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.Map;

public class BrickManager {

    private static BrickManager brickManager;
    Brick[][] bricks;

    private int brickWidth;
    private int brickHeight;

    private int brickCountX;
    private int brickCountY;

    private final Map<BrickType, Brick> brickRegistry = new EnumMap<>(BrickType.class);

    private HashSet<Integer> normalBrickTextureSet;
    private HashSet<Integer> strongBrickTextureSet;
    private HashSet<Integer> powerUpBrickTextureSet;

    private int framePerRow;

    private int destroyedBricksCount;
    private int totalBricksCount;

    private boolean isIncremented;

    private BrickManager() {
    }

    public static BrickManager getInstance() {
        if (brickManager == null) {
            brickManager = new BrickManager();
        }
        return brickManager;
    }

    public void loadFromJson(LevelConfig levelConfig) {
        initBricks(levelConfig);
        loadBricks(levelConfig);
    }

    private void initBricks(LevelConfig levelConfig) {

        framePerRow = levelConfig.framePerRow;

        normalBrickTextureSet = new HashSet<>();
        strongBrickTextureSet = new HashSet<>();
        powerUpBrickTextureSet = new HashSet<>();

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

                baseBrick.setTextureX(0);
                baseBrick.setTextureY(brickTextureIndex * baseBrick.getTextureHeight());

                Brick currentBrick = createBrickByType(brickType, baseBrick);

                if (brickType == BrickType.POWERUP_BRICK ||
                        brickType == BrickType.NORMAL_BRICK) {
                    totalBricksCount++;
                }

                bricks[y][x] = currentBrick;
            }
        }
    }

    private Brick createBrickByType(BrickType type, Brick baseBrick) {
        switch (type) {
            case NORMAL_BRICK:
                return new NormalBrick(baseBrick);
            case STRONG_BRICK:
                return new StrongBrick(baseBrick);
            case POWERUP_BRICK:
                return new PowerUpBrick(baseBrick);
            default:
                return baseBrick;
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

    public void setDestroyedBricksCount(int destroyedBricksCount) {
        this.destroyedBricksCount = destroyedBricksCount;
    }

    public void setTotalBricksCount(int totalBricksCount) {
        this.totalBricksCount = totalBricksCount;
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
        if(isIncremented) {
            isIncremented = false;
            return true;
        }
        return false;
    }

    public int incrementDestroyedBricks() {
        isIncremented = true;
        return ++destroyedBricksCount;
    }

    public boolean isCleared() {
        return destroyedBricksCount >= totalBricksCount;
    }
}
