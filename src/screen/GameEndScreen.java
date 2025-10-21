package screen;

import object.UI.Background;
import object.UI.GameButton;
import object.UI.Text.GameText;

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
        escapeButton = new GameButton(gameEndScreen.escapeButton);
        playAgainButton = new GameButton(gameEndScreen.playAgainButton);
        saveProgressButton = new GameButton(gameEndScreen.saveProgressButton);
        background = new Background(gameEndScreen.background);

    }

    @Override
    public void init(Screen screen) {

        if (!(screen instanceof GameEndScreen gameEndScreen)) {
            return;
        }

        GameText baseGameEndText = gameEndScreen.gameEndText;
        GameButton baseEscapeButton = gameEndScreen.escapeButton;
        GameButton basePlayAgainButton = gameEndScreen.playAgainButton;
        GameButton baseSaveProgressButton = gameEndScreen.saveProgressButton;

        baseGameEndText.updateSizeFromFontData();
        baseGameEndText.center();

        basePlayAgainButton.applyRelativeSize(basePlayAgainButton);
        basePlayAgainButton.alignBelow(baseGameEndText);
        basePlayAgainButton.centerHorizontally();

        baseEscapeButton.applyRelativeSize(baseEscapeButton);
        baseEscapeButton.alignRightOf(basePlayAgainButton);

        baseSaveProgressButton.applyRelativeSize(baseSaveProgressButton);
        baseSaveProgressButton.alignLeftOf(basePlayAgainButton);

    }

    @Override
    public void update() {

    }

    @Override
    public void render(Graphics2D graphics2D) {

        background.render(graphics2D);
        gameEndText.render(graphics2D);
        escapeButton.render(graphics2D);
        playAgainButton.render(graphics2D);
        saveProgressButton.render(graphics2D);
    }

    @Override
    public void onEnter() {

    }

    @Override
    public void onExit() {

    }
}
