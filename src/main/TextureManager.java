package main;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

public final class TextureManager {

    public static BufferedImage loadTexture(String path) {
        try {
            return ImageIO.read(TextureManager.class.getResource(path));
        } catch (IOException | IllegalArgumentException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static BufferedImage loadTexture(String path, int width, int height) {
        BufferedImage original = loadTexture(path);
        if (original == null) {
            return null;
        }

        BufferedImage scaledImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics2D = scaledImage.createGraphics();

        graphics2D.drawImage(original.getScaledInstance(width, height, Image.SCALE_SMOOTH), 0, 0, null);
        graphics2D.dispose();

        return scaledImage;
    }
}
