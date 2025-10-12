package UI.Text;

import config.LevelData;
import main.GameContext;
import object.Paddle;

import java.awt.*;
import java.util.EnumMap;

public class TextManager {
    private static volatile TextManager textManager;
    private EnumMap<TextType, GameText> texts = new EnumMap<>(TextType.class);

    private TextManager() {
    }

    public static TextManager getInstance() {
        if (textManager == null) {
            synchronized (TextManager.class) {
                if (textManager == null) textManager = new TextManager();
            }
        }
        return textManager;
    }

    public void addText(TextType type, TextData textData) {
        texts.put(type, new GameText(textData, type));
    }

    public GameText getText(TextType type) {
        return texts.get(type);
    }

    public void renderAll(Graphics2D graphics2D) {
        for (GameText text : texts.values()) {
            text.render(graphics2D);
        }
    }

    public void loadFromLevel(LevelData levelData) {
        clear();

        addText(TextType.BRICK_DESTROYED, levelData.brickDestroyedText);
        addText(TextType.GAME_WIN, levelData.winText);
        addText(TextType.GAME_OVER, levelData.loseText);
        addText(TextType.SCORE, levelData.scoreText);
        addText(TextType.PRESS_ENTER, levelData.pressEnterText);
        addText(TextType.PRESS_ESC, levelData.pressExitText);
    }

    public void clear() {
        texts.clear();
    }
}
