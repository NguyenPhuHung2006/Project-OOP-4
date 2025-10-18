package audio;

import config.LevelData;
import exception.ExceptionHandler;
import exception.InvalidGameStateException;
import kuusisto.tinysound.TinySound;

import java.util.EnumMap;
import java.util.Map;

public class SoundManager {
    private static SoundManager soundManager;
    private final Map<SoundType, GameSound> sounds = new EnumMap<>(SoundType.class);
    private float globalVolume = 1.0f;

    private SoundManager() {
        TinySound.init();
    }

    public static SoundManager getInstance() {
        if (soundManager == null) {
            soundManager = new SoundManager();
        }
        return soundManager;
    }

    public void loadSound(SoundType soundType, String path) {

        if(sounds.containsKey(soundType)) {
            ExceptionHandler.handle(new InvalidGameStateException("the sound type " + soundType + " is loaded twice", null));
        }
        sounds.put(soundType, new GameSound(path));
    }

    public void loadFromJson(LevelData levelData) {

        refreshSounds();
        loadSound(SoundType.NORMAL_BRICK, levelData.normalBrickSoundPath);
        loadSound(SoundType.STRONG_BRICK, levelData.strongBrickSoundPath);
        loadSound(SoundType.POWERUP_BRICK, levelData.powerUpBrickSoundPath);
        setGlobalVolume(0.6f);
    }

    public void play(SoundType effect) {
        GameSound sound = sounds.get(effect);
        if (sound != null) {
            sound.play();
        }
    }

    public void stop(SoundType effect) {
        GameSound sound = sounds.get(effect);
        if (sound != null) {
            sound.stop();
        }
    }

    public void setVolume(SoundType effect, float level) {
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

    private void refreshSounds() {
        sounds.clear();
    }
}
