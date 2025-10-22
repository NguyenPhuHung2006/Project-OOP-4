package screen;

import input.MouseManager;
import object.UI.Background;
import object.UI.GameButton;
import object.UI.Text.GameText;

import java.awt.*;

public class StartScreen implements Screen {
    
    private GameButton playButton;
    private GameText title;
    private Background background;

    public StartScreen(StartScreen startScreen) {
        init(startScreen);
        playButton = new GameButton(startScreen.playButton);
        title = new GameText(startScreen.title);
        background = new Background(startScreen.background);
    }

    @Override
    public void init(Screen screen) {
        if (!(screen instanceof StartScreen startScreen)) {
            return;
        }

        GameButton basePlayButton = startScreen.playButton;
        GameText baseTitleText = startScreen.title;

        baseTitleText.updateSizeFromFontData();
        baseTitleText.center();

        basePlayButton.applyRelativeSize(basePlayButton);
        basePlayButton.alignBelow(baseTitleText);
        basePlayButton.centerHorizontallyTo(baseTitleText);

    }

    @Override
    public void update() {

        MouseManager mouseManager = MouseManager.getInstance();

        if(mouseManager.isLeftClicked() && playButton.isClicked(mouseManager)) {
            ScreenManager screenManager = ScreenManager.getInstance();
            screenManager.push(ScreenType.MENU);
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
