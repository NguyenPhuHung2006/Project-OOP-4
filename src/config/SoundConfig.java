package config;


/**
 * Represents the configuration for all sound and music resources in the game.
 *
 * <p>The {@code SoundConfig} class stores file paths for both short sound effects
 * (such as brick hits or button clicks) and long background music tracks
 * (such as menu or gameplay themes). These paths are typically loaded from
 * a JSON configuration file and passed to {@link audio.SoundManager#loadFromJson(SoundConfig)}.</p>
 *
 *
 * @see audio.SoundManager
 * @see audio.GameSound
 * @see audio.GameMusic
 */

public class SoundConfig {
    public String normalBrickSoundPath;
    public String strongBrickSoundPath;
    public String powerUpBrickSoundPath;
    public String clickButtonSoundPath;
    public String paddleSoundPath;
    public String wallSoundPath;
    public String winSoundPath;
    public String loseSoundPath;

    public String playThemeMusicPath;
    public String menuThemeMusicPath;
}

