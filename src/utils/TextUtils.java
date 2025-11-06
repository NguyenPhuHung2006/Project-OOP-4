package utils;

import exception.ExceptionHandler;
import object.UI.Text.ColorData;
import object.UI.Text.FontData;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * A utility class providing helper methods for text measurement, font conversion,
 * and color manipulation within the game's UI system.
 * <p>
 * This class simplifies tasks such as computing text dimensions, creating {@link Font}
 * objects from custom font data, converting color data to {@link Color}, and formatting
 * time values.
 * </p>
 * <p>
 * This class cannot be instantiated.
 * </p>
 */
public final class TextUtils {

    /**
     * The default {@link FontRenderContext} used for calculating text layout and dimensions.
     * It uses antialiasing and fractional metrics for smoother text rendering.
     */
    private static final FontRenderContext DEFAULT_FRC =
            new FontRenderContext(new AffineTransform(), true, true);

    private TextUtils() {
    }

    /**
     * Calculates the width in pixels of a given text string using the specified font.
     *
     * @param text the text to measure
     * @param font the {@link Font} used to render the text
     * @return the width of the text in pixels, or {@code 0} if text or font is {@code null}
     */
    public static int getTextWidth(String text, Font font) {
        if (text == null || font == null) {
            return 0;
        }
        TextLayout layout = new TextLayout(text, font, DEFAULT_FRC);
        return (int) Math.ceil(layout.getAdvance());
    }

    /**
     * Calculates the height in pixels of a given text string using the specified font.
     *
     * @param text the text to measure
     * @param font the {@link Font} used to render the text
     * @return the height of the text in pixels, or {@code 0} if text or font is {@code null}
     */
    public static int getTextHeight(String text, Font font) {
        if (text == null || font == null) {
            return 0;
        }
        TextLayout layout = new TextLayout(text, font, DEFAULT_FRC);
        Rectangle pixelBounds = layout.getPixelBounds(DEFAULT_FRC, 0f, 0f);
        return pixelBounds.height;
    }

    /**
     * Calculates the size (width and height) of a given text string using the specified font.
     *
     * @param text the text to measure
     * @param font the {@link Font} used to render the text
     * @return a {@link Dimension} containing the width and height in pixels;
     *         returns {@code (0, 0)} if text or font is {@code null}
     */
    public static Dimension getTextSize(String text, Font font) {
        if (text == null || font == null) {
            return new Dimension(0, 0);
        }
        TextLayout layout = new TextLayout(text, font, DEFAULT_FRC);
        Rectangle pixelBounds = layout.getPixelBounds(DEFAULT_FRC, 0f, 0f);
        int width = (int) Math.ceil(layout.getAdvance());
        int height = pixelBounds.height;
        return new Dimension(width, height);
    }

    /**
     * Converts a {@link FontData} object to a {@link Font} instance.
     * <p>
     * Loads a TrueType font from the specified path, applies the given style
     * and size, and returns the resulting {@link Font}.
     * </p>
     *
     * @param fontData the {@link FontData} containing font path, size, and style
     * @return the created {@link Font}, or {@code null} if the font could not be loaded
     */
    public static Font toFont(FontData fontData) {
        int fontStyle = switch (fontData.style.toUpperCase()) {
            case "BOLD" -> Font.BOLD;
            case "ITALIC" -> Font.ITALIC;
            case "BOLD_ITALIC" -> Font.BOLD | Font.ITALIC;
            default -> Font.PLAIN;
        };

        try {
            Font baseFont = Font.createFont(Font.TRUETYPE_FONT, Objects.requireNonNull(TextUtils.class.getResourceAsStream(fontData.fontPath)));

            return baseFont.deriveFont(fontStyle, (float) fontData.size);

        } catch (IOException | FontFormatException e) {
            ExceptionHandler.handle(e);
            return null;
        }
    }

    /**
     * Converts a duration in milliseconds to a formatted time string
     * in the format {@code "HH:MM:SS"}.
     *
     * @param millis the duration in milliseconds
     * @return a formatted time string representing hours, minutes, and seconds
     */
    public static String convertMillisToTimeUnit(long millis) {

        long hours = TimeUnit.MILLISECONDS.toHours(millis);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(millis) % 60;
        long seconds = TimeUnit.MILLISECONDS.toSeconds(millis) % 60;

        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    /**
     * Derives a new font scaled proportionally based on the window height and ratio.
     *
     * @param ratio        the scaling ratio relative to the window height
     * @param windowHeight the height of the window, used as a scaling reference
     * @param originalFont the base {@link Font} to scale
     * @return a new {@link Font} derived from the original with adjusted size
     */
    public static Font derivedFont(float ratio, int windowHeight, Font originalFont) {
        return originalFont.deriveFont(ratio * windowHeight);
    }

    /**
     * Converts a {@link ColorData} object to a {@link Color} instance.
     *
     * @param colorData the {@link ColorData} containing RGBA values
     * @return a {@link Color} object representing the specified RGBA values
     */
    public static Color toColor(ColorData colorData) {
        int r = colorData.r;
        int g = colorData.g;
        int b = colorData.b;
        int a = colorData.a;
        return new Color(r, g, b, a);
    }
}
