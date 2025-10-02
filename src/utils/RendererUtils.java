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

    public static void renderBricks(Graphics2D graphics2D, Brick[][] bricks) {

        int brickCountX = bricks.length;
        int brickCountY = bricks[0].length;

        for(int y = 0; y < brickCountY; y++) {
            for(int x = 0; x < brickCountX; x++) {
                if(bricks[x][y] != null) {
                    Brick currentBrick = bricks[x][y];
                    currentBrick.render(graphics2D);
                }
            }
        }

    }

}
