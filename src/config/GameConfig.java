package config;

/**
 * Stores global configuration settings for the game window and resource paths.
 *
 * <p>The {@code GameConfig} class defines the overall game setup,
 * such as window size and file paths to other configuration files.</p>
 *
 * <p>It is typically loaded at startup to initialize the game environment.</p>
 *
 * @see config.ScreenConfig
 * @see config.SoundConfig
 */

public class GameConfig {

    public int windowWidth;
    public int windowHeight;
    public String screenConfigPath;
    public String soundConfigPath;
}
