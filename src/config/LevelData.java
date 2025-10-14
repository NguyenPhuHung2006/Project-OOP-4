package config;

import object.*;
import object.UI.Background;
import object.UI.Text.TextData;
import object.brick.NormalBrick;
import object.brick.PowerUpBrick;
import object.brick.StrongBrick;
import object.movable.Ball;
import object.movable.Paddle;

public class LevelData {
    public Paddle paddle;
    public Ball ball;
    public Background background;

    public int[][] brickLayout;

    public NormalBrick normalBrick;
    public StrongBrick strongBrick;
    public PowerUpBrick powerUpBrick;

    public int normalBrickTypeId;
    public int strongBrickTypeId;
    public int powerUpBrickTypeId;

    public int[] normalBrickTextureIndices;
    public int[] strongBrickTextureIndices;
    public int[] powerUpBrickTextureIndices;

    public String normalBrickSoundPath;
    public String strongBrickSoundPath;
    public String powerUpBrickSoundPath;

    public int framePerRow;

    public TextData brickDestroyedText;
    public TextData loseText;
    public TextData winText;
    public TextData pressEnterText;
    public TextData pressExitText;
    public TextData scoreText;

}
