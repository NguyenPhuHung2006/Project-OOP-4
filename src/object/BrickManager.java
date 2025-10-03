package object;

import main.GameContext;

import java.awt.*;

public class BrickManager {

    private static BrickManager instance;
    Brick[][] bricks;

    private int normalBrickTypeId;
    private int strongBrickTypeId;

    private int normalBrickStartIndex;
    private int strongBrickStartIndex;

    private int normalBrickCount;
    private int strongBrickCount;
    
    private int brickWidth;
    private int brickHeight;
    
    private int brickCountX;
    private int brickCountY;

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

        normalBrickStartIndex = levelData.normalBrickStartIndex;
        strongBrickStartIndex = levelData.strongBrickStartIndex;

        normalBrickCount = levelData.normalBrickCount;
        strongBrickCount = levelData.strongBrickCount;

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

                int tileIndex = brickLayout[y][x] / framePerRow;

                int currentBrickType = getBrickType(tileIndex);

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
                currentBrick.setTextureY(tileIndex * currentBrick.getTextureHeight());

                if(currentBrickType == normalBrickTypeId) {
                    currentBrick = createNormalBrick((NormalBrick) currentBrick);
                } else {
                    currentBrick = createStrongBrick((StrongBrick) currentBrick);
                }
                bricks[y][x] = currentBrick;
            }
        }
    }

    private boolean isNormalBrickIndex(int tileIndex) {
        return tileIndex >= normalBrickStartIndex
                && tileIndex < normalBrickStartIndex + normalBrickCount;
    }

    private boolean isStrongBrickIndex(int tileIndex) {
        return tileIndex >= strongBrickStartIndex
                && tileIndex < strongBrickStartIndex + strongBrickCount;
    }

    private int getBrickType(int tileIndex) {

        if(isNormalBrickIndex(tileIndex)) {
            return normalBrickTypeId;
        }
        return strongBrickTypeId;
    }

    public void updateBricks() {

        for(int y = 0; y < brickCountY; y++) {
            for(int x = 0; x < brickCountX; x++) {
                if(bricks[y][x] != null) {
                    Brick currentBrick = bricks[y][x];
                    currentBrick.update();
                }
            }
        }
    }

//    public void renderBricks(Graphics2D graphics2D) {
//
//        for(int y = 0; y < brickCountY; y++) {
//            for(int x = 0; x < brickCountX; x++) {
//                if(bricks[y][x] != null) {
//                    Brick currentBrick = bricks[y][x];
//                    currentBrick.render(graphics2D);
//                }
//            }
//        }
//    }

    public void renderBricks(Graphics2D graphics2D) {
        for (int y = 0; y < brickCountY; y++) {
            for (int x = 0; x < brickCountX; x++) {
                if (bricks[y][x] != null) {
                    Brick currentBrick = bricks[y][x];
                    currentBrick.render(graphics2D);

                    if (currentBrick instanceof NormalBrick normalBrick) {
                        graphics2D.setColor(Color.RED);
                        graphics2D.setFont(new Font("Consolas", Font.PLAIN, 12));

                        // show frames left
                        graphics2D.drawString(
                                "Frames: " + normalBrick.getFrames().size(),
                                normalBrick.getX(),
                                normalBrick.getY() - 5
                        );

                        // show destroyed flag
                        if (normalBrick.isDestroyed()) {
                            graphics2D.drawString(
                                    "X",
                                    normalBrick.getX() + normalBrick.getWidth() / 2,
                                    normalBrick.getY() + normalBrick.getHeight() / 2
                            );
                        }
                    }
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
