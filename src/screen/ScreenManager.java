package screen;

import config.ScreenConfig;
import exception.ExceptionHandler;
import exception.InvalidGameStateException;

import java.awt.*;
import java.util.EnumMap;
import java.util.Map;
import java.util.Stack;

public class ScreenManager {
    private final Stack<Screen> screens = new Stack<>();
    Map<ScreenType, Screen> screenRegistry = new EnumMap<>(ScreenType.class);

    private static ScreenManager screenManager;

    private ScreenManager() {
    }

    public static ScreenManager getInstance() {
        if (screenManager == null) {
            screenManager = new ScreenManager();
        }
        return screenManager;
    }

    public void loadFromJson(ScreenConfig screenConfig) {
        screenRegistry.put(ScreenType.START, screenConfig.startScreen);
        screenRegistry.put(ScreenType.MENU, screenConfig.menuScreen);
        screenRegistry.put(ScreenType.PLAY_LEVEL1, screenConfig.playLevel1Screen);
        screenRegistry.put(ScreenType.PLAY_LEVEL2, screenConfig.playLevel2Screen);
        screenRegistry.put(ScreenType.PLAY_LEVEL3, screenConfig.playLevel3Screen);
        screenRegistry.put(ScreenType.PAUSE, screenConfig.pauseScreen);
        screenRegistry.put(ScreenType.GAME_OVER, screenConfig.gameOverScreen);
        screenRegistry.put(ScreenType.GAME_WIN, screenConfig.gameWinScreen);
        screenRegistry.put(ScreenType.PLAYER_STATUS, screenConfig.playerStatusScreen);
    }

    public void push(ScreenType screenType) {
        if (!screens.isEmpty()) {
            screens.peek().onExit();
        }

        Screen baseScreen = screenRegistry.get(screenType);

        if (baseScreen == null) {
            ExceptionHandler.handle(new InvalidGameStateException("the screen: " + screenType + " has not been registered", null));
        }

        Screen newScreen = screenType.create(baseScreen);

        if (newScreen instanceof PlayScreen playScreen) {
            if (playScreen.isExited()) {
                return;
            }
        }

        screens.push(newScreen);
        newScreen.onEnter();
    }

    public void push(Screen screen) {
        screens.push(screen);
    }

    public void pop() {
        if (!screens.isEmpty()) {
            screens.pop().onExit();
        }
        if (!screens.isEmpty()) {
            screens.peek().onEnter();
        }
    }

    public Screen top() {
        if (screens.isEmpty()) {
            return null;
        }
        return screens.peek();
    }

    public void update() {
        if (!screens.isEmpty()) {
            screens.peek().update();
        }
    }

    public void render(Graphics2D graphics2D) {
        if (!screens.isEmpty()) {
            screens.peek().render(graphics2D);
        }
    }

    public boolean isEmpty() {
        return screens.isEmpty();
    }

}
