package classes;

import java.util.LinkedList;
import java.util.UUID;
import java.util.HashMap;

public interface Chat {

    public UUID getId();

    public LinkedList<String> getParticipants();

    public HashMap<UUID, Message> getMessages();

    public Message addMessage(Message message);

    public Message editMessage(UUID messageId, String newMessage);

    public Message removeMesage(UUID messageId);
    public void removeParticipant(String username);
}
