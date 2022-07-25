package server.errors;

/**
 * InvalidCommandException
 *
 * <p>
 * A {@code InvalidCommandException} is thrown when a client sends an invalid
 * command to the server.
 * </p>
 *
 * @author Stephen Feria
 * @version December 7, 2020
 */
public class InvalidCommandException extends Exception {
    private static final long serialVersionUID = 5408046167564623651L;

    /**
     * Calls the parent method.
     */
    public InvalidCommandException() {
        super();
    }

    /**
     * Calls the parent method with a message.
     * 
     * @param message
     */
    public InvalidCommandException(String message) {
        super(message);
    }

}
