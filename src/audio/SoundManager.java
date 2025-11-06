package audio;

import config.SoundConfig;
import exception.ExceptionHandler;
import exception.InvalidGameStateException;
import kuusisto.tinysound.TinySound;

import java.util.EnumMap;
import java.util.Map;

/**
 * Manages all sound and music playback in the game using the TinySound library.
 *
 * <p>The {@code SoundManager} class follows the <b>Singleton</b> pattern and
 * provides centralized control for loading, playing, stopping, pausing, and
 * adjusting volume levels of both {@link GameSound} and {@link GameMusic} objects.</p>
 *
 * <p>All sounds and music tracks are loaded from paths specified in a
 * {@link SoundConfig} object, typically parsed from a JSON configuration file.</p>
 *
 * <p>Example usage:</p>
 * <pre>
 *     SoundManager soundManager = SoundManager.getInstance();
 *     soundManager.loadFromJson(config);
 *     soundManager.play(SoundType.CLICK_BUTTON);
 *     soundManager.playMusic(MusicType.MENU_THEME, true);
 * </pre>
 *
 * @see GameSound
 * @see GameMusic
 * @see SoundType
 * @see MusicType
 * @see SoundConfig
 * @see TinySound
 */
public class SoundManager {

    /** The singleton instance of {@code SoundManager}. */
    private static SoundManager soundManager;

    /** A map storing all game sound effects, keyed by their {@link SoundType}. */
    private final Map<SoundType, GameSound> sounds = new EnumMap<>(SoundType.class);

    /** A map storing all background music tracks, keyed by their {@link MusicType}. */
    private final Map<MusicType, GameMusic> musics = new EnumMap<>(MusicType.class);

    /** Global sound volume (range: 0.0 – 1.0). */
    private float globalSoundVolume = 1.0f;

    /** Global music volume (range: 0.0 – 1.0). */
    private float globalMusicVolume = 1.0f;

    /** Private constructor to enforce Singleton pattern and initialize TinySound. */
    private SoundManager() {
        TinySound.init();
    }

    /**
     * Returns the singleton instance of {@code SoundManager}.
     *
     * @return the shared {@code SoundManager} instance
     */
    public static SoundManager getInstance() {
        if (soundManager == null) {
            soundManager = new SoundManager();
        }
        return soundManager;
    }

    /**
     * Loads a sound effect from the given file path and associates it with a {@link SoundType}.
     *
     * @param soundType the type of sound being loaded
     * @param path the resource path to the sound file
     */
    private void loadSound(SoundType soundType, String path) {
        if (sounds.containsKey(soundType)) {
            ExceptionHandler.handle(new InvalidGameStateException(
                    "The sound type " + soundType + " is loaded twice", null));
            return;
        }
        sounds.put(soundType, new GameSound(path));
    }

    /**
     * Loads a music track from the given file path and associates it with a {@link MusicType}.
     *
     * @param musicType the type of music being loaded
     * @param path the resource path to the music file
     */
    private void loadMusic(MusicType musicType, String path) {
        if (musics.containsKey(musicType)) {
            ExceptionHandler.handle(new InvalidGameStateException(
                    "The music type " + musicType + " is loaded twice", null));
            return;
        }
        musics.put(musicType, new GameMusic(path));
    }

    /**
     * Loads all sounds and music tracks from the given {@link SoundConfig} object.
     *
     * <p>This method automatically refreshes existing sounds and music before reloading.</p>
     *
     * @param soundConfig the configuration containing file paths for sounds and music
     */
    public void loadFromJson(SoundConfig soundConfig) {
        refreshSounds();
        refreshMusics();

        loadSound(SoundType.NORMAL_BRICK, soundConfig.normalBrickSoundPath);
        loadSound(SoundType.STRONG_BRICK, soundConfig.strongBrickSoundPath);
        loadSound(SoundType.POWERUP_BRICK, soundConfig.powerUpBrickSoundPath);
        loadSound(SoundType.CLICK_BUTTON, soundConfig.clickButtonSoundPath);
        loadSound(SoundType.PLAYER_PADDLE, soundConfig.paddleSoundPath);
        loadSound(SoundType.WINDOW_WALL, soundConfig.wallSoundPath);
        loadSound(SoundType.WIN_GAME, soundConfig.winSoundPath);
        loadSound(SoundType.LOSE_GAME, soundConfig.loseSoundPath);

        loadMusic(MusicType.PLAY_THEME, soundConfig.playThemeMusicPath);
        loadMusic(MusicType.MENU_THEME, soundConfig.menuThemeMusicPath);

        setSoundGlobalVolume(0.6f);
        setMusicGlobalVolume(0.3f);
    }

