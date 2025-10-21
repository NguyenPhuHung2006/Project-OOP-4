package screen;

import exception.ExceptionHandler;
import exception.InvalidGameStateException;
import object.GameContext;
import object.UI.Background;
import object.UI.GameButton;
import object.UI.Text.GameText;
import utils.TextUtils;

import java.awt.*;

public abstract class GameEndScreen implements Screen {

    private GameText gameEndText;
    private GameButton escapeButton;
    private GameButton playAgainButton;
    private GameButton saveProgressButton;
    private Background background;

    public GameEndScreen(GameEndScreen gameEndScreen) {

        init(gameEndScreen);

        gameEndText = new GameText(gameEndScreen.gameEndText);

    }

    @Override
    public void init(Screen screen) {

        if (!(screen instanceof GameEndScreen gameEndScreen)) {
            return;
        }

        GameContext gameContext = GameContext.getInstance();
        int windowWidth = gameContext.getWindowWidth();
        int windowHeight = gameContext.getWindowHeight();

        GameText baseGameEndText = gameEndScreen.gameEndText;
        GameButton baseEscapeButton = gameEndScreen.escapeButton;
        GameButton basePlayAgainButton = gameEndScreen.playAgainButton;
        GameButton baseSaveProgressButton = gameEndScreen.saveProgressButton;

        Font baseGameEndFont = TextUtils.toFont(baseGameEndText.getFontData());

        if (baseGameEndFont == null) {
            ExceptionHandler.handle(new InvalidGameStateException("Can't load the game end text font", null));
            return;
        }

        Font gameEndFont = TextUtils.derivedFont(baseGameEndText.getRelativeSize(), windowHeight, baseGameEndFont);
        Dimension gameEndSize = TextUtils.getTextSize(baseGameEndText.getContent(), gameEndFont);

        baseGameEndText.setFont(gameEndFont);
        baseGameEndText.setWidth(gameEndSize.width);
        baseGameEndText.setHeight(gameEndSize.height);
        baseGameEndText.setX((windowWidth - baseGameEndText.getWidth()) / 2f);
        baseGameEndText.setY((windowHeight + baseGameEndText.getHeight()) / 2f);

        baseEscapeButton.setWidth(windowWidth * baseEscapeButton.getRelativeSize());
        baseEscapeButton.setHeight(baseEscapeButton.getTextureHeight() * baseEscapeButton.getWidth() / baseEscapeButton.getTextureWidth());
        baseEscapeButton.setX((windowWidth - baseEscapeButton.getWidth()) / 2f);

    }

    @Override
    public void update() {

    }

    @Override
    public void render(Graphics2D graphics2D) {
        gameEndText.render(graphics2D);
    }

    @Override
    public void onEnter() {

    }

    @Override
    public void onExit() {

    }
}
