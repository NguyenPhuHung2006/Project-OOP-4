package screen.pausescreen;

import screen.Screen;
import screen.ScreenType;
import screen.playscreen.SinglePlayerPlayScreen;
import utils.JsonLoaderUtils;

import javax.swing.*;

public class SinglePlayerPauseScreen extends PauseScreen {

    public SinglePlayerPauseScreen(Screen screen) {
        super(screen);
    }


    @Override
    protected void handlePlayAgain() {
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

    @Override
    protected void handleEscape() {
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

    private void playAgain() {

        screenManager.pop();

        SinglePlayerPlayScreen previousSinglePlayScreen = (SinglePlayerPlayScreen) screenManager.top();
        previousSinglePlayScreen.setExited(true);
        JsonLoaderUtils.clearJsonFile(previousSinglePlayScreen.getLevelSavePath());
        ScreenType previousLevelId = previousSinglePlayScreen.getLevelId();

        screenManager.pop();
        screenManager.push(previousLevelId);
    }

}
