package exception;

/**
 * Thrown to indicate that the game has entered an invalid or inconsistent state.
 *
 * <p>The {@code InvalidGameStateException} class extends {@link GameException}
 * and is used when a game component (e.g., sound system, network, or UI) is invoked
 * in an unexpected or illegal state. This helps developers detect and correct
 * logic errors during development and debugging.</p>
 *
 * <p>Example usage:</p>
 * <pre>
 * if (sounds.containsKey(soundType)) {
 *     throw new InvalidGameStateException("Sound type already loaded: " + soundType, null);
 * }
 * </pre>
 *
 * @see exception.GameException
 * @see exception.GameException.ErrorCode#INVALID_GAME_STATE
 */
public class InvalidGameStateException extends GameException {

    /**
     * Constructs a new {@code InvalidGameStateException} with a detailed message and cause.
     *
     * @param message a description of the invalid state
     * @param cause the underlying cause of this exception (maybe {@code null})
     */
    public InvalidGameStateException(String message, Throwable cause) {
        super(ErrorCode.INVALID_GAME_STATE, "Invalid game state: " + message, cause);
    }
}
