package classes;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.UUID;

public class DirectMessage implements Serializable, Chat {
    private static final long serialVersionUID = 952891651691413076L;
    private final UUID id;
    protected LinkedList<String> usernames;
    private HashMap<UUID, Message> messages;

    public DirectMessage(UUID id, LinkedList<String> usernames, HashMap<UUID, Message> messages) {
        this.id = id;
        this.usernames = usernames;
        this.messages = messages;
    }

    public DirectMessage(LinkedList<String> usernames, HashMap<UUID, Message> messages) {
        this(UUID.randomUUID(), usernames, messages);
    }

    public DirectMessage(LinkedList<String> usernames) {
        this(UUID.randomUUID(), usernames, new HashMap<>());
    }

    /**
     * @return UUID
     */
    @Override
    public UUID getId() {
        return this.id;
    }

    /**
     * @return LinkedList<String>
     */
    @Override
    public LinkedList<String> getParticipants() {
        return this.usernames;
    }

    /**
     * @return LinkedList<Message>
     */
    @Override
    public HashMap<UUID, Message> getMessages() {
        return this.messages;
    }

    /**
     * @param username
     * @param message
     */
    @Override
    public Message addMessage(Message message) {
        messages.put(message.getId(), message);
        return message;
    }

    /**
     * @param messageId
     * @param newMessage
     */
    @Override
    public Message editMessage(UUID messageId, String newMessage) {
        if (!this.messages.containsKey(messageId))
            return null;

        Message msg = new Message(this.messages.get(messageId).getSenderUsername(), newMessage);
        this.messages.replace(messageId, msg);

        return msg;

    }

    /**
     * @param messageId
     */
    @Override
    public Message removeMesage(UUID messageId) {
        Message msg = this.messages.get(messageId);
        this.messages.remove(messageId);
        return msg;
    }

    /**
     * @param username
     */
    public void removeParticipant(String username) {
        this.usernames.remove(username);
    }
}
