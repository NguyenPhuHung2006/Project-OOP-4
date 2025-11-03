package screen;

import audio.MusicType;
import audio.SoundType;
import object.UI.Background;
import object.UI.GameButton;
import object.UI.Text.GameText;

import javax.swing.*;
import java.awt.*;

public class MenuScreen implements Screen {

    GameText level1Text;
    GameButton level1Button;

    GameText level2Text;
    GameButton level2Button;

    GameText level3Text;
    GameButton level3Button;

    GameText multiPlayerText;
    GameButton multiPlayerButton;

    GameButton playerStatusButton;

    GameButton escapeButton;

    Background background;

    public MenuScreen(Screen screen) {

        init(screen);

        MenuScreen menuScreen = (MenuScreen) screen;

        level1Button = new GameButton(menuScreen.level1Button);
        level1Text = new GameText(menuScreen.level1Text);

        level2Button = new GameButton(menuScreen.level2Button);
        level2Text = new GameText(menuScreen.level2Text);

        level3Button = new GameButton(menuScreen.level3Button);
        level3Text = new GameText(menuScreen.level3Text);

        multiPlayerText = new GameText(menuScreen.multiPlayerText);
        multiPlayerButton = new GameButton(menuScreen.multiPlayerButton);

        playerStatusButton = new GameButton(menuScreen.playerStatusButton);

        escapeButton = new GameButton(menuScreen.escapeButton);

        background = new Background(menuScreen.background);
    }

    @Override
    public void init(Screen screen) {
        if (!(screen instanceof MenuScreen menuScreen)) {
            return;
        }

        GameText baseLevel1Text = menuScreen.level1Text;
        GameButton baseLevel1Button = menuScreen.level1Button;

        GameText baseLevel2Text = menuScreen.level2Text;
        GameButton baseLevel2Button = menuScreen.level2Button;

        GameText baseLevel3Text = menuScreen.level3Text;
        GameButton baseLevel3Button = menuScreen.level3Button;

        GameText baseMultiPlayerText = menuScreen.multiPlayerText;
        GameButton baseMultiPlayerButton = menuScreen.multiPlayerButton;

        GameButton basePlayerStatusButton = menuScreen.playerStatusButton;

        GameButton baseEscapeButton = menuScreen.escapeButton;

        baseLevel1Text.updateSizeFromFontData();
        baseLevel1Text.applyRelativePosition();

        baseLevel1Button.applyRelativeSize();
        baseLevel1Button.alignRightOf(baseLevel1Text);
        baseLevel1Button.centerVerticallyTo(baseLevel1Text);

        baseLevel2Text.updateSizeFromFontData();
        baseLevel2Text.alignBelow(baseLevel1Text);
        baseLevel2Text.translateY(spacingY);

        baseLevel2Button.applyRelativeSize();
        baseLevel2Button.alignRightOf(baseLevel2Text);
        baseLevel2Button.centerVerticallyTo(baseLevel2Text);

        baseLevel3Text.updateSizeFromFontData();
        baseLevel3Text.alignBelow(baseLevel2Text);
        baseLevel3Text.translateY(spacingY);

        baseLevel3Button.applyRelativeSize();
        baseLevel3Button.alignRightOf(baseLevel3Text);
        baseLevel3Button.centerVerticallyTo(baseLevel3Text);

        baseMultiPlayerText.updateSizeFromFontData();
        baseMultiPlayerText.alignBelow(baseLevel3Text);
        baseMultiPlayerText.translateY(spacingY);

        baseMultiPlayerButton.applyRelativeSize();
        baseMultiPlayerButton.alignRightOf(baseMultiPlayerText);
        baseMultiPlayerButton.centerVerticallyTo(baseMultiPlayerText);

        basePlayerStatusButton.applyRelativeSize();
        basePlayerStatusButton.alignTopRight();

        baseEscapeButton.applyRelativeSize();
        baseEscapeButton.alignTopLeft();

    }

    @Override
    public void update() {

        if (mouseManager.isLeftClicked()) {
            soundManager.play(SoundType.CLICK_BUTTON);
            if (level1Button.isClicked(mouseManager)) {
                screenManager.push(ScreenType.PLAY_LEVEL1);

            } else if (level2Button.isClicked(mouseManager)) {
                screenManager.push(ScreenType.PLAY_LEVEL2);

            } else if (level3Button.isClicked(mouseManager)) {
                screenManager.push(ScreenType.PLAY_LEVEL3);

            } else if (playerStatusButton.isClicked(mouseManager)) {
                screenManager.push(ScreenType.PLAYER_STATUS);

            } else if (escapeButton.isClicked(mouseManager)) {
                handleEscape();

            } else if(multiPlayerButton.isClicked(mouseManager)) {
                screenManager.push(ScreenType.MULTI_PLAYER);
            }
        }
    }

    @Override
    public void render(Graphics2D graphics2D) {

        background.render(graphics2D);

        level1Text.render(graphics2D);
        level1Button.render(graphics2D);

        level2Text.render(graphics2D);
        level2Button.render(graphics2D);

        level3Text.render(graphics2D);
        level3Button.render(graphics2D);

        multiPlayerText.render(graphics2D);
        multiPlayerButton.render(graphics2D);

        playerStatusButton.render(graphics2D);
        escapeButton.render(graphics2D);
    }

    private void handleEscape() {
        int option = JOptionPane.showConfirmDialog(
                null,
                "Do you want to exit",
                "WARNING",
                JOptionPane.YES_NO_OPTION
        );
        if (option == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }

    @Override
    public void onEnter() {
        soundManager.playMusic(MusicType.MENU_THEME, true);
    }

    @Override
    public void onExit() {
        soundManager.stopMusic(MusicType.MENU_THEME);
    }
}
