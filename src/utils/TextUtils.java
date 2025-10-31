package utils;

import exception.ExceptionHandler;
import object.UI.Text.ColorData;
import object.UI.Text.FontData;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public final class TextUtils {

    private static final FontRenderContext DEFAULT_FRC =
            new FontRenderContext(new AffineTransform(), true, true);

    private TextUtils() {
    }

    public static int getTextWidth(String text, Font font) {
        if (text == null || font == null) {
            return 0;
        }
        TextLayout layout = new TextLayout(text, font, DEFAULT_FRC);
        return (int) Math.ceil(layout.getAdvance());
    }

    public static int getTextHeight(String text, Font font) {
        if (text == null || font == null) {
            return 0;
        }
        TextLayout layout = new TextLayout(text, font, DEFAULT_FRC);
        Rectangle pixelBounds = layout.getPixelBounds(DEFAULT_FRC, 0f, 0f);
        return pixelBounds.height;
    }

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

    public static String convertMillisToTimeUnit(long millis) {

        long hours = TimeUnit.MILLISECONDS.toHours(millis);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(millis) % 60;
        long seconds = TimeUnit.MILLISECONDS.toSeconds(millis) % 60;

        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    public static Font derivedFont(float ratio, int windowHeight, Font originalFont) {
        return originalFont.deriveFont(ratio * windowHeight);
    }

    public static Color toColor(ColorData colorData) {
        int r = colorData.r;
        int g = colorData.g;
        int b = colorData.b;
        int a = colorData.a;
        return new Color(r, g, b, a);
    }
}
