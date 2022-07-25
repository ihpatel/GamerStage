package server.errors;

/**
 * StreamFailedException
 *
 * <p>
 * A {@code StreamFailedException} is thrown when a stream fails.
 * </p>
 *
 * @author Stephen Feria
 * @version December 7, 2020
 */
public class StreamFailedException extends Exception {
    private static final long serialVersionUID = 7135935004435093076L;

    /**
     * Calls the parent method.
     */
    public StreamFailedException() {
        super();
    }

    /**
     * Calls the parent method with a message.
     * 
     * @param message
     */
    public StreamFailedException(String message) {
        super(message);
    }
}
