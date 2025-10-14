package utils;

import java.awt.*;
import java.awt.image.BufferedImage;

public class RendererUtils {

    private RendererUtils() {
    }

    public static void render(BufferedImage texture, float x, float y, Graphics2D graphics2D) {
        if (texture != null && graphics2D != null) {
            graphics2D.drawImage(texture, (int) x, (int) y, null);
        }
    }

    public static void render(BufferedImage texture, float x, float y, int scaledWidth, int scaledHeight, Graphics2D graphics2D) {
        if (texture != null && graphics2D != null) {
            graphics2D.drawImage(texture, (int) x, (int) y, scaledWidth, scaledHeight, null);
        }
    }
}
