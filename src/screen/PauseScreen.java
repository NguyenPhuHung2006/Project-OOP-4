package screen;

import audio.SoundType;
import object.GameContext;
import object.UI.Background;
import object.UI.GameButton;
import object.UI.Text.GameText;
import object.brick.BrickManager;
import object.movable.powerup.PowerUpManager;
import utils.JsonLoaderUtils;

import javax.swing.*;
import java.awt.*;

public class PauseScreen implements Screen {

    private GameText pauseText;
    private GameButton resumeButton;
    private GameButton playAgainButton;
    private GameButton escapeButton;
    private Background background;
    private boolean hasSavedGameProgress;

    public PauseScreen(Screen screen) {

        init(screen);

        PauseScreen pauseScreen = (PauseScreen) screen;

        pauseText = new GameText(pauseScreen.pauseText);
        resumeButton = new GameButton(pauseScreen.resumeButton);
        playAgainButton = new GameButton(pauseScreen.playAgainButton);
        escapeButton = new GameButton(pauseScreen.escapeButton);
        background = new Background(pauseScreen.background);
    }

    @Override
    public void init(Screen screen) {
        if (!(screen instanceof PauseScreen pauseScreen)) {
            return;
        }

        GameText basePauseText = pauseScreen.pauseText;
        GameButton baseResumeButton = pauseScreen.resumeButton;
        GameButton basePlayAgainButton = pauseScreen.playAgainButton;
        GameButton baseEscapeButton = pauseScreen.escapeButton;

        basePauseText.updateSizeFromFontData();
        basePauseText.center();

        baseResumeButton.applyRelativeSize();
        baseResumeButton.alignBelow(basePauseText);
        baseResumeButton.centerHorizontallyTo(basePauseText);

        basePlayAgainButton.applyRelativeSize();
        basePlayAgainButton.alignLeftOf(baseResumeButton);

        baseEscapeButton.applyRelativeSize();
        baseEscapeButton.alignRightOf(baseResumeButton);
    }

    @Override
    public void update() {
        if (mouseManager.isLeftClicked()) {

            soundManager.play(SoundType.CLICK_BUTTON);
            if (resumeButton.isClicked(mouseManager)) {
                handleResume();
            }

            if (playAgainButton.isClicked(mouseManager)) {
                handlePlayAgain();
            }

            if (escapeButton.isClicked(mouseManager)) {
                handleEscape();
            }

        }
    }

    private void handleResume() {

        screenManager.pop();

    }

    private void handlePlayAgain() {
        int option = JOptionPane.showConfirmDialog(
                null,
                "Your game progress will not be saved.",
                "WARNING",
                JOptionPane.OK_CANCEL_OPTION
        );
        if (option == JOptionPane.OK_OPTION) {
            playAgain();
        }
    }

    private void handleEscape() {
        int option = JOptionPane.showConfirmDialog(
                null,
                "Do you want to save the game progress?",
                "WARNING",
                JOptionPane.YES_NO_OPTION
        );

        if (option == JOptionPane.YES_OPTION) {
            saveGameProgressAndExit();
        } else if (option == JOptionPane.NO_OPTION) {
            exitToMainMenu();
        }
    }

    private void exitToMainMenu() {
        screenManager.pop();
        PlayScreen previousPlayScreen = (PlayScreen) screenManager.top();
        previousPlayScreen.setExited(true);
        screenManager.pop();
    }

    private void playAgain() {

        screenManager.pop();

        PlayScreen previousPlayScreen = (PlayScreen) screenManager.top();
        previousPlayScreen.setExited(true);
        ScreenType previousLevelId = previousPlayScreen.getLevelId();

        screenManager.pop();
        screenManager.push(previousLevelId);
    }

    private void saveGameProgressAndExit() {

        screenManager.pop();

        PlayScreen previousPlayScreen = (PlayScreen) screenManager.top();
        String savePath = previousPlayScreen.getLevelSavePath();

        GameContext.getInstance().serializeGameContext();
        PowerUpManager.getInstance().serializePowerUps();
        BrickManager.getInstance().serializeBricks();
        previousPlayScreen.getScoreText().serializeToJson();
        previousPlayScreen.getNumScoreText().serializeToJson();
        previousPlayScreen.getPauseButton().serializeToJson();
        previousPlayScreen.getBackground().serializeToJson();

        JsonLoaderUtils.saveToJson(savePath, previousPlayScreen);

        previousPlayScreen.setExited(true);
        screenManager.pop();

    }

    @Override
    public void render(Graphics2D graphics2D) {
        background.render(graphics2D);
        pauseText.render(graphics2D);
        resumeButton.render(graphics2D);
        playAgainButton.render(graphics2D);
        escapeButton.render(graphics2D);
    }

    @Override
    public void onEnter() {

    }

    @Override
    public void onExit() {

    }
}
