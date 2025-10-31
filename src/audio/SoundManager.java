package audio;

import config.SoundConfig;
import exception.ExceptionHandler;
import exception.InvalidGameStateException;
import kuusisto.tinysound.TinySound;

import java.util.EnumMap;
import java.util.Map;

public class SoundManager {
    private static SoundManager soundManager;
    private final Map<SoundType, GameSound> sounds = new EnumMap<>(SoundType.class);
    private final Map<MusicType, GameMusic> musics = new EnumMap<>(MusicType.class);
    private float globalSoundVolume = 1.0f;
    private float globalMusicVolume = 1.0f;

    private SoundManager() {
        TinySound.init();
    }

    public static SoundManager getInstance() {
        if (soundManager == null) {
            soundManager = new SoundManager();
        }
        return soundManager;
    }


    private void loadSound(SoundType soundType, String path) {
        if (sounds.containsKey(soundType)) {
            ExceptionHandler.handle(new InvalidGameStateException(
                    "The sound type " + soundType + " is loaded twice", null));
            return;
        }
        sounds.put(soundType, new GameSound(path));
    }

    private void loadMusic(MusicType musicType, String path) {
        if (musics.containsKey(musicType)) {
            ExceptionHandler.handle(new InvalidGameStateException(
                    "The music type " + musicType + " is loaded twice", null));
            return;
        }
        musics.put(musicType, new GameMusic(path));
    }

    public void loadFromJson(SoundConfig soundConfig) {
        refreshSounds();
        refreshMusics();

        loadSound(SoundType.NORMAL_BRICK, soundConfig.normalBrickSoundPath);
        loadSound(SoundType.STRONG_BRICK, soundConfig.strongBrickSoundPath);
        loadSound(SoundType.POWERUP_BRICK, soundConfig.powerUpBrickSoundPath);
        loadSound(SoundType.CLICK_BUTTON, soundConfig.clickButtonSoundPath);
        loadSound(SoundType.PLAYER_PADDLE, soundConfig.paddleSoundPath);
        loadSound(SoundType.WINDOW_WALL, soundConfig.wallSoundPath);

        loadMusic(MusicType.PLAY_THEME, soundConfig.playThemeMusicPath);

        setSoundGlobalVolume(0.6f);
        setMusicGlobalVolume(0.3f);
    }


    public void play(SoundType soundType) {
        GameSound sound = sounds.get(soundType);
        if (sound != null) {
            sound.play();
        }
    }

    public void stop(SoundType soundType) {
        GameSound sound = sounds.get(soundType);
        if (sound != null) {
            sound.stop();
        }
    }

    public void setSoundVolume(SoundType soundType, float level) {
        GameSound sound = sounds.get(soundType);
        if (sound != null) {
            sound.setVolume(level);
        }
    }

    public void setSoundGlobalVolume(float level) {
        globalSoundVolume = Math.max(0f, Math.min(1f, level));
        for (GameSound sound : sounds.values()) {
            sound.setVolume(globalSoundVolume);
        }
    }


    public void playMusic(MusicType musicType, boolean loop) {
        GameMusic music = musics.get(musicType);
        if (music != null) {
            if (loop) music.playLoop();
            else music.playOnce();
        }
    }

    public void stopMusic(MusicType musicType) {
        GameMusic music = musics.get(musicType);
        if (music != null) {
            music.stop();
        }
    }

    public void pauseMusic(MusicType musicType) {
        GameMusic music = musics.get(musicType);
        if (music != null) {
            music.pause();
        }
    }

    public void resumeMusic(MusicType musicType) {
        GameMusic music = musics.get(musicType);
        if (music != null) {
            music.resume();
        }
    }

    public void setMusicVolume(MusicType musicType, float level) {
        GameMusic music = musics.get(musicType);
        if (music != null) {
            music.setVolume(level);
        }
    }

    public void setMusicGlobalVolume(float level) {
        globalMusicVolume = Math.max(0f, Math.min(1f, level));
        for (GameMusic music : musics.values()) {
            music.setVolume(globalMusicVolume);
        }
    }

    public void cleanup() {
        sounds.clear();
        musics.clear();
        TinySound.shutdown();
    }

    private void refreshSounds() {
        sounds.clear();
    }

    private void refreshMusics() {
        musics.clear();
    }
}
