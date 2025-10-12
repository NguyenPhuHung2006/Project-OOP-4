package audio;

import config.LevelData;
import kuusisto.tinysound.TinySound;

import java.util.EnumMap;
import java.util.Map;

public class SoundManager {
    private static SoundManager instance;
    private final Map<SoundEffect, GameSound> sounds = new EnumMap<>(SoundEffect.class);
    private float globalVolume = 1.0f;

    private SoundManager() {
        TinySound.init();
    }

    public static SoundManager getInstance() {
        if (instance == null) {
            instance = new SoundManager();
        }
        return instance;
    }

    public void loadSound(SoundEffect effect, String path) {
        sounds.put(effect, new GameSound(path));
    }

    public void loadFromLevel(LevelData levelData) {
        loadSound(SoundEffect.NORMAL_BRICK, levelData.normalBrickSoundPath);
        loadSound(SoundEffect.STRONG_BRICK, levelData.strongBrickSoundPath);
        setGlobalVolume(0.6f);
    }

    public void play(SoundEffect effect) {
        GameSound sound = sounds.get(effect);
        if (sound != null) {
            sound.play();
        }
    }

    public void stop(SoundEffect effect) {
        GameSound sound = sounds.get(effect);
        if (sound != null) {
            sound.stop();
        }
    }

    public void setVolume(SoundEffect effect, float level) {
        GameSound sound = sounds.get(effect);
        if (sound != null) {
            sound.setVolume(level);
        }
    }

    public void setGlobalVolume(float level) {
        globalVolume = Math.max(0f, Math.min(1f, level));
        for (GameSound sound : sounds.values()) {
            sound.setVolume(globalVolume);
        }
    }

    public void cleanup() {
        TinySound.shutdown();
    }
}
