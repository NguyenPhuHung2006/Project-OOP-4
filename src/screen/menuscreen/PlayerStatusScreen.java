package screen.menuscreen;

import audio.SoundType;
import config.PlayerStatusData;
import object.UI.Background;
import object.UI.GameButton;
import object.UI.Text.GameText;
import screen.Screen;
import utils.JsonLoaderUtils;
import utils.TextUtils;

import java.awt.*;

public class PlayerStatusScreen implements Screen {

    GameText brickDestroyedText;
    GameText numBrickDestroyedText;

    GameText totalTimePlayedText;
    GameText numTotalTimePlayedText;

    GameText totalWinGameText;
    GameText numTotalWinGameText;

    GameText totalLostGameText;
    GameText numTotalLostGameText;

    GameButton returnButton;

    Background background;

    public PlayerStatusScreen(Screen screen) {
        init(screen);

        PlayerStatusScreen playerStatusScreen = (PlayerStatusScreen) screen;

        brickDestroyedText = new GameText(playerStatusScreen.brickDestroyedText);
        numBrickDestroyedText = new GameText(playerStatusScreen.numBrickDestroyedText);

        totalTimePlayedText = new GameText(playerStatusScreen.totalTimePlayedText);
        numTotalTimePlayedText = new GameText(playerStatusScreen.numTotalTimePlayedText);

        totalWinGameText = new GameText(playerStatusScreen.totalWinGameText);
        numTotalWinGameText = new GameText(playerStatusScreen.numTotalWinGameText);

        totalLostGameText = new GameText(playerStatusScreen.totalLostGameText);
        numTotalLostGameText = new GameText(playerStatusScreen.numTotalLostGameText);

        returnButton = new GameButton(playerStatusScreen.returnButton);

        background = new Background(playerStatusScreen.background);

    }

    @Override
    public void init(Screen screen) {
        if(!(screen instanceof PlayerStatusScreen playerStatusScreen)) {
            return;
        }

        GameText baseBrickDestroyedText = playerStatusScreen.brickDestroyedText;
        GameText baseNumBrickDestroyedText = playerStatusScreen.numBrickDestroyedText;

        GameText baseTotalTimePlayedText = playerStatusScreen.totalTimePlayedText;
        GameText baseNumTotalTimePlayedText = playerStatusScreen.numTotalTimePlayedText;

        GameText baseTotalWinGameText = playerStatusScreen.totalWinGameText;
        GameText baseNumTotalWinGameText = playerStatusScreen.numTotalWinGameText;

        GameText baseTotalLostGameText = playerStatusScreen.totalLostGameText;
        GameText baseNumTotalLostGameText = playerStatusScreen.numTotalLostGameText;

        GameButton baseReturnButton = playerStatusScreen.returnButton;

        PlayerStatusData playerStatusData = JsonLoaderUtils.loadFromJson(JsonLoaderUtils.playerStatusDataPath, PlayerStatusData.class);

        assert playerStatusData != null;
        baseNumBrickDestroyedText.setContent(String.valueOf(playerStatusData.numberOfBricksDestroyed));
        baseNumTotalTimePlayedText.setContent(TextUtils.convertMillisToTimeUnit(playerStatusData.totalTimePlayed));
        baseNumTotalWinGameText.setContent(String.valueOf(playerStatusData.numberOfWinGames));
        baseNumTotalLostGameText.setContent(String.valueOf(playerStatusData.numberOfLoseGames));

        baseBrickDestroyedText.updateSizeFromFontData();
        baseBrickDestroyedText.applyRelativePosition();

        baseNumBrickDestroyedText.updateSizeFromFontData();
        baseNumBrickDestroyedText.alignRightOf(baseBrickDestroyedText);

        baseTotalTimePlayedText.updateSizeFromFontData();
        baseTotalTimePlayedText.alignBelow(baseBrickDestroyedText);
        baseTotalTimePlayedText.translateY(spacingY);

        baseNumTotalTimePlayedText.updateSizeFromFontData();
        baseNumTotalTimePlayedText.alignRightOf(baseTotalTimePlayedText);

        baseTotalWinGameText.updateSizeFromFontData();
        baseTotalWinGameText.alignBelow(baseTotalTimePlayedText);
        baseTotalWinGameText.translateY(spacingY);

        baseNumTotalWinGameText.updateSizeFromFontData();
        baseNumTotalWinGameText.alignRightOf(baseTotalWinGameText);

        baseTotalLostGameText.updateSizeFromFontData();
        baseTotalLostGameText.alignBelow(baseTotalWinGameText);
        baseTotalLostGameText.translateY(spacingY);

        baseNumTotalLostGameText.updateSizeFromFontData();
        baseNumTotalLostGameText.alignRightOf(baseTotalLostGameText);

        baseReturnButton.applyRelativeSize();
        baseReturnButton.alignTopLeft();

    }

    @Override
    public void update() {
        if(mouseManager.isLeftClicked()) {
            soundManager.play(SoundType.CLICK_BUTTON);
            if(returnButton.isClicked(mouseManager)) {
                screenManager.pop();
            }
        }
    }

    @Override
    public void render(Graphics2D graphics2D) {

        background.render(graphics2D);

        brickDestroyedText.render(graphics2D);
        numBrickDestroyedText.render(graphics2D);

        totalTimePlayedText.render(graphics2D);
        numTotalTimePlayedText.render(graphics2D);

        totalWinGameText.render(graphics2D);
        numTotalWinGameText.render(graphics2D);

        totalLostGameText.render(graphics2D);
        numTotalLostGameText.render(graphics2D);

        returnButton.render(graphics2D);
    }

    @Override
    public void onEnter() {

    }

    @Override
    public void onExit() {

    }
}
