package screen;


import audio.SoundType;
import config.PlayerStatusData;

public class GameOverScreen extends GameEndScreen {

    public GameOverScreen(Screen screen) {

        super(screen);

    }

    @Override
    protected void saveGameResultCount(PlayerStatusData playerStatusData) {
        playerStatusData.numberOfLoseGames++;
    }

    @Override
    public void onEnter() {
        soundManager.play(SoundType.LOSE_GAME);
    }

    @Override
    public void onExit() {
    }
}
