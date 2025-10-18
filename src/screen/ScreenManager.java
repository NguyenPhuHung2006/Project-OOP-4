package screen;

import config.ScreenConfig;

import java.util.EnumMap;
import java.util.Map;
import java.util.Stack;

public class ScreenManager {
    private final Stack<Screen> screens = new Stack<>();
    Map<ScreenType, Screen> screenRegistry = new EnumMap<>(ScreenType.class);

    private static ScreenManager screenManager;
    private ScreenManager() {}

    public static ScreenManager getInstance() {
        if(screenManager == null) {
            screenManager = new ScreenManager();
        }
        return screenManager;
    }

    public void loadFromJson(ScreenConfig screenConfig) {

    }

    public void push(Screen screen) {
        if (!screens.isEmpty()) {
            screens.peek().onExit();
        }
        screens.push(screen);
        screen.onEnter();
    }

    public void pop() {
        if (!screens.isEmpty()) {
            screens.pop().onExit();
        }
        if (!screens.isEmpty()) {
            screens.peek().onEnter();
        }
    }

    public void update() {
        if (!screens.isEmpty()) {
            screens.peek().update();
        }
    }

    public boolean isEmpty() {
        return screens.isEmpty();
    }

}
