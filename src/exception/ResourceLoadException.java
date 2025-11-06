package exception;

/**
 * Thrown when a game resource (such as a texture, sound, or configuration file)
 * fails to load properly.
 *
 * <p>The {@code ResourceLoadException} class extends {@link GameException} and provides
 * a structured way to report missing or inaccessible assets. This ensures that
 * resource-loading issues are clearly distinguishable from other runtime errors.</p>
 *
 * <p>Example usage:</p>
 * <pre>
 * try {
 *     sound = TinySound.loadSound(getClass().getResource(path));
 * } catch (Exception e) {
 *     throw new ResourceLoadException(path, e);
 * }
 * </pre>
 *
 * @see exception.GameException
 * @see exception.GameException.ErrorCode#RESOURCE_NOT_FOUND
 */
public class ResourceLoadException extends GameException {

    /**
     * Constructs a new {@code ResourceLoadException} for a given resource path.
     *
     * @param resource the resource that failed to load
     * @param cause the underlying cause of the loading failure
     */
    public ResourceLoadException(String resource, Throwable cause) {
        super(ErrorCode.RESOURCE_NOT_FOUND, "Failed to load resource: " + resource, cause);
    }
}
