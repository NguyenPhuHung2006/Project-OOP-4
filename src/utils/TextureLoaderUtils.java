package utils;

import exception.ExceptionHandler;
import exception.ResourceLoadException;
import object.GameObject;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.RasterFormatException;
import java.io.IOException;
import java.util.Objects;

/**
 * this class will help me load the texture
 */
public class TextureLoaderUtils {

    private TextureLoaderUtils() {
    }

    public static BufferedImage loadTexture(int textureX, int textureY, int textureWidth, int textureHeight,
                                            String texturePath) {
        try {
            BufferedImage fullImage = ImageIO.read(Objects.requireNonNull(GameObject.class.getResource(texturePath)));

            return fullImage.getSubimage(textureX, textureY, textureWidth, textureHeight);

        } catch (IOException | NullPointerException | RasterFormatException e) {
            ExceptionHandler.handle(new ResourceLoadException(texturePath, e));
            return null;
        }
    }

    public static BufferedImage scaleTexture(int textureX, int textureY, int textureWidth, int textureHeight,
                                             String texturePath,
                                             float scaledWidth, float scaledHeight) {

        BufferedImage originalTexture;

        originalTexture = loadTexture(textureX, textureY, textureWidth, textureHeight, texturePath);

        BufferedImage scaledTexture = new BufferedImage((int)scaledWidth, (int)scaledHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics2D = scaledTexture.createGraphics();

        try {
            assert originalTexture != null;
            graphics2D.drawImage(originalTexture.getScaledInstance((int)scaledWidth, (int)scaledHeight, Image.SCALE_SMOOTH), 0, 0, null);
        } catch (NullPointerException e) {
            ExceptionHandler.handle(e);
        }
        graphics2D.dispose();

        return scaledTexture;
    }
}
