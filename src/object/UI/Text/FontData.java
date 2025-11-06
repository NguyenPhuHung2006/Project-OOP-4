package object.UI.Text;

/**
 * Contains font metadata for text rendering.
 * <p>
 * Used to reconstruct a {@link java.awt.Font} during deserialization.
 */
public class FontData {
    public String fontPath;
    public String style;
    public int size;
}
