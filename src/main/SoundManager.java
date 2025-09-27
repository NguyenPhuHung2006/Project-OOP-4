package main;

import java.util.EnumMap;
import java.util.Map;

public class SoundManager {
    private static SoundManager soundManager;
    private final Map<SoundEffect, Sound> sounds = new EnumMap<>(SoundEffect.class);

    private SoundManager() {
    }

    public void loadSound(SoundEffect effect) {
        sounds.put(effect, new Sound(effect.getPath()));
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
}
