package screen;


import config.PlayerStatusData;

public class GameWinScreen extends GameEndScreen {

    public GameWinScreen(Screen screen) {

        super(screen);

    }

    @Override
    protected void saveGameResultCount(PlayerStatusData playerStatusData) {
        playerStatusData.numberOfWinGames++;
    }
}
