package classes;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.UUID;

public class Group extends DirectMessage {
    private static final long serialVersionUID = -4038251311574471344L;
    private String groupName;

    public Group(String groupName, LinkedList<String> usernames, HashMap<UUID, Message> messages, UUID id) {
        super(id, usernames, messages);
        this.groupName = groupName;

    }

    public Group(String groupName, LinkedList<String> usernames, HashMap<UUID, Message> messages) {
        this(groupName, usernames, messages, UUID.randomUUID());
    }

    public Group(String groupName){
        this(groupName, new LinkedList<>(), new HashMap<>());
    }

    /**
     * @param username
     */
    public void addParticipant(String username) {
        super.usernames.add(username);
    }

    /**
     * @param username
     */
    public void removeParticipant(String username) {
        super.usernames.remove(username);
    }

    /**
     * @param groupName
     */
    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    /**
     * @return String
     */
    public String getGroupName() {
        return this.groupName;
    }

}
