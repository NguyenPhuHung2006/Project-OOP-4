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
 * The {@code TextureLoaderUtils} class provides static utility methods for loading and scaling
 * textures used in the game.
 * <p>
 * It is responsible for reading texture files from the classpath, extracting sub-images,
 * and generating scaled versions when needed.
 * </p>
 * <p>
 * If an error occurs during texture loading or scaling, it is handled by {@link ExceptionHandler}
 * through a {@link ResourceLoadException}.
 * </p>
 */
public class TextureLoaderUtils {

    private TextureLoaderUtils() {
    }

    /**
     * Loads a subtexture from an image resource based on the specified coordinates and dimensions.
     *
     * @param textureX      the x-coordinate of the texture region to extract
     * @param textureY      the y-coordinate of the texture region to extract
     * @param textureWidth  the width of the texture region
     * @param textureHeight the height of the texture region
     * @param texturePath   the path to the texture image resource, relative to {@link GameObject}'s classpath
     * @return the loaded subtexture as a {@link BufferedImage}, or {@code null} if loading fails
     */
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

    /**
     * Loads and scales a subtexture from an image resource.
     * <p>
     * This method first extracts the specified subtexture from the full image, then scales it to
     * the given dimensions using {@link Image#SCALE_SMOOTH}.
     * </p>
     *
     * @param textureX       the x-coordinate of the texture region to extract
     * @param textureY       the y-coordinate of the texture region to extract
     * @param textureWidth   the width of the texture region
     * @param textureHeight  the height of the texture region
     * @param texturePath    the path to the texture image resource
     * @param scaledWidth    the target width of the scaled texture
     * @param scaledHeight   the target height of the scaled texture
     * @return the scaled texture as a {@link BufferedImage}, or {@code null} if loading fails
     */
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
