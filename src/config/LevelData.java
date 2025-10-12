package config;

import UI.Text.TextData;
import object.*;

public class LevelData {
    public Paddle paddle;
    public Ball ball;
    public Background background;

    public int[][] brickLayout;

    public NormalBrick normalBrick;
    public StrongBrick strongBrick;

    public int normalBrickTypeId;
    public int strongBrickTypeId;

    public int[] normalBrickTextureIndices;
    public int[] strongBrickTextureIndices;

    public String normalBrickSoundPath;
    public String strongBrickSoundPath;

    public int framePerRow;

    public TextData brickDestroyedText;
    public TextData loseText;
    public TextData winText;
    public TextData pressEnterText;
    public TextData pressExitText;
    public TextData scoreText;

}
