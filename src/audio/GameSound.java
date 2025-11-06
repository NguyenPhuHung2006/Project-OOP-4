package audio;

import kuusisto.tinysound.Sound;
import kuusisto.tinysound.TinySound;

/**
 * Represents a sound effect in the game, managed using the TinySound library.
 *
 * <p>The {@code GameSound} class is responsible for loading, playing,
 * stopping, and controlling the volume of short sound effects such as
 * brick hits, paddle bounces, or button clicks.</p>
 *
 * <p>Example usage:</p>
 * <pre>
 *     GameSound clickSound = new GameSound("/sounds/clickButton.wav");
 *     clickSound.play();
 * </pre>
 *
 * @see SoundType
 * @see TinySound
 */
public class GameSound {

    /** The TinySound sound instance associated with this game sound. */
    private Sound sound;

    /** The playback volume (range: 0.0 – 1.0). */
    private float volume = 1.0f;

    /**
     * Loads a sound from the given file path.
     *
     * <p>The path should be a valid resource within the game's classpath
     * (for example, {@code "/sounds/clickButton.wav"}).</p>
     *
     * @param path the resource path to the sound file
     */
    public GameSound(String path) {
        this.sound = TinySound.loadSound(getClass().getResource(path));
    }

    /**
     * Plays the sound at the current volume.
     *
     * <p>If the sound is already playing, this will trigger another instance
     * to play concurrently.</p>
     */
    public void play() {
        if (sound != null) {
            sound.play(volume);
        }
    }

    /**
     * Stops the sound playback.
     *
     * <p>If the sound is looping or currently playing, it will be halted immediately.</p>
     */
    public void stop() {
        if (sound != null) {
            sound.stop();
        }
    }

    /**
     * Sets the playback volume for this sound.
     *
     * <p>The volume is clamped between {@code 0.0f} (mute)
     * and {@code 1.0f} (maximum).</p>
     *
     * @param v the desired volume level
     */
    public void setVolume(float v) {
        volume = Math.max(0f, Math.min(1f, v)); // clamp 0–1
    }

    /**
     * Returns the current volume of this sound.
     *
     * @return the playback volume between {@code 0.0f} and {@code 1.0f}
     */
    public float getVolume() {
        return volume;
    }
}
