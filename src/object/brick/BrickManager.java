package object.brick;

import config.LevelData;
import exception.ExceptionHandler;
import exception.InvalidGameStateException;
import object.GameContext;
import object.UI.Text.TextManager;
import object.UI.Text.TextType;

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

    private BrickManager() {}

    public static BrickManager getInstance() {
        if (brickManager == null) {
            brickManager = new BrickManager();
        }
        return brickManager;
    }

    public void loadFromLevel(LevelData levelData) {
        initBricks(levelData);
        loadBricks(levelData);
    }

    private void initBricks(LevelData levelData) {

        framePerRow = levelData.framePerRow;

        normalBrickTextureSet = new HashSet<>();
        strongBrickTextureSet = new HashSet<>();
        powerUpBrickTextureSet = new HashSet<>();

        for (int normalBrickTextureIndex : levelData.normalBrickTextureIndices) {
            normalBrickTextureSet.add(normalBrickTextureIndex);
        }

        for (int strongBrickTextureIndex : levelData.strongBrickTextureIndices) {
            strongBrickTextureSet.add(strongBrickTextureIndex);
        }

        for(int powerUpBrickTextureIndex : levelData.powerUpBrickTextureIndices) {
            powerUpBrickTextureSet.add(powerUpBrickTextureIndex);
        }

        brickCountX = levelData.brickLayout[0].length;
        brickCountY = levelData.brickLayout.length;

        GameContext gameContext = GameContext.getInstance();

        int windowWidth = gameContext.getWindowWidth();
        int windowHeight = gameContext.getWindowHeight();

        brickWidth = windowWidth / brickCountX;
        brickHeight = windowHeight / brickCountY;

        brickRegistry.put(BrickType.NORMAL_BRICK, levelData.normalBrick);
        brickRegistry.put(BrickType.STRONG_BRICK, levelData.strongBrick);
        brickRegistry.put(BrickType.POWERUP_BRICK, levelData.powerUpBrick);
    }

    private void loadBricks(LevelData levelData) {

        destroyedBricksCount = 0;
        TextManager.getInstance().getText(TextType.SCORE).setContent(String.valueOf(destroyedBricksCount));
        totalBricksCount = 0;

        int[][] brickLayout = levelData.brickLayout;

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

                if(brickType == BrickType.POWERUP_BRICK ||
                   brickType == BrickType.NORMAL_BRICK) {
                    totalBricksCount++;
                }

                bricks[y][x] = currentBrick;
            }
        }
    }

    private Brick createBrickByType(BrickType type, Brick baseBrick) {
        switch (type) {
            case NORMAL_BRICK: return new NormalBrick(baseBrick);
            case STRONG_BRICK: return new StrongBrick(baseBrick);
            case POWERUP_BRICK: return new PowerUpBrick(baseBrick);
            default: return baseBrick;
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
        } else if(isPowerUpBrickTextureIndex(tileIndex)) {
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
                    if(currentBrick.isDestroyed()) {
                        bricks[y][x] = null;
                        int newDestroyedBrickCount = incrementDestroyedBricks();
                        TextManager.getInstance().getText(TextType.SCORE).setContent(String.valueOf(newDestroyedBrickCount));
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

    public int incrementDestroyedBricks() {
        return ++destroyedBricksCount;
    }

    public boolean isCleared() {
        return destroyedBricksCount >= totalBricksCount;
    }
}
