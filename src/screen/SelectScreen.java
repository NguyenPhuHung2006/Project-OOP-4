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

        GameContext gameContext = GameContext.getInstance();
        int windowWidth = gameContext.getWindowWidth();
        int windowHeight = gameContext.getWindowHeight();
        
        GameText baseLevel1Text = selectScreen.level1Text;
        GameButton baseLevel1Button = selectScreen.level1Button;

        Font baseFont = TextUtils.toFont(baseLevel1Text.getFontData());
        if (baseFont == null) {
            ExceptionHandler.handle(new InvalidGameStateException("Can't load the level 1 text font", null));
            return;
        }

        Font level1TextFont = TextUtils.derivedFont(baseLevel1Text.getRelativeSize(), windowHeight, baseFont);
        Dimension level1TextSize = TextUtils.getTextSize(baseLevel1Text.getContent(), level1TextFont);

        baseLevel1Text.setFont(level1TextFont);
        baseLevel1Text.setWidth(level1TextSize.width);
        baseLevel1Text.setHeight(level1TextSize.height);
        baseLevel1Text.setX(windowWidth * baseLevel1Text.getRelativeX());
        baseLevel1Text.setY(windowHeight * baseLevel1Text.getRelativeY());

        float level1ButtonHeight = baseLevel1Text.getHeight();
        float level1ButtonWidth = baseLevel1Button.getTextureWidth() * level1ButtonHeight / baseLevel1Button.getTextureHeight();

        baseLevel1Button.setWidth(level1ButtonWidth);
        baseLevel1Button.setHeight(level1ButtonHeight);
        baseLevel1Button.setX(baseLevel1Text.getX() + baseLevel1Text.getWidth());
        baseLevel1Button.setY(baseLevel1Text.getY() - baseLevel1Text.getHeight());
    }

    @Override
    public void update() {
        MouseManager mouseManager = MouseManager.getInstance();

        if(level1Button.isClicked(mouseManager)) {
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