    // --- Sound Methods ---

    /**
     * Plays a sound effect of the specified {@link SoundType}.
     *
     * @param soundType the type of sound to play
     */
    public void play(SoundType soundType) {
        GameSound sound = sounds.get(soundType);
        if (sound != null) {
            sound.play();
        }
    }

    /**
     * Stops a sound effect of the specified {@link SoundType}.
     *
     * @param soundType the type of sound to stop
     */
    public void stop(SoundType soundType) {
        GameSound sound = sounds.get(soundType);
        if (sound != null) {
            sound.stop();
        }
    }

    /**
     * Sets the volume level for a specific sound type.
     *
     * @param soundType the sound to adjust
     * @param level the volume level (0.0–1.0)
     */
    public void setSoundVolume(SoundType soundType, float level) {
        GameSound sound = sounds.get(soundType);
        if (sound != null) {
            sound.setVolume(level);
        }
    }

    /**
     * Sets the global volume for all sound effects.
     *
     * @param level the new global sound volume (0.0–1.0)
     */
    public void setSoundGlobalVolume(float level) {
        globalSoundVolume = Math.max(0f, Math.min(1f, level));
        for (GameSound sound : sounds.values()) {
            sound.setVolume(globalSoundVolume);
        }
    }

    // --- Music Methods ---

    /**
     * Plays a music track of the specified {@link MusicType}.
     *
     * @param musicType the type of music to play
     * @param loop whether the music should loop continuously
     */
    public void playMusic(MusicType musicType, boolean loop) {
        GameMusic music = musics.get(musicType);
        if (music != null) {
            if (loop) {
                music.playLoop();
            }
            else {
                music.playOnce();
            }
        }
    }

    /**
     * Stops a music track of the specified {@link MusicType}.
     *
     * @param musicType the type of music to stop
     */
    public void stopMusic(MusicType musicType) {
        GameMusic music = musics.get(musicType);
        if (music != null) {
            music.stop();
        }
    }

    /**
     * Pauses a currently playing music track.
     *
     * @param musicType the type of music to pause
     */
    public void pauseMusic(MusicType musicType) {
        GameMusic music = musics.get(musicType);
        if (music != null) {
            music.pause();
        }
    }

    /**
     * Resumes playback of a paused music track.
     *
     * @param musicType the type of music to resume
     */
    public void resumeMusic(MusicType musicType) {
        GameMusic music = musics.get(musicType);
        if (music != null) {
            music.resume();
        }
    }

    /**
     * Sets the volume level for a specific music track.
     *
     * @param musicType the music track to adjust
     * @param level the volume level (0.0–1.0)
     */
    public void setMusicVolume(MusicType musicType, float level) {
        GameMusic music = musics.get(musicType);
        if (music != null) {
            music.setVolume(level);
        }
    }

    /**
     * Sets the global volume for all music tracks.
     *
     * @param level the new global music volume (0.0–1.0)
     */
    public void setMusicGlobalVolume(float level) {
        globalMusicVolume = Math.max(0f, Math.min(1f, level));
        for (GameMusic music : musics.values()) {
            music.setVolume(globalMusicVolume);
        }
    }

    // --- Utility Methods ---

    /**
     * Frees all loaded sounds and music, and shuts down the TinySound engine.
     *
     * <p>This should be called when exiting the game or switching contexts
     * to prevent memory leaks.</p>
     */
    public void cleanup() {
        sounds.clear();
        musics.clear();
        TinySound.shutdown();
    }

    /** Clears all loaded sound effects from memory. */
    private void refreshSounds() {
        sounds.clear();
    }

    /** Clears all loaded music tracks from memory. */
    private void refreshMusics() {
        musics.clear();
    }
}
