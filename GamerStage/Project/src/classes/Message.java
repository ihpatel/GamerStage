package classes;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

public class Message implements Serializable {
    private static final long serialVersionUID = 2153156156511598572L;
    private final UUID id;
    private String senderUsername;
    private String message;
    private LocalDateTime timestamp;

    public Message(String senderUsername, String message, LocalDateTime timestamp, UUID id) {
        this.senderUsername = senderUsername;
        this.message = message;
        this.timestamp = timestamp;
        this.id = id;
    }

    public Message(String senderUsername, String message, LocalDateTime timestamp) {
        this(senderUsername, message, timestamp, UUID.randomUUID());
    }

    public Message(String senderUsername, String message) {
        this(senderUsername, message, LocalDateTime.now(), UUID.randomUUID());
    }

    /**
     * @return UUID
     */
    public UUID getId() {
        return this.id;
    }

    /**
     * @return String
     */
    public String getMessage() {
        return this.message;
    }

    /**
     * @param message
     */
    public void editMessage(String message) {
        this.message = message;
    }

    /**
     * @return String
     */
    public String getSenderUsername() {
        return this.senderUsername;
    }

    /**
     * @return LocalDateTime
     */
    public LocalDateTime getTimeStamp() {
        return this.timestamp;
    }
}