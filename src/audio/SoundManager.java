package audio;

import java.util.EnumMap;
import java.util.Map;

public class SoundManager {
    private static SoundManager soundManager;
    private final Map<SoundEffect, Sound> sounds = new EnumMap<>(SoundEffect.class);

    private SoundManager() {
    }

    public void loadSound(SoundEffect effect, String path) {
        sounds.put(effect, new Sound(path));
    }

    public static SoundManager getInstance() {
        if (soundManager == null) {
            soundManager = new SoundManager();
        }
        return soundManager;
    }

    public void play(SoundEffect effect) {
        Sound sound = sounds.get(effect);
        if (sound != null) {
            sound.play();
        }
    }

    public void loop(SoundEffect effect) {
        Sound sound = sounds.get(effect);
        if (sound != null) {
            sound.loop();
        }
    }

    public void stop(SoundEffect effect) {
        Sound sound = sounds.get(effect);
        if (sound != null) {
            sound.stop();
        }
    }

    public Sound getSound(SoundEffect effect) {
        return sounds.get(effect);
    }

    public void setVolume(SoundEffect effect, float level) {
        Sound sound = sounds.get(effect);
        if (sound != null) {
            sound.setVolume(level);
        }
    }

    public void setGlobalVolume(float level) {
        for (Sound sound : sounds.values()) {
            sound.setVolume(level);
        }
    }
}
