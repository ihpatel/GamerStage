package classes;

import java.io.Serializable;
import java.util.HashMap;
import java.util.UUID;

public class User implements Serializable {
    private static final long serialVersionUID = -3742300279878909691L;
    private final String username;
    private String name;
    private int age;
    private String password;
    private HashMap<UUID, Chat> chats;

    public User(String username, String name, int age, String password, HashMap<UUID, Chat> chats) {
        this.username = username;
        this.name = name;
        this.age = age;
        this.password = password;
        this.chats = chats;
    }

    public User(String username, String name, int age, String password) {
        this(username, name, age, password, new HashMap<>());
    }

    /**
     * @return String
     */
    public String getUsername() {
        return this.username;
    }

    /**
     * @return String
     */
    public String getName() {
        return this.name;
    }

    /**
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return int
     */
    public int getAge() {
        return age;
    }

    /**
     * @param age
     */
    public void setAge(int age) {
        this.age = age;
    }

    /**
     * @return String
     */
    public String getPassword() {
        return this.password;
    }

    /**
     * @param password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return LinkedList<Chat>
     */
    public HashMap<UUID, Chat> getChats() {
        return chats;
    }

    /**
     * @param c
     */
    public void addChat(Chat c) {
        if (chats.containsKey(c.getId())) {
            chats.replace(c.getId(), chats.get(c.getId()), c);
        } else {
            chats.put(c.getId(), c);
        }
    }

    /**
     * @param chatId
     */
    public void removeChat(UUID chatId) {
        chats.remove(chatId);
    }

}
