package server.errors;

/**
 * DuplicateUsernameException
 *
 * <p>
 * A {@code DuplicateUsernameException} is thrown when a client tries to sign up
 * with a username that already exists.
 * </p>
 *
 * @author Stephen Feria
 * @version December 7, 2020
 */
public class DuplicateUsernameException extends Exception {
    private static final long serialVersionUID = -8318158180932810661L;

    /**
     * Calls the parent method.
     */
    public DuplicateUsernameException() {
        super();
    }

    /**
     * Calls the parent method with a message.
     * 
     * @param message
     */
    public DuplicateUsernameException(String message) {
        super(message);
    }

}
