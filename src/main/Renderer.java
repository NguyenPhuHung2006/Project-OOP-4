package main;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Renderer {
    public static void render(BufferedImage texture, Graphics2D graphics2D, int x, int y) {
        if (texture != null && graphics2D != null) {
            graphics2D.drawImage(texture, x, y, null);
        }
    }

    public static void render(BufferedImage texture, Graphics2D graphics, int x, int y, int width, int height) {
        if (texture != null && graphics != null) {
            graphics.drawImage(texture, x, y, width, height, null);
        }
    }
}
