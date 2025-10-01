package audio;

import exception.ExceptionHandler;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;

public class Sound {
    private Clip clip;

    public Sound(String path) {
        try {
            URL url = getClass().getResource(path);
            if (url == null) {
                throw new IllegalArgumentException("Sound file not found: " + path);
            }
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(url);
            clip = AudioSystem.getClip();
            clip.open(audioStream);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            ExceptionHandler.handle(e);
        }
    }

    public void play() {
        if (clip == null) {
            return;
        }
        if (clip.isRunning()) {
            clip.stop();
        }
        clip.setFramePosition(0);
        clip.start();
    }

    public void loop() {
        if (clip == null) {
            return;
        }
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    public void stop() {
        if (clip == null) {
            return;
        }
        clip.stop();
    }
}
