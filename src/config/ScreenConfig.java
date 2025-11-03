package config;

import screen.endscreen.GameOverScreen;
import screen.endscreen.GameWinScreen;
import screen.menuscreen.MenuScreen;
import screen.menuscreen.PlayerStatusScreen;
import screen.menuscreen.StartScreen;
import screen.pausescreen.SinglePlayerPauseScreen;
import screen.playscreen.MultiPlayerPlayScreen;
import screen.playscreen.SinglePlayerPlayScreen;

public class ScreenConfig {

    public StartScreen startScreen;
    public MenuScreen menuScreen;

    public SinglePlayerPlayScreen playLevel1Screen;
    public SinglePlayerPlayScreen playLevel2Screen;
    public SinglePlayerPlayScreen playLevel3Screen;
    public MultiPlayerPlayScreen multiPlayerPlayScreen;

    public PlayerStatusScreen playerStatusScreen;
    public SinglePlayerPauseScreen pauseScreen;

    public GameOverScreen gameOverScreen;
    public GameWinScreen gameWinScreen;
}
