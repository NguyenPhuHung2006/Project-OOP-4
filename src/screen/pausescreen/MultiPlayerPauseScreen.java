package screen.pausescreen;

import screen.Screen;
import screen.playscreen.MultiPlayerPlayScreen;

import javax.swing.*;

public class MultiPlayerPauseScreen extends PauseScreen {

    public MultiPlayerPauseScreen(Screen screen) {
        super(screen);
    }

    @Override
    protected void handlePlayAgain() {
        JOptionPane.showMessageDialog(
                null,
                "Multiplayer mode cannot use this button.",
                "Action Not Allowed",
                JOptionPane.WARNING_MESSAGE
        );
    }

    @Override
    protected void handleEscape() {
        int option = JOptionPane.showConfirmDialog(
                null,
                "You will lose this match if you exit",
                "WARNING",
                JOptionPane.YES_NO_OPTION
        );

        if(option == JOptionPane.YES_OPTION) {
            screenManager.pop();

            MultiPlayerPlayScreen multiPlayerPlayScreen = (MultiPlayerPlayScreen) screenManager.top();

            multiPlayerPlayScreen.setGameOver(true);

            multiPlayerPlayScreen.handleGameEnd();
        }

    }
}
