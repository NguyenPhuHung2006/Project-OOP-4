package screen.endscreen;

import audio.SoundType;
import config.PlayerStatusData;
import object.UI.Background;
import object.UI.GameButton;
import object.UI.Text.GameText;
import object.brick.BrickManager;
import screen.Screen;
import screen.ScreenType;
import screen.menuscreen.MenuScreen;
import screen.playscreen.PlayScreen;
import screen.playscreen.SinglePlayerPlayScreen;
import utils.JsonLoaderUtils;

import javax.swing.*;
import java.awt.*;

public abstract class GameEndScreen implements Screen {

    private final GameText gameEndText;
    private final GameButton escapeButton;
    private final GameButton playAgainButton;
    private final Background background;
    private final String playerStatusDataPath;

    public GameEndScreen(Screen screen) {

        init(screen);

        GameEndScreen gameEndScreen = (GameEndScreen) screen;

        gameEndText = new GameText(gameEndScreen.gameEndText);
        escapeButton = new GameButton(gameEndScreen.escapeButton);
        playAgainButton = new GameButton(gameEndScreen.playAgainButton);
        background = new Background(gameEndScreen.background);

        playerStatusDataPath = gameEndScreen.playerStatusDataPath;
    }

    @Override
    public void init(Screen screen) {

        if (!(screen instanceof GameEndScreen gameEndScreen)) {
            return;
        }

        GameText baseGameEndText = gameEndScreen.gameEndText;
        GameButton baseEscapeButton = gameEndScreen.escapeButton;
        GameButton basePlayAgainButton = gameEndScreen.playAgainButton;

        baseGameEndText.updateSizeFromFontData();
        baseGameEndText.center();

        basePlayAgainButton.applyRelativeSize();
        basePlayAgainButton.alignBelow(baseGameEndText);
        basePlayAgainButton.centerHorizontally();

        baseEscapeButton.applyRelativeSize();
        baseEscapeButton.alignBelow(basePlayAgainButton);

    }

    @Override
    public void update() {
        if (!mouseManager.isLeftClicked()) {
            return;
        }

        soundManager.play(SoundType.CLICK_BUTTON);

        if (escapeButton.isClicked(mouseManager)) {
            goToMenu();
        } else if (playAgainButton.isClicked(mouseManager)) {
            playAgain();
        }
    }

    private void playAgain() {

        saveGameStatus();
        screenManager.pop();
        PlayScreen previousPlayScreen = (PlayScreen) screenManager.top();
        previousPlayScreen.setExited(true);
        ScreenType previousLevelId = previousPlayScreen.getLevelId();
        screenManager.pop();
        screenManager.push(previousLevelId);
    }

    private void goToMenu() {

        saveGameStatus();
        while (!(screenManager.top() instanceof MenuScreen)) {
            screenManager.pop();
        }
    }

    @Override
    public void render(Graphics2D graphics2D) {

        background.render(graphics2D);
        gameEndText.render(graphics2D);
        escapeButton.render(graphics2D);
        playAgainButton.render(graphics2D);
    }

    public void saveGameStatus() {

        PlayerStatusData playerStatusData = JsonLoaderUtils.loadFromJson(playerStatusDataPath, PlayerStatusData.class);

        assert playerStatusData != null;

        saveGameResultCount(playerStatusData);

        int numberOfBrickDestroyed = BrickManager.getInstance().getDestroyedBricksCount();
        playerStatusData.numberOfBricksDestroyed += numberOfBrickDestroyed;

        GameEndScreen currentGameEndScreen = (GameEndScreen) screenManager.top();
        screenManager.pop();
        PlayScreen previousPlayScreen = (PlayScreen) screenManager.top();
        long totalTimePlayed = previousPlayScreen.getTotalTimePlayedBeforeExit();

        playerStatusData.totalTimePlayed += totalTimePlayed;

        if(previousPlayScreen instanceof SinglePlayerPlayScreen singlePlayerPlayScreen) {
            String levelSavedPath = singlePlayerPlayScreen.getLevelSavePath();
            JsonLoaderUtils.clearJsonFile(levelSavedPath);
        }

        screenManager.push(currentGameEndScreen);

        JsonLoaderUtils.saveToJson(playerStatusDataPath, playerStatusData);

    }

    protected abstract void saveGameResultCount(PlayerStatusData playerStatusData);
}
