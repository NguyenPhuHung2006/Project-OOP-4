package screen;

import config.ScreenConfig;
import exception.ExceptionHandler;
import exception.InvalidGameStateException;
import screen.playscreen.MultiPlayerPlayScreen;
import screen.playscreen.PlayScreen;

import java.awt.*;
import java.util.EnumMap;
import java.util.Map;
import java.util.Stack;

/**
 * Manages all game screens and transitions between them.
 * <p>
 * The {@code ScreenManager} maintains a stack of active screens and
 * provides methods to push, pop, or replace them. It also handles
 * initialization of all available screens from a {@link ScreenConfig}.
 * <p>
 * This class is implemented as a singleton, accessible via {@link #getInstance()}.
 */
public class ScreenManager {

    /** Stack of active screens (top is the current screen). */
    private final Stack<Screen> screens = new Stack<>();
    Map<ScreenType, Screen> screenRegistry = new EnumMap<>(ScreenType.class);

    private static ScreenManager screenManager;

    private ScreenManager() {
    }

    /**
     * Returns the singleton instance of the {@code ScreenManager}.
     *
     * @return the global screen manager instance
     */
    public static ScreenManager getInstance() {
        if (screenManager == null) {
            screenManager = new ScreenManager();
        }
        return screenManager;
    }

    /**
     * Loads screen configurations from a {@link ScreenConfig} object.
     * <p>
     * Registers all predefined screens in the {@link #screenRegistry}.
     *
     * @param screenConfig the screen configuration loaded from JSON
     */
    public void loadFromJson(ScreenConfig screenConfig) {

        screenRegistry.put(ScreenType.START, screenConfig.startScreen);
        screenRegistry.put(ScreenType.MENU, screenConfig.menuScreen);

        screenRegistry.put(ScreenType.PLAY_LEVEL1, screenConfig.playLevel1Screen);
        screenRegistry.put(ScreenType.PLAY_LEVEL2, screenConfig.playLevel2Screen);
        screenRegistry.put(ScreenType.PLAY_LEVEL3, screenConfig.playLevel3Screen);
        screenRegistry.put(ScreenType.MULTI_PLAYER, screenConfig.multiPlayerPlayScreen);

        screenRegistry.put(ScreenType.SINGLE_PLAYER_PAUSE, screenConfig.pauseScreen);
        screenRegistry.put(ScreenType.MULTIPLE_PLAYER_PAUSE, screenConfig.pauseScreen);

        screenRegistry.put(ScreenType.GAME_OVER, screenConfig.gameOverScreen);
        screenRegistry.put(ScreenType.GAME_WIN, screenConfig.gameWinScreen);
        screenRegistry.put(ScreenType.PLAYER_STATUS, screenConfig.playerStatusScreen);
    }

    /**
     * Pushes a new screen of the given type onto the stack.
     * <p>
     * The current screen’s {@link Screen#onExit()} method is called before pushing the new screen.
     *
     * @param screenType the type of screen to activate
     */
    public void push(ScreenType screenType) {
        if (!screens.isEmpty()) {
            screens.peek().onExit();
        }

        Screen baseScreen = screenRegistry.get(screenType);

        if (baseScreen == null) {
            ExceptionHandler.handle(new InvalidGameStateException("the screen: " + screenType + " has not been registered", null));
        }

        Screen newScreen = screenType.create(baseScreen);

        if ((newScreen instanceof PlayScreen playScreen && playScreen.isExited()) ||
                (newScreen instanceof MultiPlayerPlayScreen multiPlayerPlayScreen && multiPlayerPlayScreen.isExited())) {
            screens.peek().onEnter();
            return;
        }

        screens.push(newScreen);
        newScreen.onEnter();
    }

    /**
     * Pushes an already constructed screen instance onto the stack.
     *
     * @param screen the screen to push
     */
    public void push(Screen screen) {
        screens.push(screen);
    }

    /**
     * Pops the top screen from the stack.
     * <p>
     * Calls {@link Screen#onExit()} on the removed screen and
     * {@link Screen#onEnter()} on the new top screen.
     */
    public void pop() {
        if (!screens.isEmpty()) {
            screens.pop().onExit();
        }
        if (!screens.isEmpty()) {
            screens.peek().onEnter();
        }
    }

    /**
     * Returns the currently active screen (top of the stack).
     *
     * @return the top screen, or {@code null} if no screens exist
     */
    public Screen top() {
        if (screens.isEmpty()) {
            return null;
        }
        return screens.peek();
    }

    /**
     * Updates the active screen by calling its {@link Screen#update()} method.
     */
    public void update() {
        if (!screens.isEmpty()) {
            screens.peek().update();
        }
    }

    /**
     * Renders the active screen using the provided graphics context.
     *
     * @param graphics2D the {@link Graphics2D} context for rendering
     */
    public void render(Graphics2D graphics2D) {
        if (!screens.isEmpty()) {
            screens.peek().render(graphics2D);
        }
    }

    /**
     * Checks whether there are no active screens.
     *
     * @return {@code true} if the stack is empty; otherwise {@code false}
     */
    public boolean isEmpty() {
        return screens.isEmpty();
    }

}
