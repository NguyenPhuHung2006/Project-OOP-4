package config;

import object.UI.LifeCounter;
import object.brick.NormalBrick;
import object.brick.PowerUpBrick;
import object.brick.StrongBrick;
import object.movable.Ball;
import object.movable.Paddle;
import object.movable.powerup.AddLivePowerUp;
import object.movable.powerup.ExpandPaddlePowerUp;
import object.movable.powerup.SlowBallPowerUp;
import object.movable.powerup.SpeedUpPaddlePowerUp;

/**
 * Defines configuration parameters for a single game level.
 *
 * <p>The {@code LevelConfig} class contains all data required to initialize
 * a level, including object instances, brick layouts, textures, and power-ups.</p>
 *
 * <p>This class can be populated from external configuration files (e.g., JSON)
 * to dynamically load levels without recompiling the game. These are typically loaded from
 *  *  * a JSON configuration file and passed to
 *  {@link object.brick.BrickManager#loadFromJson(LevelConfig)}
 *  {@link object.GameContext#loadFromJson(LevelConfig)}
 *  {@link object.movable.powerup.PowerUpManager#loadFromJson(LevelConfig)}.</p>
 *
 * @see object.movable.Paddle
 * @see object.movable.Ball
 * @see object.brick.NormalBrick
 * @see object.movable.powerup.PowerUp
 */

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
    public AddLivePowerUp addLivePowerUp;
}
