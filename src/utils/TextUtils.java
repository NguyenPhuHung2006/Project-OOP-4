package utils;

import exception.ExceptionHandler;
import object.UI.Text.ColorData;
import object.UI.Text.FontData;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.util.Objects;

public final class TextUtils {

    private static final FontRenderContext DEFAULT_FRC =
            new FontRenderContext(new AffineTransform(), true, true);

    private TextUtils() {
    }

    public static int getTextWidth(String text, Font font) {
        if (text == null || font == null) {
            return 0;
        }
        Rectangle2D bounds = font.getStringBounds(text, DEFAULT_FRC);
        return (int) Math.ceil(bounds.getWidth());
    }

    public static int getTextHeight(String text, Font font) {
        if (text == null || font == null) {
            return 0;
        }
        Rectangle2D bounds = font.getStringBounds(text, DEFAULT_FRC);
        return (int) Math.ceil(bounds.getHeight());
    }

    public static Dimension getTextSize(String text, Font font) {
        if (text == null || font == null) {
            return new Dimension(0, 0);
        }
        Rectangle2D bounds = font.getStringBounds(text, DEFAULT_FRC);
        return new Dimension(
                (int) Math.ceil(bounds.getWidth()),
                (int) Math.ceil(bounds.getHeight())
        );
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
