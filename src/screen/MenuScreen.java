package screen;

import input.MouseManager;
import object.UI.Background;
import object.UI.GameButton;
import object.UI.Text.GameText;

import java.awt.*;

public class MenuScreen implements Screen {
    
    private GameButton playButton;
    private GameText title;
    private Background background;

    public MenuScreen(MenuScreen menuScreen) {
        init(menuScreen);
        playButton = new GameButton(menuScreen.playButton);
        title = new GameText(menuScreen.title);
        background = new Background(menuScreen.background);
    }

    @Override
    public void init(Screen screen) {
        if (!(screen instanceof MenuScreen menuScreen)) {
            return;
        }

        GameButton basePlayButton = menuScreen.playButton;
        GameText baseTitleText = menuScreen.title;

        baseTitleText.updateSizeFromFontData();
        baseTitleText.center();

        basePlayButton.applyRelativeSize(basePlayButton);
        basePlayButton.alignBelow(baseTitleText);
        basePlayButton.centerHorizontally();

    }

    @Override
    public void update() {

        MouseManager mouseManager = MouseManager.getInstance();

        if(mouseManager.isLeftClicked() && playButton.isClicked(mouseManager)) {
            ScreenManager screenManager = ScreenManager.getInstance();
            screenManager.push(ScreenType.SELECT);
        }
    }

    @Override
    public void render(Graphics2D graphics2D) {
        background.render(graphics2D);
        title.render(graphics2D);
        playButton.render(graphics2D);
    }

    @Override
    public void onEnter() {}

    @Override
    public void onExit() {}

}
