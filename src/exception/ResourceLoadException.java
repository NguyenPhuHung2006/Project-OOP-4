package exception;

public class ResourceLoadException extends GameException {
    public ResourceLoadException(String resource, Throwable cause) {
        super(ErrorCode.RESOURCE_NOT_FOUND, "Failed to load resource: " + resource, cause);
    }
}
