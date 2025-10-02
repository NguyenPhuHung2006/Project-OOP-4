package exception;

public class GameException extends Exception {
    private final ErrorCode errorCode;

    public GameException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public GameException(ErrorCode errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public enum ErrorCode {
        RESOURCE_NOT_FOUND,
        INVALID_INPUT,
        INVALID_GAME_STATE,
        UNKNOWN
    }
}
