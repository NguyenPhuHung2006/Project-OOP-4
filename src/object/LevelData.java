package object;

public class LevelData {

    public Paddle paddle;
    public Ball ball;
    public int[][] brickLayout;
    public Background background;

    public NormalBrick normalBrick;
    public StrongBrick strongBrick;

    public int normalBrickTypeId;
    public int strongBrickTypeId;

    public int[] normalBrickTextureIndices;
    public int[] strongBrickTextureIndices;

    public String normalBrickSoundPath;
    public String strongBrickSoundPath;

    public int framePerRow;
}
