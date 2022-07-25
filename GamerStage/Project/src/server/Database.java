package server;

import java.io.*;
import java.util.Base64;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import classes.*;
import server.errors.*;

public class Database {
    private static HashMap<String, User> userData = new HashMap<>();
    private static LinkedList<String> liveUsers = new LinkedList<>();

    @SuppressWarnings("unchecked")
    public synchronized static void initializeData() {
        File f = new File("server\\user_info.txt");
        if (f.length() == 0)
            return;

        try (FileInputStream fis = new FileInputStream(f); ObjectInputStream ois = new ObjectInputStream(fis);) {

            userData = (HashMap<String, User>) ois.readObject();

        } catch (IOException ioe) {
            ioe.printStackTrace();
        } catch (ClassNotFoundException cnfe) {
            cnfe.printStackTrace();
        }
    }

    public synchronized static void writeData() {
        File f = new File("server\\user_info.txt");
        try (FileOutputStream fos = new FileOutputStream(f); ObjectOutputStream oos = new ObjectOutputStream(fos);) {

            oos.writeObject(userData);
            oos.flush();

        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public static User signUp(String username, String name, int age, String password)
            throws DuplicateUsernameException {
        if (!userNameAvailable(username) || username.equals("SERVER"))
            throw new DuplicateUsernameException();

        User user = new User(username, name, age, password);
        userData.put(user.getUsername(), user);
        liveUsers.add(user.getUsername());
        writeData();
        return user;
    }

    public static User login(String username, String password) throws NotFoundException, InvalidLoginException {
        if (!userData.containsKey(username)) {
            throw new NotFoundException("user");
        } else if (!userData.get(username).getPassword().equals(password)) {
            throw new InvalidLoginException();
        } else {
            User user = userData.get(username);
            liveUsers.add(user.getUsername());
            writeData();
            return user;
        }

    }

    public static void signOut(String username) throws NotFoundException {
        if (!liveUsers.contains(username))
            throw new NotFoundException("user");

        liveUsers.remove(username);
        writeData();
    }

    public static void editName(String username, String newName) throws NotFoundException {
        if (!userData.containsKey(username))
            throw new NotFoundException("user");

        userData.get(username).setName(newName);
        writeData();
    }

    public static void editAge(String username, int newAge) throws NotFoundException {
        if (!userData.containsKey(username))
            throw new NotFoundException("user");

        userData.get(username).setAge(newAge);
        writeData();
    }

    public static void editPassword(String username, String currentPassword, String newPassword)
            throws NotFoundException, InvalidLoginException {
        if (!userData.containsKey(username))
            throw new NotFoundException("user");
        else if (!userData.get(username).getPassword().equals(currentPassword))
            throw new InvalidLoginException();

        userData.get(username).setPassword(newPassword);
        writeData();
    }

    public static void deleteAccount(String username, String password)
            throws NotFoundException, InvalidLoginException, IOException {
        if (!userData.containsKey(username))
            throw new NotFoundException("user");
        else if (!userData.get(username).getPassword().equals(password))
            throw new InvalidLoginException();

        HashSet<String> participants = new HashSet<>();
        HashSet<UUID> chatIds = new HashSet<>();
        synchronized (Database.class) {
            // for (String participant : chat.getParticipants()) {
            // if (!participant.equals(username)) {
            // userData.get(participant).addChat(chat);
            // }

            // if (liveUsers.contains(participant)) {
            // sendChatToUser(participant, chat);
            // }
            // }

            for (Chat chat : userData.get(username).getChats().values()) {
                chatIds.add(chat.getId());
                for (String participant : chat.getParticipants()) {
                    if (!participant.equals(username))
                        participants.add(participant);
                }
            }

            HashMap<String, List<Chat>> modifiedChats = new HashMap<>();
            Message terminationMessage = new Message("SERVER", username + " has left");

            for (String participant : participants) {
                LinkedList<Chat> chats = new LinkedList<>();
                for (Chat chat : userData.get(participant).getChats().values()) {
                    if (chatIds.contains(chat.getId())) {
                        chats.add(chat);
                        if (chat.getParticipants().contains(username)
                                && !chat.getMessages().values().contains(terminationMessage)) {
                            chat.removeParticipant(username);
                            chat.addMessage(terminationMessage);
                            chats.add(chat);
                        }
                    }
                }
                modifiedChats.put(participant, chats);
            }

            for (String participant : liveUsers) {
                if (participants.contains(participant)) {
                    for (Chat chat : modifiedChats.get(participant)) {
                        sendChatToUser(participant, chat);
                    }
                }
            }

        }

        userData.remove(username);

        writeData();

    }

    public static void sendMessage(String username, UUID chatId, String message) throws NotFoundException, IOException {
        if (!userData.containsKey(username)) {
            throw new NotFoundException("user");
        }

        Message messageObject = new Message(username, message);

        for (String participant : userData.get(username).getChats().get(chatId).getParticipants()) {
            if (!userData.containsKey(participant)) {
                throw new NotFoundException("user");
            }

            User user = userData.get(participant);

            if (!user.getChats().containsKey(chatId)) {
                throw new NotFoundException("chat");
            }

            synchronized (Database.class) {
                Chat chat = user.getChats().get(chatId);
                chat.addMessage(messageObject);
                user.addChat(chat);

                sendChatToUser(participant, chat);

            }
        }
        writeData();

    }

    public static void editMessage(String username, UUID chatId, UUID messageId, String newMessage)
            throws NotFoundException, IOException {
        if (!userData.containsKey(username)) {
            throw new NotFoundException("user");
        }

        Message msg = userData.get(username).getChats().get(chatId).getMessages().get(messageId);
        msg.editMessage(newMessage);

        for (String participant : userData.get(username).getChats().get(chatId).getParticipants()) {
            if (!userData.containsKey(participant)) {
                throw new NotFoundException("user");
            }

            User user = userData.get(participant);

            if (!user.getChats().containsKey(chatId)) {
                throw new NotFoundException("chat");
            }

            synchronized (Database.class) {
                Chat chat = user.getChats().get(chatId);
                chat.addMessage(msg);
                user.addChat(chat);

                sendChatToUser(participant, chat);
            }
        }
        writeData();
    }

    public static void removeMessage(String username, UUID chatId, UUID messageId)
            throws NotFoundException, IOException {
        if (!userData.containsKey(username)) {
            throw new NotFoundException("user");
        }

        for (String participant : userData.get(username).getChats().get(chatId).getParticipants()) {
            if (!userData.containsKey(participant)) {
                throw new NotFoundException("user");
            }

            User user = userData.get(participant);

            if (!user.getChats().containsKey(chatId)) {
                throw new NotFoundException("chat");
            }

            synchronized (Database.class) {
                Chat chat = user.getChats().get(chatId);
                chat.removeMesage(messageId);
                user.addChat(chat);

                sendChatToUser(participant, chat);

            }
        }
        writeData();
    }

    public static DirectMessage createDM(String username, String otherUser) throws NotFoundException, IOException {
        if (!userData.containsKey(username)) {
            throw new NotFoundException("user");
        } else if (!userData.containsKey(otherUser)) {
            throw new NotFoundException("user");
        }

        LinkedList<String> list = new LinkedList<>();
        list.add(username);
        list.add(otherUser);
        DirectMessage c = new DirectMessage(list);

        synchronized (Database.class) {
            userData.get(username).addChat(c);
            userData.get(otherUser).addChat(c);

            sendChatToUser(otherUser, c);
        }
        writeData();
        return c;

    }

    public static Group createGroup(String username, String groupName) throws NotFoundException, IOException {
        if (!userData.containsKey(username)) {
            throw new NotFoundException("user");
        }

        Group c = new Group(groupName);
        c.addParticipant(username);

        synchronized (Database.class) {
            userData.get(username).addChat(c);
        }
        writeData();
        return c;

    }

    public static void addUserToGroup(String username, UUID groupId, String otherUser)
            throws NotFoundException, IOException {
        if (!userData.containsKey(username)) {
            throw new NotFoundException("user");
        } else if (!userData.get(username).getChats().containsKey(groupId)) {
            throw new NotFoundException("chat");
        }

        Group group = (Group) userData.get(username).getChats().get(groupId);
        group.addParticipant(otherUser);
        synchronized (Database.class) {

            for (String participant : group.getParticipants()) {
                if (!participant.equals(username)) {
                    userData.get(participant).addChat(group);
                }

                if (liveUsers.contains(participant)) {
                    sendChatToUser(otherUser, group);
                }
            }

        }
        writeData();

    }

    public static void removeUserFromGroup(String username, UUID groupId) throws NotFoundException, IOException {
        if (!userData.containsKey(username)) {
            throw new NotFoundException("user");
        } else if (!userData.get(username).getChats().containsKey(groupId)) {
            throw new NotFoundException("chat");
        }

        Chat chat = (Chat) userData.get(username).getChats().get(groupId);
        chat.removeParticipant(username);
        chat.addMessage(new Message("SERVER", username + " has left the chat"));
        synchronized (Database.class) {

            for (String participant : chat.getParticipants()) {
                if (!participant.equals(username)) {
                    userData.get(participant).addChat(chat);
                }

                if (liveUsers.contains(participant)) {
                    sendChatToUser(participant, chat);
                }
            }
        }
        writeData();
    }

    public static String requestInfo(String requestedUsername) throws NotFoundException {
        if (!userData.containsKey(requestedUsername)) {
            throw new NotFoundException("user");
        }

        User user = userData.get(requestedUsername);

        return String.format("^ %s %d", "!" + user.getName() + "!", user.getAge());
    }

    private static void sendChatToUser(String user, Chat chat) throws IOException {
        if (liveUsers.contains(user)) {
            for (ClientThread ct : ServerController.getConnectedUsers()) {
                if (ct.getUsername().equals(user)) {
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    ObjectOutputStream oos = new ObjectOutputStream(baos);
                    oos.writeObject(chat);
                    oos.flush();
                    ct.getOut().println("$ " + Base64.getEncoder().encodeToString(baos.toByteArray()));
                    ct.getOut().flush();
                }
            }
        }
    }

    private static boolean userNameAvailable(String user) {
        boolean available = true;
        for (String username : userData.keySet()) {
            if (user.equals(username)) {
                return !available;
            }
        }
        return available;
    }

}
