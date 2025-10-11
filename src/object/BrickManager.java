package object;

import exception.ExceptionHandler;
import exception.GameException;
import exception.InvalidGameStateException;
import exception.ResourceLoadException;
import main.GameContext;
import main.GameManager;

import java.awt.*;
import java.util.HashSet;

public class BrickManager {

    private static BrickManager instance;
    Brick[][] bricks;
    
    private int brickWidth;
    private int brickHeight;
    
    private int brickCountX;
    private int brickCountY;

    private HashSet<Integer> normalBrickTextureSet;
    private HashSet<Integer> strongBrickTextureSet;

    private int normalBrickTypeId;
    private int strongBrickTypeId;

    private int framePerRow;

    public static BrickManager getInstance() {
        if (instance == null) {
            instance = new BrickManager();
        }
        return instance;
    }

    private NormalBrick createNormalBrick(NormalBrick normalBrick) {
        return new NormalBrick(normalBrick);
    }
    private StrongBrick createStrongBrick(StrongBrick strongBrick) {
        return new StrongBrick(strongBrick);
    }

    public void initBricks(LevelData levelData) {

        framePerRow = levelData.framePerRow;

        normalBrickTypeId = levelData.normalBrickTypeId;
        strongBrickTypeId = levelData.strongBrickTypeId;

        normalBrickTextureSet = new HashSet<>();
        strongBrickTextureSet = new HashSet<>();

        for(int normalBrickTextureIndex : levelData.normalBrickTextureIndices) {
            normalBrickTextureSet.add(normalBrickTextureIndex);
        }

        for(int strongBrickTextureIndex : levelData.strongBrickTextureIndices) {
            strongBrickTextureSet.add(strongBrickTextureIndex);
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

        int[][] brickLayout = levelData.brickLayout;

        bricks = new Brick[brickCountY][brickCountX];

        for (int y = 0; y < brickCountY; y++) {
            for (int x = 0; x < brickCountX; x++) {

                if (brickLayout[y][x] < 0)
                    continue;

                int brickTextureIndex = brickLayout[y][x] / framePerRow;

                int currentBrickType;

                try {
                    currentBrickType = getBrickTextureType(brickTextureIndex);
                } catch (InvalidGameStateException gameStateException) {
                    ExceptionHandler.handle(gameStateException);
                    GameManager.getInstance().stopGame();
                    return;
                }

                Brick currentBrick;

                if(currentBrickType == normalBrickTypeId) {
                    currentBrick = levelData.normalBrick.clone();
                } else {
                    currentBrick = levelData.strongBrick.clone();
                }

                currentBrick.setX(x * brickWidth);
                currentBrick.setY(y * brickHeight);
                currentBrick.setWidth(brickWidth);
                currentBrick.setHeight(brickHeight);

                currentBrick.setBrickTypeId(currentBrickType);
                currentBrick.setTextureX(0);
                currentBrick.setTextureY(brickTextureIndex * currentBrick.getTextureHeight());

                if(isNormalBrick(currentBrick)) {
                    currentBrick = createNormalBrick((NormalBrick) currentBrick);
                } else if(isStrongBrick(currentBrick)) {
                    currentBrick = createStrongBrick((StrongBrick) currentBrick);
                } else {
                    ExceptionHandler.handle(new InvalidGameStateException("the current brick type is not found", null));
                    GameManager.getInstance().stopGame();
                    return;
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

    private boolean isNormalBrickTextureIndex(int brickTextureIndex) {
        return normalBrickTextureSet.contains(brickTextureIndex);
    }

    private boolean isStrongBrickTextureIndex(int brickTextureIndex) {
        return strongBrickTextureSet.contains(brickTextureIndex);
    }

    private int getBrickTextureType(int tileIndex) throws InvalidGameStateException {

        if(isNormalBrickTextureIndex(tileIndex)) {
            return normalBrickTypeId;
        } else if(isStrongBrickTextureIndex(tileIndex)) {
            return strongBrickTypeId;
        }
        throw new InvalidGameStateException("index" + tileIndex + "is not valid when getting brick type", null);
    }

    public void updateBricks() {

        for(int y = 0; y < brickCountY; y++) {
            for(int x = 0; x < brickCountX; x++) {
                Brick currentBrick = bricks[y][x];
                if(currentBrick != null) {
                    currentBrick.update();
                }
            }
        }
    }

    public void renderBricks(Graphics2D graphics2D) {

        for(int y = 0; y < brickCountY; y++) {
            for(int x = 0; x < brickCountX; x++) {
                if(bricks[y][x] != null) {
                    Brick currentBrick = bricks[y][x];
                    currentBrick.render(graphics2D);
                }
            }
        }
    }

    public int getBrickWidth() {
        return brickWidth;
    }

    public int getBrickHeight() {
        return brickHeight;
    }

    public void setNormalBrickTypeId(int normalBrickTypeId) {
        this.normalBrickTypeId = normalBrickTypeId;
    }

    public void setStrongBrickTypeId(int strongBrickTypeId) {
        this.strongBrickTypeId = strongBrickTypeId;
    }

    public int getNormalBrickTypeId() {
        return normalBrickTypeId;
    }

    public int getStrongBrickTypeId() {
        return strongBrickTypeId;
    }

    public Brick[][] getBricks() {
        return bricks;
    }
}
