package screen;

import input.MouseManager;
import object.UI.Background;
import object.UI.GameButton;
import object.UI.Text.GameText;

import java.awt.*;

public class SelectScreen implements Screen {

    GameText level1Text;
    GameButton level1Button;
    Background background;

    public SelectScreen(SelectScreen selectScreen) {
        
        init(selectScreen);
        level1Button = new GameButton(selectScreen.level1Button);
        level1Text = new GameText(selectScreen.level1Text);
        background = new Background(selectScreen.background);
    }

    @Override
    public void init(Screen screen) {
        if (!(screen instanceof SelectScreen selectScreen)) {
            return;
        }
        
        GameText baseLevel1Text = selectScreen.level1Text;
        GameButton baseLevel1Button = selectScreen.level1Button;

        baseLevel1Text.updateSizeFromFontData();
        baseLevel1Text.applyRelativePosition();

        baseLevel1Button.applyRelativeSize(baseLevel1Button);
        baseLevel1Button.alignRightOf(baseLevel1Text);

    }

    @Override
    public void update() {
        MouseManager mouseManager = MouseManager.getInstance();

        if(mouseManager.isLeftClicked() && level1Button.isClicked(mouseManager)) {
            ScreenManager screenManager = ScreenManager.getInstance();
            screenManager.push(ScreenType.PLAY_LEVEL1);
        }
    }

    @Override
    public void render(Graphics2D graphics2D) {
        background.render(graphics2D);
        level1Text.render(graphics2D);
        level1Button.render(graphics2D);
    }

    @Override
    public void onEnter() {

    }

    @Override
    public void onExit() {

    }
}
