package screen;

import audio.SoundType;
import object.UI.Background;
import object.UI.GameButton;
import object.UI.Text.GameText;

import javax.swing.*;
import java.awt.*;

public abstract class GameEndScreen implements Screen {

    private GameText gameEndText;
    private GameButton escapeButton;
    private GameButton playAgainButton;
    private GameButton saveProgressButton;
    private Background background;
    private boolean hasSavedGameProgress;

    public GameEndScreen(Screen screen) {

        init(screen);

        GameEndScreen gameEndScreen = (GameEndScreen) screen;

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

        basePlayAgainButton.applyRelativeSize();
        basePlayAgainButton.alignBelow(baseGameEndText);
        basePlayAgainButton.centerHorizontally();

        baseEscapeButton.applyRelativeSize();
        baseEscapeButton.alignRightOf(basePlayAgainButton);

        baseSaveProgressButton.applyRelativeSize();
        baseSaveProgressButton.alignLeftOf(basePlayAgainButton);

    }

    @Override
    public void update() {

        if(mouseManager.isLeftClicked()) {
            soundManager.play(SoundType.CLICK_BUTTON);

            if(escapeButton.isClicked(mouseManager)) {
                if(hasSavedGameProgress) {
                    goToMenu();
                    return;
                }
                int option = JOptionPane.showConfirmDialog(
                        null,
                        "Your game process will not be saved",
                        "WARNING",
                        JOptionPane.OK_CANCEL_OPTION
                );
                if(option == JOptionPane.OK_OPTION) {
                    goToMenu();
                }
            } else if(playAgainButton.isClicked(mouseManager)) {
                if(hasSavedGameProgress) {
                    playAgain();
                    return;
                }
                int option = JOptionPane.showConfirmDialog(
                        null,
                        "Your game status will not be saved. Please save the game progress first",
                        "WARNING",
                        JOptionPane.OK_CANCEL_OPTION
                );
                if(option == JOptionPane.OK_OPTION) {
                    playAgain();
                }
            } else if(saveProgressButton.isClicked(mouseManager)) {
                saveGameStatus();
                hasSavedGameProgress = true;
            }
        }
    }

    private void playAgain() {
        screenManager.pop();
        PlayScreen previousPlayScreen = (PlayScreen) screenManager.top();
        ScreenType previousLevelId = previousPlayScreen.getLevelId();
        screenManager.pop();
        screenManager.push(previousLevelId);
    }

    private void goToMenu() {
        screenManager.pop();
        screenManager.pop();
    }

    @Override
    public void render(Graphics2D graphics2D) {

        background.render(graphics2D);
        gameEndText.render(graphics2D);
        escapeButton.render(graphics2D);
        playAgainButton.render(graphics2D);
        saveProgressButton.render(graphics2D);
    }

    public abstract void saveGameStatus();

    @Override
    public void onEnter() {

    }

    @Override
    public void onExit() {

    }
}
