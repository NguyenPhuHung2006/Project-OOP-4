package exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Centralized handler for game-related exceptions.
 *
 * <p>The {@code ExceptionHandler} class provides a static method to process and log exceptions
 * using the SLF4J logging framework. It distinguishes between known {@link GameException} types
 * and generic unexpected exceptions.</p>
 *
 * <p>All critical exceptions should be reported through this handler to maintain consistent
 * logging and error tracing throughout the game engine.</p>
 *
 * <p>Example usage:</p>
 * <pre>
 * try {
 *     soundManager.loadFromJson(config);
 * } catch (Exception e) {
 *     ExceptionHandler.handle(e);
 * }
 * </pre>
 *
 * @see exception.GameException
 * @see org.slf4j.Logger
 */
public class ExceptionHandler {

    /** SLF4J logger used to record exceptions and error messages. */
    private static final Logger logger = LoggerFactory.getLogger(ExceptionHandler.class);

    /**
     * Handles a given exception by logging it with an appropriate message.
     *
     * <p>If the exception is a {@link GameException}, its error code and message
     * are included in the log output. Otherwise, it is logged as an unexpected error.</p>
     *
     * @param e the exception to handle
     */
    public static void handle(Exception e) {
        if (e instanceof GameException ge) {
            logger.error("GameException [{}]: {}", ge.getErrorCode(), ge.getMessage(), ge);
        } else {
            logger.error("Unexpected exception: {}", e.getMessage(), e);
        }
    }
}
