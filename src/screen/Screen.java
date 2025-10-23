package screen;

import audio.SoundManager;
import input.MouseManager;
import object.GameContext;

import java.awt.*;

public interface Screen {

    int windowWidth = GameContext.getInstance().getWindowWidth();
    int windowHeight = GameContext.getInstance().getWindowHeight();
    int paddingX = GameContext.getInstance().getPaddingX();
    int paddingY = GameContext.getInstance().getPaddingY();

    MouseManager mouseManager = MouseManager.getInstance();
    SoundManager soundManager = SoundManager.getInstance();
    ScreenManager screenManager = ScreenManager.getInstance();

    void init(Screen screen);
    void update();
    void render(Graphics2D graphics2D);
    void onEnter();
    void onExit();
}
