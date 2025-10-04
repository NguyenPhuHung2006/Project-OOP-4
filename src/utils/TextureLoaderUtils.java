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

public class TextureLoaderUtils {

    private TextureLoaderUtils() {}

    public static BufferedImage loadTexture(int textureX, int textureY, int textureWidth, int textureHeight,
                                        String texturePath) throws ResourceLoadException {
        try {
            BufferedImage fullImage = ImageIO.read(Objects.requireNonNull(GameObject.class.getResource(texturePath)));

            return fullImage.getSubimage(textureX, textureY, textureWidth, textureHeight);

        } catch (IOException | NullPointerException | RasterFormatException e) {
            throw new ResourceLoadException(texturePath, e);
        }
    }

    public static BufferedImage scaleTexture(int textureX, int textureY, int textureWidth, int textureHeight,
                                         String texturePath,
                                         int scaledWidth, int scaledHeight) {

        BufferedImage originalTexture;

        try {
            originalTexture = loadTexture(textureX, textureY, textureWidth, textureHeight, texturePath);
        } catch (ResourceLoadException e) {
            ExceptionHandler.handle(e);
            return null;
        }

        BufferedImage scaledTexture = new BufferedImage(scaledWidth, scaledHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics2D = scaledTexture.createGraphics();

        graphics2D.drawImage(originalTexture.getScaledInstance(scaledWidth, scaledHeight, Image.SCALE_SMOOTH), 0, 0, null);
        graphics2D.dispose();

        return scaledTexture;
    }
}
