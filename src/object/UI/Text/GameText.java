package object.UI.Text;

import exception.ExceptionHandler;
import exception.InvalidGameStateException;
import object.GameObject;
import utils.TextUtils;

import java.awt.*;

public class GameText extends GameObject {

    private String content;
    private transient Color color;
    private transient Font font;
    private transient float ratio;

    private ColorData colorData;
    private FontData fontData;

    public GameText(GameText gameText) {
        super(null);

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

    @Override
    public void render(Graphics2D graphics2D) {
        
        graphics2D.setFont(font);
        graphics2D.setColor(color);
        graphics2D.drawString(content, x, y + height);
    }

    @Override
    protected void initBounds(GameObject gameObject) {

        x = gameObject.getX();
        y = gameObject.getY();
        width = gameObject.getWidth();
        height = gameObject.getHeight();
    }

    @Override
    public void serializeToJson() {
        relativeX = x / windowWidth;
        relativeY = y / windowHeight;
    }

    @Override
    public void deserializeFromJson() {
        x = relativeX * windowWidth;
        y = relativeY * windowHeight;

        font = TextUtils.toFont(fontData);
        color = TextUtils.toColor(colorData);

    }

    public void updateFontAndBounds(float ratio) {
        font = TextUtils.derivedFont(ratio, windowHeight, font);
        updateTextBounds();
    }

    private void updateTextBounds() {
        Dimension size = TextUtils.getTextSize(content, font);
        width = size.width;
        height = size.height;
    }

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
