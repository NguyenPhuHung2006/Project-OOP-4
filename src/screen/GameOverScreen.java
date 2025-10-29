package screen;


import config.PlayerStatusData;

public class GameOverScreen extends GameEndScreen {

    public GameOverScreen(Screen screen) {

        super(screen);

    }

    @Override
    protected void saveGameResultCount(PlayerStatusData playerStatusData) {
        playerStatusData.numberOfLoseGames++;
    }
}
