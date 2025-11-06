package audio;

/**
 * Represents the different types of sounds used in the game.
 *
 * <p>Each {@code SoundType} value corresponds to a specific in-game
 * event or object that triggers a sound effect — such as breaking a brick,
 * clicking a button, or winning the game.</p>
 *
 * <p>This enum is typically used by the {@code SoundManager}
 * to identify which sound to play.</p>
 *
 * <pre>
 *     // Example usage:
 *     soundManager.play(SoundType.WIN_GAME);
 * </pre>
 *
 * @author Nguyen Phu Hung
 */
public enum SoundType {

    /** Sound played when a normal brick is hit or destroyed. */
    NORMAL_BRICK,

    /** Sound played when a strong brick (requires multiple hits) is damaged. */
    STRONG_BRICK,

    /** Sound played when a brick containing a power-up is broken. */
    POWERUP_BRICK,

    /** Sound played when a button in the UI is clicked. */
    CLICK_BUTTON,

    /** Sound played when the ball collides with the player's paddle. */
    PLAYER_PADDLE,

    /** Sound played when the ball hits the window boundary. */
    WINDOW_WALL,

    /** Sound played when the player wins the game. */
    WIN_GAME,

    /** Sound played when the player loses the game. */
    LOSE_GAME
}
