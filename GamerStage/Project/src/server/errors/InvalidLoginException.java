package server.errors;

/**
 * InvalidLoginException
 *
 * <p>
 * A {@code InvalidLoginException} is thrown when a client tries to log in with
 * invalid credentials.
 * </p>
 *
 * @author Stephen Feria
 * @version December 7, 2020
 */
public class InvalidLoginException extends Exception {
    private static final long serialVersionUID = 9140395238723774385L;

    /**
     * Calls the parent method.
     */
    public InvalidLoginException() {
        super();
    }

    /**
     * Calls the parent method with a message.
     * 
     * @param message
     */
    public InvalidLoginException(String message) {
        super(message);
    }
}
