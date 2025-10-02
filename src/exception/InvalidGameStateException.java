package exception;

public class InvalidGameStateException extends GameException {
    public InvalidGameStateException(String message, Throwable cause) {
        super(ErrorCode.INVALID_GAME_STATE, "Invalid game state: " + message, cause);
    }
}
