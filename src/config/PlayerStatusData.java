package config;

/**
 * Represents statistical data related to the player's overall performance.
 *
 * <p>The {@code PlayerStatusData} class stores player statistics such as
 * total bricks destroyed, total time played, and the number of games won or lost.
 * It is typically serialized or saved to track long-term progress.</p>
 *
 *
 * @see screen.menuscreen.PlayerStatusScreen
 */

public class PlayerStatusData {
    public int numberOfBricksDestroyed;
    public long totalTimePlayed;
    public int numberOfWinGames;
    public int numberOfLoseGames;
}
