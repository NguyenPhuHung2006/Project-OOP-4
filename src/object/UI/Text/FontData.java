package object.UI.Text;

import exception.ExceptionHandler;

import java.awt.*;
import java.io.IOException;
import java.util.Objects;

public class FontData {
    public String fontPath;
    public String style;
    public int size;

    public Font toFont() {
        int fontStyle = switch (style.toUpperCase()) {
            case "BOLD" -> Font.BOLD;
            case "ITALIC" -> Font.ITALIC;
            case "BOLD_ITALIC" -> Font.BOLD | Font.ITALIC;
            default -> Font.PLAIN;
        };

        try {
            Font baseFont = Font.createFont(Font.TRUETYPE_FONT, Objects.requireNonNull(getClass().getResourceAsStream(fontPath)));

            return baseFont.deriveFont(fontStyle, (float) size);

        } catch (IOException | FontFormatException e) {
            ExceptionHandler.handle(e);
            return null;
        }
    }
}
