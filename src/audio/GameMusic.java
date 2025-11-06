package audio;

import kuusisto.tinysound.Music;
import kuusisto.tinysound.TinySound;

/**
 * Represents background music used in the game, managed by the TinySound library.
 *
 * <p>The {@code GameMusic} class is responsible for loading, playing, pausing,
 * resuming, stopping, and adjusting the volume of background music tracks.</p>
 *
 * <p>Unlike {@link GameSound}, this class is designed for longer looping audio files,
 * such as menu or gameplay themes.</p>
 *
 * <p>Example usage:</p>
 * <pre>
 *     GameMusic theme = new GameMusic("/sounds/MusicMenu.wav");
 *     theme.playLoop();  // Play background music continuously
 * </pre>
 *
 * @see MusicType
 * @see TinySound
 * @see GameSound
 * @see kuusisto.tinysound.Music
 * @see kuusisto.tinysound.TinySound
 */
public class GameMusic {

    /** The TinySound {@code Music} instance representing this track. */
    private Music music;

    /** The playback volume (range: 0.0 – 1.0). */
    private double volume = 1.0;

    /**
     * Loads a music track from the given file path.
     *
     * <p>The path should be a valid resource within the classpath
     * (for example, {@code "/sounds/play_theme.wav"}).</p>
     *
     * @param path the resource path to the music file
     */
    public GameMusic(String path) {
        music = TinySound.loadMusic(getClass().getResource(path));
    }

    /**
     * Plays the music track in a continuous loop.
     *
     * <p>If the track is already playing, this call restarts it.</p>
     */
    public void playLoop() {
        if (music != null) {
            music.play(true, volume);
        }
    }

    /**
     * Plays the music track once (without looping).
     *
     * <p>This is typically used for short cutscene or transition music.</p>
     */
    public void playOnce() {
        if (music != null) {
            music.play(false, volume);
        }
    }

    /**
     * Stops the music playback immediately.
     *
     * <p>If the track is currently paused or playing, it will be halted and reset.</p>
     */
    public void stop() {
        if (music != null) {
            music.stop();
        }
    }

    /**
     * Pauses the currently playing music.
     *
     * <p>The playback can be resumed later using {@link #resume()}.</p>
     */
    public void pause() {
        if (music != null) {
            music.pause();
        }
    }

    /**
     * Resumes playback of a paused music track.
     */
    public void resume() {
        if (music != null) {
            music.resume();
        }
    }

    /**
     * Sets the playback volume for this music track.
     *
     * <p>The volume value is clamped between {@code 0.0} (mute)
     * and {@code 1.0} (maximum). The change takes effect immediately
     * if the music is currently playing.</p>
     *
     * @param v the desired volume level
     */
    public void setVolume(double v) {
        volume = Math.max(0.0, Math.min(1.0, v));
        if (music != null) {
            music.setVolume(volume);
        }
    }
}
