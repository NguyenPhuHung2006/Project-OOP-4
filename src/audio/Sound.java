package audio;

import exception.ExceptionHandler;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;

public class Sound {
    private Clip clip;
    private FloatControl volumeControl;

    public Sound(String path) {
        try {
            URL url = getClass().getResource(path);
            if (url == null) {
                throw new IllegalArgumentException("Sound file not found: " + path);
            }
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(url);
            clip = AudioSystem.getClip();
            clip.open(audioStream);

            if (clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
                volumeControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            }
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

    public void setVolume(float level) {
        if (volumeControl == null) return;
        level = Math.max(0f, Math.min(level, 1f)); // giới hạn 0–1

        float min = volumeControl.getMinimum(); // thường khoảng -80 dB
        float max = volumeControl.getMaximum(); // thường khoảng 6 dB
        float gain = (max - min) * level + min; // nội suy
        volumeControl.setValue(gain);
    }
}
