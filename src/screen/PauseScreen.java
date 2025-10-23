package screen;

import audio.SoundType;
import object.UI.Background;
import object.UI.GameButton;
import object.UI.Text.GameText;

import java.awt.*;

public class PauseScreen implements Screen {

    private GameText pauseText;
    private GameButton resumeButton;
    private GameButton playAgainButton;
    private GameButton escapeButton;
    private Background background;

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
        if(!(screen instanceof PauseScreen pauseScreen)) {
            return;
        }

        GameText basePauseText = pauseScreen.pauseText;
        GameButton baseResumeButton = pauseScreen.resumeButton;
        GameButton basePlayAgainButton = pauseScreen.playAgainButton;
        GameButton baseEscapeButton = pauseScreen.escapeButton;

        basePauseText.updateSizeFromFontData();
        basePauseText.center();

        basePlayAgainButton.applyRelativeSize();
        basePlayAgainButton.alignBelow(basePauseText);
        basePlayAgainButton.centerHorizontallyTo(basePauseText);

        baseResumeButton.applyRelativeSize();
        baseResumeButton.alignRightOf(basePlayAgainButton);

        baseEscapeButton.applyRelativeSize();
        baseEscapeButton.alignLeftOf(basePlayAgainButton);

    }

    @Override
    public void update() {
        if(mouseManager.isLeftClicked()) {

            soundManager.play(SoundType.CLICK_BUTTON);
            if(resumeButton.isClicked(mouseManager)) {
                screenManager.pop();
            }
        }
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
