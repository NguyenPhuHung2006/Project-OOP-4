package object.brick;

import config.LevelData;
import exception.ExceptionHandler;
import exception.InvalidGameStateException;
import object.GameContext;
import object.UI.Text.TextManager;
import object.UI.Text.TextType;

import java.awt.*;
import java.util.HashSet;

public class BrickManager {

    private static BrickManager brickManager;
    Brick[][] bricks;

    private int brickWidth;
    private int brickHeight;

    private int brickCountX;
    private int brickCountY;

    private HashSet<Integer> normalBrickTextureSet;
    private HashSet<Integer> strongBrickTextureSet;
    private HashSet<Integer> powerUpBrickTextureSet;

    private int normalBrickTypeId;
    private int strongBrickTypeId;
    private int powerUpBrickTypeId;

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

    private NormalBrick createNormalBrick(NormalBrick normalBrick) {
        return new NormalBrick(normalBrick);
    }

    private StrongBrick createStrongBrick(StrongBrick strongBrick) {
        return new StrongBrick(strongBrick);
    }

    private PowerUpBrick createPowerUpBrick(PowerUpBrick powerUpBrick) {
        return new PowerUpBrick(powerUpBrick);
    }

    public void loadFromLevel(LevelData levelData) {
        brickManager.setNormalBrickTypeId(levelData.normalBrickTypeId);
        brickManager.setStrongBrickTypeId(levelData.strongBrickTypeId);
        brickManager.setPowerUpBrickTypeId(levelData.powerUpBrickTypeId);
        brickManager.initBricks(levelData);
    }

    private void initBricks(LevelData levelData) {

        framePerRow = levelData.framePerRow;

        normalBrickTypeId = levelData.normalBrickTypeId;
        strongBrickTypeId = levelData.strongBrickTypeId;
        powerUpBrickTypeId = levelData.powerUpBrickTypeId;

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

        loadBricks(levelData);
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

                totalBricksCount++;

                int brickTextureIndex = brickLayout[y][x] / framePerRow;

                int currentBrickType = getBrickTextureType(brickTextureIndex);

                Brick currentBrick;

                if (currentBrickType == normalBrickTypeId) {
                    currentBrick = levelData.normalBrick.clone();

                } else if(currentBrickType == strongBrickTypeId) {
                    currentBrick = levelData.strongBrick.clone();

                } else if(currentBrickType == powerUpBrickTypeId) {
                    currentBrick = levelData.powerUpBrick.clone();

                } else {
                    ExceptionHandler.handle(new InvalidGameStateException("the current brick type is not found", null));
                    return;
                }

                currentBrick.setBrickTypeId(currentBrickType);

                currentBrick.setX(x * brickWidth);
                currentBrick.setY(y * brickHeight);
                currentBrick.setWidth(brickWidth);
                currentBrick.setHeight(brickHeight);

                // the textureX will always be equal to 0

                currentBrick.setTextureY(brickTextureIndex * currentBrick.getTextureHeight());

                if (isNormalBrick(currentBrick)) {
                    currentBrick = createNormalBrick((NormalBrick) currentBrick);
                } else if (isStrongBrick(currentBrick)) {
                    currentBrick = createStrongBrick((StrongBrick) currentBrick);
                } else if(isPowerUpBrick(currentBrick)) {
                    currentBrick = createPowerUpBrick((PowerUpBrick) currentBrick);
                }
                bricks[y][x] = currentBrick;
            }
        }
    }

    private boolean isNormalBrick(Brick brick) {
        return brick.getBrickTypeId() == normalBrickTypeId;
    }

    private boolean isStrongBrick(Brick brick) {
        return brick.getBrickTypeId() == strongBrickTypeId;
    }

    private boolean isPowerUpBrick(Brick brick) {
        return brick.getBrickTypeId() == powerUpBrickTypeId;
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

    private int getBrickTextureType(int tileIndex) {

        if (isNormalBrickTextureIndex(tileIndex)) {
            return normalBrickTypeId;
        } else if (isStrongBrickTextureIndex(tileIndex)) {
            return strongBrickTypeId;
        } else if(isPowerUpBrickTextureIndex(tileIndex)) {
            return powerUpBrickTypeId;
        }
        ExceptionHandler.handle(new InvalidGameStateException("index " + tileIndex + " is not valid when getting brick type", null));
        return -1;
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

    public void setNormalBrickTypeId(int normalBrickTypeId) {
        this.normalBrickTypeId = normalBrickTypeId;
    }

    public void setStrongBrickTypeId(int strongBrickTypeId) {
        this.strongBrickTypeId = strongBrickTypeId;
    }

    public void setPowerUpBrickTypeId(int powerUpBrickTypeId) {
        this.powerUpBrickTypeId = powerUpBrickTypeId;
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

    public int getNormalBrickTypeId() {
        return normalBrickTypeId;
    }

    public int getStrongBrickTypeId() {
        return strongBrickTypeId;
    }

    public int getPowerUpBrickTypeId() {
        return powerUpBrickTypeId;
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
