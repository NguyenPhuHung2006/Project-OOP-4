package audio;

import exception.ExceptionHandler;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class Sound {
    private byte[] audioData;
    private AudioFormat format;
    private float volume = 1.0f;
    private FloatControl volumeControl;

    public Sound(String path) {
        try {
            URL url = getClass().getResource(path);
            if (url == null) {
                throw new IllegalArgumentException("Sound file not found: " + path);
            }

            try (AudioInputStream stream = AudioSystem.getAudioInputStream(url);
                 ByteArrayOutputStream buffer = new ByteArrayOutputStream()) {

                format = stream.getFormat();
                byte[] temp = new byte[4096];
                int bytesRead;
                while ((bytesRead = stream.read(temp)) != -1) {
                    buffer.write(temp, 0, bytesRead);
                }
                audioData = buffer.toByteArray();
            }

        } catch (UnsupportedAudioFileException | IOException e) {
            ExceptionHandler.handle(e);
        }
    }

    public void play() {
        if (audioData == null || format == null) return;

        new Thread(() -> {
            try (AudioInputStream ais = new AudioInputStream(
                    new ByteArrayInputStream(audioData),
                    format,
                    audioData.length / format.getFrameSize())) {

                Clip clip = AudioSystem.getClip();
                clip.open(ais);

                if (clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
                    FloatControl control = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                    float min = control.getMinimum();
                    float max = control.getMaximum();
                    float gain = min + (max - min) * volume;
                    control.setValue(gain);
                }

                clip.start();

                clip.addLineListener(event -> {
                    if (event.getType() == LineEvent.Type.STOP) {
                        clip.close();
                    }
                });

            } catch (Exception e) {
                ExceptionHandler.handle(e);
            }
        }).start();
    }

    public void loop() {
        if (audioData == null || format == null) return;

        new Thread(() -> {
            try (AudioInputStream ais = new AudioInputStream(
                    new ByteArrayInputStream(audioData),
                    format,
                    audioData.length / format.getFrameSize())) {

                Clip clip = AudioSystem.getClip();
                clip.open(ais);

                if (clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
                    FloatControl control = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                    float min = control.getMinimum();
                    float max = control.getMaximum();
                    float gain = min + (max - min) * volume;
                    control.setValue(gain);
                }

                clip.loop(Clip.LOOP_CONTINUOUSLY);

            } catch (Exception e) {
                ExceptionHandler.handle(e);
            }
        }).start();
    }

    public void stop() {

    }

    public void setVolume(float level) {
        this.volume = level;
    }
}
