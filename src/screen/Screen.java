package screen;

import java.awt.*;

public interface Screen {
    void init(Screen screen);
    void update();
    void render(Graphics2D graphics2D);
    void onEnter();
    void onExit();
}
