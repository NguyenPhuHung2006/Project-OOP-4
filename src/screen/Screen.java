package screen;

import audio.SoundManager;
import input.MouseManager;
import object.GameContext;

import java.awt.*;

/**
 * Represents a screen in the game
 * <p>
 * Each {@code Screen} is responsible for managing its own update, render,
 * and transition logic. This interface defines the essential lifecycle
 * methods and provides access to global game components such as
 * {@link MouseManager}, {@link SoundManager}, and {@link ScreenManager}.
 */
public interface Screen {

    int windowWidth = GameContext.getInstance().getWindowWidth();
    int windowHeight = GameContext.getInstance().getWindowHeight();
    int paddingX = GameContext.getInstance().getPaddingX();
    int paddingY = GameContext.getInstance().getPaddingY();
    int spacingX = paddingX * 10;
    int spacingY = paddingY * 10;

    MouseManager mouseManager = MouseManager.getInstance();
    SoundManager soundManager = SoundManager.getInstance();
    ScreenManager screenManager = ScreenManager.getInstance();

    void init(Screen screen);
    void update();
    void render(Graphics2D graphics2D);
    void onEnter();
    void onExit();
}
