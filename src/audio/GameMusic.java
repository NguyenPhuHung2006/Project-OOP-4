package audio;

import kuusisto.tinysound.Music;
import kuusisto.tinysound.TinySound;

public class GameMusic {
    private Music music;
    private double volume = 1.0;

    public GameMusic(String path) {
        music = TinySound.loadMusic(getClass().getResource(path));
    }

    public void playLoop() {
        if (music != null) music.play(true, volume); // loop=true
    }

    public void stop() {
        if (music != null) music.stop();
    }

    public void setVolume(double v) {
        volume = Math.max(0.0, Math.min(1.0, v));
        if (music != null) music.setVolume(volume);
    }
}

