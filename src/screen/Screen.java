package screen;

import object.GameContext;

import java.awt.*;

public interface Screen {

    int windowWidth = GameContext.getInstance().getWindowWidth();
    int windowHeight = GameContext.getInstance().getWindowHeight();
    int paddingX = GameContext.getInstance().getPaddingX();
    int paddingY = GameContext.getInstance().getPaddingY();

    void init(Screen screen);
    void update();
    void render(Graphics2D graphics2D);
    void onEnter();
    void onExit();
}
