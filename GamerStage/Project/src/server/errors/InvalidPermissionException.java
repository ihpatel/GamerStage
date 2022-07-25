package server.errors;

/**
 * InvalidPermissionException
 *
 * <p>
 * A {@code InvalidPermissionException} is thrown when client requests to modify
 * an entity that they do not have permission to edit.
 * </p>
 *
 * @author Stephen Feria
 * @version December 7, 2020
 */
public class InvalidPermissionException extends Exception {
    private static final long serialVersionUID = -7517180267088535590L;

    /**
     * Calls the parent method.
     */
    public InvalidPermissionException() {
        super();
    }

    /**
     * Calls the parent method with a message.
     * 
     * @param message
     */
    public InvalidPermissionException(String message) {
        super(message);
    }

}
