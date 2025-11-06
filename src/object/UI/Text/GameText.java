package object.UI.Text;

import exception.ExceptionHandler;
import exception.InvalidGameStateException;
import object.GameObject;
import utils.TextUtils;

import java.awt.*;

/**
 * Represents a text element in the game UI.
 * <p>
 * The {@code GameText} class handles font rendering, sizing, color,
 * and dynamic scaling relative to the game window.
 */
public class GameText extends GameObject {

    private String content;
    private transient Color color;
    private transient Font font;

    private ColorData colorData;
    private FontData fontData;

    /**
     * Constructs a {@code GameText} instance by copying another one.
     *
     * @param gameText the game text object to copy
     */
    public GameText(GameText gameText) {
        super(gameText);

        initBounds(gameText);

        this.content = gameText.getContent();

        this.font = gameText.font;
        this.color = TextUtils.toColor(gameText.getColorData());

        colorData = gameText.colorData;
        fontData = gameText.fontData;
    }

    @Override
    public void update() {

    }

    /**
     * Renders the text to the screen using the provided graphics context.
     *
     * @param graphics2D the {@link Graphics2D} context used for drawing
     */
    @Override
    public void render(Graphics2D graphics2D) {
        
        graphics2D.setFont(font);
        graphics2D.setColor(color);
        graphics2D.drawString(content, x, y + height);
    }

    /**
     * Initializes position and size based on another {@link GameObject}.
     *
     * @param gameObject the reference object
     */
    @Override
    protected void initBounds(GameObject gameObject) {

        x = gameObject.getX();
        y = gameObject.getY();
        width = gameObject.getWidth();
        height = gameObject.getHeight();
    }

    /** Serializes position data relative to window size. */
    @Override
    public void serializeToJson() {
        relativeX = x / windowWidth;
        relativeY = y / windowHeight;
    }

    /** Deserializes text data and restores font, color, and size. */
    @Override
    public void deserializeFromJson() {
        x = relativeX * windowWidth;
        y = relativeY * windowHeight;

        font = TextUtils.toFont(fontData);
        color = TextUtils.toColor(colorData);
        updateSizeFromFontData();
    }

    /**
     * Scales the font and updates bounds based on the given ratio.
     *
     * @param ratio the scaling ratio relative to window height
     */
    public void updateFontAndBounds(float ratio) {
        font = TextUtils.derivedFont(ratio, windowHeight, font);
        updateTextBounds();
    }

    /** Recalculates width and height based on the current font and content. */
    private void updateTextBounds() {
        Dimension size = TextUtils.getTextSize(content, font);
        width = size.width;
        height = size.height;
    }

    /**
     * Updates font and size using {@link FontData}.
     * <p>
     * Handles exceptions if font loading fails.
     */
    public void updateSizeFromFontData() {

        Font baseFont = TextUtils.toFont(fontData);
        if(baseFont == null) {
            ExceptionHandler.handle(new InvalidGameStateException("Failed to load font", null));
            return;
        }

        Font derivedFont = TextUtils.derivedFont(relativeSize, windowHeight, baseFont);
        Dimension textSize = TextUtils.getTextSize(content, derivedFont);

        font = derivedFont;
        width = textSize.width;
        height = textSize.height;
    }

    public void setContent(String content) {
        this.content = content;
        updateTextBounds();
    }

    public void setFont(Font font) {
        this.font = font;
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

    public ColorData getColorData() {
        return colorData;
    }

    public FontData getFontData() {
        return fontData;
    }
}
