package audio;

import kuusisto.tinysound.Sound;
import kuusisto.tinysound.TinySound;

public class GameSound {
    private Sound sound;
    private float volume = 1.0f;

    public GameSound(String path) {
        // Load the sound file from resources
        this.sound = TinySound.loadSound(getClass().getResource(path));
    }

    public void play() {
        if (sound != null) sound.play(volume);
    }

    public void stop() {
        if (sound != null) sound.stop();
    }

    public void setVolume(float v) {
        volume = Math.max(0f, Math.min(1f, v)); // clamp 0–1
    }

    public float getVolume() {
        return volume;
    }
}
