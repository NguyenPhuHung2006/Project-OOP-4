package utils;

import object.Brick;

import java.awt.*;
import java.awt.image.BufferedImage;

public class RendererUtils {

    public static void render(BufferedImage texture, int x, int y, Graphics2D graphics2D) {
        if (texture != null && graphics2D != null) {
            graphics2D.drawImage(texture, x, y, null);
        }
    }

    public static void render(BufferedImage texture, int x, int y, int scaledWidth, int scaledHeight, Graphics2D graphics2D) {
        if (texture != null && graphics2D != null) {
            graphics2D.drawImage(texture, x, y, scaledWidth, scaledHeight, null);
        }
    }
}
