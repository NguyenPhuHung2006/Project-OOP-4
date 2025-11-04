package screen.pausescreen;

import audio.SoundType;
import object.GameContext;
import object.UI.Background;
import object.UI.GameButton;
import object.UI.Text.GameText;
import object.brick.BrickManager;
import object.movable.powerup.PowerUpManager;
import screen.Screen;
import screen.ScreenType;
import screen.playscreen.PlayScreen;
import screen.playscreen.SinglePlayerPlayScreen;
import utils.JsonLoaderUtils;

import javax.swing.*;
import java.awt.*;

public abstract class PauseScreen implements Screen {

    private final GameText pauseText;
    private final GameButton resumeButton;
    private final GameButton playAgainButton;
    private final GameButton escapeButton;
    private final Background background;

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

    protected abstract void handlePlayAgain();
    protected abstract void handleEscape();

    protected void handleResume() {
        screenManager.pop();
    }

    protected void exitToMainMenu() {
        screenManager.pop();
        PlayScreen previousPlayScreen = (PlayScreen) screenManager.top();
        previousPlayScreen.setExited(true);
        screenManager.pop();
    }

    protected void saveGameProgressAndExit() {

        screenManager.pop();

        if(screenManager.top() instanceof SinglePlayerPlayScreen singlePlayerPlayScreen) {
            singlePlayerPlayScreen.saveGameProgress();
        }

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
