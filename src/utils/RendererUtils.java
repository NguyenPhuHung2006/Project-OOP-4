package utils;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * The {@code RendererUtils} class provides helper methods for rendering
 * {@link BufferedImage} textures on a {@link Graphics2D} context.
 * <p>
 * This utility class centralizes common rendering logic used throughout the
 * application to maintain code readability and reduce redundancy.
 * </p>
 *
 * <p>
 * It is declared as a non-instantiable utility class with a private constructor.
 * </p>
 */
public class RendererUtils {

    private RendererUtils() {
    }

    /**
     * Renders a given texture at the specified position using the provided {@link Graphics2D} context.
     *
     * @param texture     the texture image to render; if {@code null}, the method does nothing
     * @param x           the x-coordinate where the texture should be drawn
     * @param y           the y-coordinate where the texture should be drawn
     * @param graphics2D  the {@link Graphics2D} rendering context; if {@code null}, the method does nothing
     */
    public static void render(BufferedImage texture, float x, float y, Graphics2D graphics2D) {
        if (texture != null && graphics2D != null) {
            graphics2D.drawImage(texture, (int) x, (int) y, null);
        }
    }

    /**
     * Renders a given texture at the specified position and scales it to the provided dimensions.
     *
     * @param texture       the texture image to render; if {@code null}, the method does nothing
     * @param x             the x-coordinate where the texture should be drawn
     * @param y             the y-coordinate where the texture should be drawn
     * @param scaledWidth   the width to scale the texture to
     * @param scaledHeight  the height to scale the texture to
     * @param graphics2D    the {@link Graphics2D} rendering context; if {@code null}, the method does nothing
     */
    public static void render(BufferedImage texture, float x, float y, int scaledWidth, int scaledHeight, Graphics2D graphics2D) {
        if (texture != null && graphics2D != null) {
            graphics2D.drawImage(texture, (int) x, (int) y, scaledWidth, scaledHeight, null);
        }
    }
}
