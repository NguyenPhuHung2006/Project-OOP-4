package exception;

/**
 * Represents a custom exception type used throughout the game for structured error handling.
 *
 * <p>The {@code GameException} class extends {@link Exception} and includes an {@link ErrorCode}
 * to categorize different types of game-related errors, such as invalid states or missing resources.</p>
 *
 * <p>This allows developers to handle specific error conditions more precisely and log
 * meaningful diagnostic messages.</p>
 *
 *
 * @see exception.ExceptionHandler
 */

public class GameException extends Exception {
    private final ErrorCode errorCode;

    /**
     * Creates a new {@code GameException} with the specified error code and message.
     *
     * @param errorCode the type of error that occurred
     * @param message a descriptive message of the error
     */
    public GameException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    /**
     * Creates a new {@code GameException} with the specified error code, message, and cause.
     *
     * @param errorCode the type of error that occurred
     * @param message a descriptive message of the error
     * @param cause the underlying cause of this exception (maybe {@code null})
     */
    public GameException(ErrorCode errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    /**
     * Returns the {@link ErrorCode} describing the category of this exception.
     *
     * @return the error code
     */
    public ErrorCode getErrorCode() {
        return errorCode;
    }

    /**
     * Enumerates common error types that can occur during game execution.
     */
    public enum ErrorCode {
        RESOURCE_NOT_FOUND,
        INVALID_INPUT,
        INVALID_GAME_STATE,
        UNKNOWN
    }
}
