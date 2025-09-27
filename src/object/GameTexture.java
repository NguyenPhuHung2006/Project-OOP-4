package object;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public enum GameTexture {

    // these are only used to test by Hung, we can change this once we get further

    DINO("/assets/dino2.png");

    private final String path;
    private final BufferedImage texture;

    GameTexture(String path) {
        this.path = path;
        this.texture = loadTexture(path);
    }

    public String getPath() {
        return path;
    }

    public BufferedImage getTexture() {
        return texture;
    }

    private static BufferedImage loadTexture(String path) {
        try {
            return ImageIO.read(GameTexture.class.getResource(path));
        } catch (IOException | IllegalArgumentException e) {
            e.printStackTrace();
            return null;
        }
    }
}
