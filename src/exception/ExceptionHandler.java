package exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(ExceptionHandler.class);

    public static void handle(Exception e) {
        if (e instanceof GameException ge) {
            logger.error("GameException [{}]: {}", ge.getErrorCode(), ge.getMessage(), ge);
        } else {
            logger.error("Unexpected exception: {}", e.getMessage(), e);
        }
    }
}

