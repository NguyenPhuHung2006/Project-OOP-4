package screen;

import exception.ExceptionHandler;
import exception.InvalidGameStateException;
import input.MouseManager;
import object.GameContext;
import object.UI.Background;
import object.UI.GameButton;
import object.UI.Text.GameText;
import utils.TextUtils;

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

        GameContext context = GameContext.getInstance();
        int windowWidth = context.getWindowWidth();
        int windowHeight = context.getWindowHeight();

        Font baseFont = TextUtils.toFont(menuScreen.title.getFontData());
        if (baseFont == null) {
            ExceptionHandler.handle(new InvalidGameStateException("Can't load the title font", null));
            return;
        }

        Font titleFont = TextUtils.derivedFont(menuScreen.title.getRelativeSize(), windowHeight, baseFont);
        Dimension titleSize = TextUtils.getTextSize(menuScreen.title.getContent(), titleFont);

        menuScreen.title.setFont(titleFont);
        menuScreen.title.setWidth(titleSize.width);
        menuScreen.title.setHeight(titleSize.height);
        menuScreen.title.setX((windowWidth - titleSize.width) / 2f);
        menuScreen.title.setY((windowHeight + titleSize.height) / 2f);

        float buttonWidth = windowWidth * menuScreen.playButton.getRelativeSize();
        float buttonHeight = menuScreen.playButton.getTextureHeight() * buttonWidth / menuScreen.playButton.getTextureWidth();

        menuScreen.playButton.setWidth(buttonWidth);
        menuScreen.playButton.setHeight(buttonHeight);
        menuScreen.playButton.setX((windowWidth - buttonWidth) / 2f);
        menuScreen.playButton.setY(menuScreen.title.getY());
    }

    @Override
    public void update() {

        MouseManager mouseManager = MouseManager.getInstance();

        if(playButton.isClicked(mouseManager)) {
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
