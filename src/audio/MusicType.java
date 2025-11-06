package audio;

/**
 * Represents the different types of background music used in the game.
 *
 * <p>Each {@code MusicType} corresponds to a specific scene or game state
 * where a background track is played. This enum helps the audio system
 * determine which music theme to start, stop, or loop.</p>
 *
 * <pre>
 *     // Example usage:
 *     soundManager.playMusic(MusicType.PLAY_THEME);
 * </pre>
 *
 * @author Nguyen Phu Hung
 */
public enum MusicType {

    /** Music theme played during the main gameplay. */
    PLAY_THEME,

    /** Music theme played in the main menu. */
    MENU_THEME
}
