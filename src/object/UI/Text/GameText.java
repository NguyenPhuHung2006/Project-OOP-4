package object.UI.Text;

import exception.ExceptionHandler;
import exception.InvalidGameStateException;
import object.GameContext;
import object.GameObject;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

public class GameText {
    private String content;
    private transient float x, y;
    private transient Color color;
    private transient Font font;

    private transient GameContext gameContext = GameContext.getInstance();
    private transient TextManager textManager = TextManager.getInstance();

    public GameText(TextData textData, TextType type) {
        this.content = textData.getContent();
        this.color = textData.getColorData().toColor();
        this.font = textData.getFontData().toFont();

        initScreenPosition(type);
    }

    public void render(Graphics2D graphics2D) {
        graphics2D.setFont(font);
        graphics2D.setColor(color);
        graphics2D.drawString(content, x, y);
    }

    public void setContent(String content) {
        this.content = content;
    }

    private void updateFontSizeToWindow(float ratio, int windowHeight) {
        this.font = font.deriveFont(ratio * windowHeight);
    }

    private void initScreenPosition(TextType type) {
        int windowWidth = gameContext.getWindowWidth();
        int windowHeight = gameContext.getWindowHeight();

        switch (type) {
            case BRICK_DESTROYED -> {
                updateFontSizeToWindow(0.05f, windowHeight);
                x = windowWidth / 50.0f;
                y = windowHeight - getHeight() / 5.0f;
            }

            case SCORE -> {
                updateFontSizeToWindow(0.05f, windowHeight);
                if(textManager.getText(TextType.BRICK_DESTROYED) == null) {
                    ExceptionHandler.handle(new InvalidGameStateException(
                            "brick destroyed text should be declared before score text", null));
                }
                GameText brickDestroyedText = textManager.getText(TextType.BRICK_DESTROYED);
                x = brickDestroyedText.getX() + brickDestroyedText.getWidth();
                y = brickDestroyedText.getY();
            }

            case GAME_OVER, GAME_WIN -> {
                updateFontSizeToWindow(0.1f, windowHeight);
                x = (windowWidth - getWidth()) / 2.0f;
                y = (windowHeight + getHeight()) / 2.0f;
            }

            case PRESS_ENTER -> {
                updateFontSizeToWindow(0.05f, windowHeight);
                if(textManager.getText(TextType.GAME_OVER) == null ||
                        textManager.getText(TextType.GAME_WIN) == null) {
                    ExceptionHandler.handle(new InvalidGameStateException(
                            "game over or game win text should be declared before score text", null));
                }
                GameText gameOverText = textManager.getText(TextType.GAME_OVER);
                x = gameOverText.getX();
                y = gameOverText.getY() + getHeight();
            }

            case PRESS_ESC -> {
                updateFontSizeToWindow(0.05f, windowHeight);
                if(textManager.getText(TextType.PRESS_ENTER) == null) {
                    ExceptionHandler.handle(new InvalidGameStateException(
                            "press enter text should be declared before score text", null));
                }
                GameText pressEnterText = textManager.getText(TextType.PRESS_ENTER);
                x = pressEnterText.getX();
                y = pressEnterText.getY() + getHeight();
            }
        }
    }

    public int getWidth() {
        FontRenderContext frc = new FontRenderContext(new AffineTransform(), true, true);
        Rectangle2D bounds = font.getStringBounds(content, frc);
        return (int) bounds.getWidth();
    }

    public int getHeight() {
        FontRenderContext frc = new FontRenderContext(new AffineTransform(), true, true);
        Rectangle2D bounds = font.getStringBounds(content, frc);
        return (int) bounds.getHeight();
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }
}
