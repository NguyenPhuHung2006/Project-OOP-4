package config;

import object.brick.NormalBrick;
import object.brick.PowerUpBrick;
import object.brick.StrongBrick;
import object.movable.Ball;
import object.movable.Paddle;
import object.movable.powerup.ExpandPaddleWidthPowerUp;
import object.movable.powerup.SlowBallPowerUp;
import object.movable.powerup.SpeedUpPaddlePowerUp;

public class LevelConfig {
    public Paddle paddle;
    public Ball ball;

    public int[][] brickLayout;

    public NormalBrick normalBrick;
    public StrongBrick strongBrick;
    public PowerUpBrick powerUpBrick;

    public int[] normalBrickTextureIndices;
    public int[] strongBrickTextureIndices;
    public int[] powerUpBrickTextureIndices;

    public int framePerRow;

    public SlowBallPowerUp slowPowerUp;
    public ExpandPaddleWidthPowerUp expandPaddleWidthPowerUp;
    public SpeedUpPaddlePowerUp speedUpPaddlePowerUp;
}
