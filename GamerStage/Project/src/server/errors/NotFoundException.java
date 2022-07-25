package server.errors;

public class NotFoundException extends Exception {
    private static final long serialVersionUID = -2619163472804718840L;

    /**
     * Calls the parent method.
     */
    public NotFoundException() {
        super();
    }

    /**
     * Calls the parent method with a message.
     * 
     * @param message
     */
    public NotFoundException(String message) {
        super(message);
    }
}
