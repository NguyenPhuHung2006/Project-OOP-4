package main;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Renderer {
    public static void render(BufferedImage texture, Graphics graphics, int x, int y) {
        if (texture != null && graphics != null) {
            graphics.drawImage(texture, x, y, null);
        }
    }

    public static void render(BufferedImage texture, Graphics graphics, int x, int y, int width, int height) {
        if (texture != null && graphics != null) {
            graphics.drawImage(texture, x, y, width, height, null);
        }
    }
}
