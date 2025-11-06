package config;

import screen.endscreen.GameOverScreen;
import screen.endscreen.GameWinScreen;
import screen.menuscreen.MenuScreen;
import screen.menuscreen.PlayerStatusScreen;
import screen.menuscreen.StartScreen;
import screen.pausescreen.SinglePlayerPauseScreen;
import screen.playscreen.MultiPlayerPlayScreen;
import screen.playscreen.SinglePlayerPlayScreen;

/**
 * Holds references to all the main screens used in the game.
 *
 * <p>The {@code ScreenConfig} class serves as a centralized container for screen instances.
 * Each field represents a specific part of the game's UI flow, such as the start screen,
 * play screens, pause menu, or end-game screens. These are typically loaded from
 *  * a JSON configuration file and passed to {@link screen.ScreenManager#loadFromJson(ScreenConfig)}.</p>
 *
 * <p>This configuration allows for flexible management of transitions between screens
 * without hardcoding dependencies in gameplay logic.</p>
 *
 * @see screen.Screen
 * @see screen.menuscreen.MenuScreen
 * @see screen.playscreen.SinglePlayerPlayScreen
 * @see screen.playscreen.MultiPlayerPlayScreen
 */

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
