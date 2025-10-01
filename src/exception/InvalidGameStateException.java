package exception;

public class InvalidGameStateException extends GameException {
    public InvalidGameStateException(String message, Throwable cause) {
        super(ErrorCode.UNKNOWN, "Invalid game state: " + message, cause);
    }
}
