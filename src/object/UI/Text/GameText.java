package object.UI.Text;

import exception.ExceptionHandler;
import exception.InvalidGameStateException;
import object.GameObject;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

public class GameText extends GameObject {

    private String content;
    private transient Color color;
    private transient Font font;

    private TextData textData;

    public GameText(TextData textData) {
        super(null);

        this.color = textData.getColorData().toColor();
        this.font = textData.getFontData().toFont();
    }

    @Override
    public void update() {

    }

    @Override
    public void render(Graphics2D g2d) {
        g2d.setFont(font);
        g2d.setColor(color);
        g2d.drawString(content, x, y);
    }

    @Override
    protected void initBounds(GameObject gameObject) {

        x = gameObject.getX();
        y = gameObject.getY();
        width = gameObject.getWidth();
        height = gameObject.getHeight();
    }

    /**
     * Updates the font size based on window height and recalculates text bounds.
     */
    private void updateFontAndBounds(float ratio, int windowHeight) {
        font = font.deriveFont(ratio * windowHeight);
        updateTextBounds();
    }

    /**
     * Recalculates width and height using the current font and content.
     */
    private void updateTextBounds() {
        FontRenderContext frc = new FontRenderContext(new AffineTransform(), true, true);
        Rectangle2D bounds = font.getStringBounds(content, frc);
        width = (int) bounds.getWidth();
        height = (int) bounds.getHeight();
    }


    public void setContent(String content) {
        this.content = content;
        updateTextBounds(); // Recalculate width when content changes
    }

    public Color getColor() {
        return color;
    }

    public Font getFont() {
        return font;
    }

    public String getContent() {
        return content;
    }
}
