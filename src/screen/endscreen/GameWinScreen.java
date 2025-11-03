package screen.endscreen;


import audio.SoundType;
import config.PlayerStatusData;
import screen.Screen;

public class GameWinScreen extends GameEndScreen {

    public GameWinScreen(Screen screen) {

        super(screen);

    }

    @Override
    protected void saveGameResultCount(PlayerStatusData playerStatusData) {
        playerStatusData.numberOfWinGames++;
    }

    @Override
    public void onEnter() {
        soundManager.play(SoundType.WIN_GAME);
    }

    @Override
    public void onExit() {
    }
}
