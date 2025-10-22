package screen;

import input.MouseManager;
import object.GameContext;
import object.UI.Background;
import object.UI.GameButton;
import object.UI.Text.GameText;

import java.awt.*;

public class MenuScreen implements Screen {

    GameText level1Text;
    GameButton level1Button;

    GameText level2Text;
    GameButton level2Button;

    Background background;

    public MenuScreen(Screen screen) {
        
        init(screen);

        MenuScreen menuScreen = (MenuScreen) screen;

        level1Button = new GameButton(menuScreen.level1Button);
        level1Text = new GameText(menuScreen.level1Text);

        level2Button = new GameButton(menuScreen.level2Button);
        level2Text = new GameText(menuScreen.level2Text);

        background = new Background(menuScreen.background);
    }

    @Override
    public void init(Screen screen) {
        if (!(screen instanceof MenuScreen menuScreen)) {
            return;
        }
        
        GameText baseLevel1Text = menuScreen.level1Text;
        GameButton baseLevel1Button = menuScreen.level1Button;

        GameText baseLevel2Text = menuScreen.level2Text;
        GameButton baseLevel2Button = menuScreen.level2Button;

        baseLevel1Text.updateSizeFromFontData();
        baseLevel1Text.applyRelativePosition();

        baseLevel1Button.applyRelativeSize();
        baseLevel1Button.alignRightOf(baseLevel1Text);
        baseLevel1Button.centerVerticallyTo(baseLevel1Text);

        baseLevel2Text.updateSizeFromFontData();
        baseLevel2Text.alignBelow(baseLevel1Text);
        baseLevel2Text.translateY(paddingY * 10);

        baseLevel2Button.applyRelativeSize();
        baseLevel2Button.alignRightOf(baseLevel2Text);
        baseLevel2Button.centerVerticallyTo(baseLevel2Text);

    }

    @Override
    public void update() {
        MouseManager mouseManager = MouseManager.getInstance();

        if(mouseManager.isLeftClicked()) {
            ScreenManager screenManager = ScreenManager.getInstance();
            if(level1Button.isClicked(mouseManager)) {
                screenManager.push(ScreenType.PLAY_LEVEL1);
            }
            else if(level2Button.isClicked(mouseManager)) {
                screenManager.push(ScreenType.PLAY_LEVEL2);
            }
        }
    }

    @Override
    public void render(Graphics2D graphics2D) {
        background.render(graphics2D);
        level1Text.render(graphics2D);
        level1Button.render(graphics2D);
        level2Text.render(graphics2D);
        level2Button.render(graphics2D);
    }

    @Override
    public void onEnter() {

    }

    @Override
    public void onExit() {

    }
}
