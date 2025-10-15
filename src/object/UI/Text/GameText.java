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
    private transient TextType type;

    public GameText(TextData textData, TextType type) {
        super(null);

        this.content = textData.getContent();
        this.color = textData.getColorData().toColor();
        this.font = textData.getFontData().toFont();
        this.type = type;

        initBounds(null);
    }

    @Override
    public void update() {
        // No logic yet — placeholder for animation or dynamic updates
    }

    @Override
    public void render(Graphics2D g2d) {
        g2d.setFont(font);
        g2d.setColor(color);
        g2d.drawString(content, x, y);
    }

    @Override
    protected void initBounds(GameObject gameObject) {
        int windowWidth = gameContext.getWindowWidth();
        int windowHeight = gameContext.getWindowHeight();

        switch (type) {
            case BRICK_DESTROYED -> {
                updateFontAndBounds(0.05f, windowHeight);
                x = windowWidth / 50.0f;
                y = windowHeight - height / 5.0f;
            }

            case SCORE -> {
                updateFontAndBounds(0.05f, windowHeight);
                GameText destroyedText = getRequiredText(TextType.BRICK_DESTROYED,
                        "brick destroyed text should be declared before score text");
                x = destroyedText.getX() + destroyedText.getWidth();
                y = destroyedText.getY();
            }

            case GAME_OVER, GAME_WIN -> {
                updateFontAndBounds(0.1f, windowHeight);
                x = (windowWidth - width) / 2.0f;
                y = (windowHeight + height) / 2.0f;
            }

            case PRESS_ENTER -> {
                updateFontAndBounds(0.05f, windowHeight);
                GameText gameOverText = getRequiredText(TextType.GAME_OVER,
                        "game over or game win text should be declared before press enter text");
                x = gameOverText.getX();
                y = gameOverText.getY() + height;
            }

            case PRESS_ESC -> {
                updateFontAndBounds(0.05f, windowHeight);
                GameText pressEnterText = getRequiredText(TextType.PRESS_ENTER,
                        "press enter text should be declared before press esc text");
                x = pressEnterText.getX();
                y = pressEnterText.getY() + height;
            }
        }
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

    /**
     * Retrieves a required text object or throws a handled exception if missing.
     */
    private GameText getRequiredText(TextType textType, String errorMessage) {
        GameText text = textManager.getText(textType);
        if (text == null) {
            ExceptionHandler.handle(new InvalidGameStateException(errorMessage, null));
        }
        return text;
    }


    public void setContent(String content) {
        this.content = content;
        updateTextBounds(); // Recalculate width when content changes
    }

    public TextType getType() {
        return type;
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
