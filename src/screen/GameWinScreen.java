package screen;

import config.PlayerStatusData;
import utils.JsonLoaderUtils;

public class GameWinScreen extends GameEndScreen {

    public GameWinScreen(Screen screen) {

        super(screen);

    }

    @Override
    public void saveGameStatus() {
        PlayerStatusData playerStatusData = JsonLoaderUtils.loadFromJson(JsonLoaderUtils.playerStatusDataPath, PlayerStatusData.class);
        assert playerStatusData != null;
        playerStatusData.numberOfWinGames += 1;
        JsonLoaderUtils.saveToJson(JsonLoaderUtils.playerStatusDataPath, playerStatusData);
    }

}
