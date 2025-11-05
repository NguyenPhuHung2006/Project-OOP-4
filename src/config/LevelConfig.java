package config;

import object.UI.LifeCounter;
import object.brick.NormalBrick;
import object.brick.PowerUpBrick;
import object.brick.StrongBrick;
import object.movable.Ball;
import object.movable.Paddle;
import object.movable.powerup.ExpandPaddlePowerUp;
import object.movable.powerup.SlowBallPowerUp;
import object.movable.powerup.SpeedUpPaddlePowerUp;

public class LevelConfig {
    public Paddle paddle;
    public Ball ball;
    public LifeCounter lifeCounter;

    public int[][] brickLayout;

    public NormalBrick normalBrick;
    public StrongBrick strongBrick;
    public PowerUpBrick powerUpBrick;

    public int[] normalBrickTextureIndices;
    public int[] strongBrickTextureIndices;
    public int[] powerUpBrickTextureIndices;

    public int framePerRow;

    public int destroyInterval;

    public SlowBallPowerUp slowPowerUp;
    public ExpandPaddlePowerUp expandPaddlePowerUp;
    public SpeedUpPaddlePowerUp speedUpPaddlePowerUp;
}
