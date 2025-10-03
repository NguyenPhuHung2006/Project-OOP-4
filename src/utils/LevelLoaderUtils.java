package utils;

import com.google.gson.Gson;
import exception.ResourceLoadException;
import main.GameContext;
import object.*;

import java.io.FileReader;
import java.io.IOException;

public class LevelLoaderUtils {
    public static LevelData loadLevelFromJson(String path) throws ResourceLoadException {
        Gson gson = new Gson();
        try (FileReader reader = new FileReader(path)) {
            return gson.fromJson(reader, LevelData.class);
        } catch (IOException e) {
            throw new ResourceLoadException(path, e);
        }
    }

    public static Brick[][] loadBricks(LevelData levelData) {

        int[][] brickLayout = levelData.brickLayout;
        int framePerRow = levelData.framePerRow;

        int tileCountX = brickLayout[0].length;
        int tileCountY = brickLayout.length;

        Brick[][] bricks = new Brick[tileCountY][tileCountX];
        GameContext gameContext = GameContext.getInstance();

        int windowWidth = gameContext.getWindowWidth();
        int windowHeight = gameContext.getWindowHeight();
        int normalBrickTypeId = gameContext.getNormalBrickTypeId();
        int strongBrickTypeId = gameContext.getStrongBrickTypeId();

        int brickWidth = windowWidth / tileCountX;
        int brickHeight = windowHeight / tileCountY;

        gameContext.setTileWidth(brickWidth);
        gameContext.setTileHeight(brickHeight);

        for (int y = 0; y < tileCountY; y++) {
            for (int x = 0; x < tileCountX; x++) {

                if (brickLayout[y][x] < 0)
                    continue;

                int tileIndex = brickLayout[x][y] / framePerRow;

                int currentBrickType = getBrickType(tileIndex, levelData);

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
                    currentBrick = BrickFactory.createNormalBrick((NormalBrick) currentBrick);
                } else {
                    currentBrick = BrickFactory.createStrongBrick((StrongBrick) currentBrick);
                }
                bricks[y][x] = currentBrick;
            }
        }
        return bricks;
    }

    private static int getBrickType(int tileIndex, LevelData levelData) {
        GameContext gameContext = GameContext.getInstance();
        int normalBrickTypeId = gameContext.getNormalBrickTypeId();
        int strongBrickTypeId = gameContext.getStrongBrickTypeId();

        int normalBrickStartIndex = levelData.normalBrickStartIndex;
        int strongBrickStartIndex = levelData.strongBrickStartIndex;

        int normalBrickCount = levelData.normalBrickCount;
        int strongBrickCount = levelData.strongBrickCount;

        if (tileIndex >= normalBrickStartIndex
                && tileIndex < normalBrickStartIndex + normalBrickCount) {
            return normalBrickTypeId;
        }
        return strongBrickTypeId;
    }

}
