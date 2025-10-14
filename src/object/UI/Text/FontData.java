package object.UI.Text;

import java.awt.*;

public class FontData {
    public String name;
    public String style;
    public int size;

    public Font toFont() {
        int fontStyle = switch (style.toUpperCase()) {
            case "BOLD" -> Font.BOLD;
            case "ITALIC" -> Font.ITALIC;
            default -> Font.PLAIN;
        };
        return new Font(name, fontStyle, size);
    }
}
