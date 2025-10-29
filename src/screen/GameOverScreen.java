package screen;


import config.PlayerStatusData;
import utils.JsonLoaderUtils;

public class GameOverScreen extends GameEndScreen {

    public GameOverScreen(Screen screen) {

        super(screen);

    }

    @Override
    public void saveGameStatus() {
        PlayerStatusData playerStatusData = JsonLoaderUtils.loadFromJson(JsonLoaderUtils.playerStatusDataPath, PlayerStatusData.class);
        assert playerStatusData != null;
        playerStatusData.numberOfLoseGames += 1;
        JsonLoaderUtils.saveToJson(JsonLoaderUtils.playerStatusDataPath, playerStatusData);
    }
}
