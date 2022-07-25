package client;

import javax.swing.*;
import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.Base64;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;


import classes.*;

import classes.gui.*;

/**
 * Running template of the network client class
 * <p>
 * Purdue University -- CS18000 -- Fall 2020 -- Project 5
 *
 * @author Jarien Reed,
 * @version November 22nd, 2020
 */
public class NetworkClient {

    public Socket socket = null;
    public User currentUser = null;
    public BufferedReader reader = null;
    public PrintWriter writer = null;
    public AtomicBoolean receiveServerInput = new AtomicBoolean(false);
    public String currentObject = null;
    public boolean awaitingResponse = false;
    public boolean error = false;
    public String errormsg = "";
    public String requestedName;
    public String requestedAge;
    public boolean closeGUI = false;
    public boolean newChat = false;

    public NetworkClient() {
    }

    @SuppressWarnings("unchecked")
    public User deserializeUser(String str) throws IOException, ClassNotFoundException {
        byte[] bytes = Base64.getDecoder().decode(str);
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        ObjectInputStream ois = new ObjectInputStream(bais);

        return (User) ois.readObject();
    }

    public Chat deserializeChat(String str) throws IOException, ClassNotFoundException {
        byte[] bytes = Base64.getDecoder().decode(str);
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        ObjectInputStream ois = new ObjectInputStream(bais);

        return (Chat) ois.readObject();
    }

    // public DirectMessage deserializeDM(String str) throws IOException,
    // ClassNotFoundException {
    // byte[] bytes = Base64.getDecoder().decode(str);
    // ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
    // ObjectInputStream ois = new ObjectInputStream(bais);

    // return (DirectMessage) ois.readObject();
    // }

    // public Group deserializeGroup(String str) throws IOException,
    // ClassNotFoundException {
    // byte[] bytes = Base64.getDecoder().decode(str);
    // ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
    // ObjectInputStream ois = new ObjectInputStream(bais);

    // return (Group) ois.readObject();
    // }

    public void signUp(String username, String name, int age, String password) {
        awaitingResponse = true;
        writer.println("signup " + username + " !" + name + "! " + age + " " + password);
        writer.flush();
    }

    public void login(String username, String password) {
        awaitingResponse = true;
        writer.println("login " + username + " " + password);
        writer.flush();
    }

    public void sendMessage(String message) {
        awaitingResponse = true;
        UUID chat_id = MessagingGUI.currentChat.getId();
        writer.println("send_message " + chat_id + " " + message);
        writer.flush();
    }

    public void editMessage(String chat_id, String message_id, String newMessage) {
        writer.println("edit_message " + chat_id + " " + message_id + " " + newMessage);
        writer.flush();
    }

    public void removeMessage(String chat_id, String message_id) {
        writer.println("remove_message " + chat_id + " " + message_id);
        writer.flush();
    }

    public void createDm(String other_Username) {
        awaitingResponse = true;
        writer.println("create_dm " + other_Username);
        writer.flush();
    }

    public void createGroup(String group_name) {
        awaitingResponse = true;
        writer.println("create_group " + group_name);
        writer.flush();
    }

    public void addUser(String other_username) {
        UUID group_id = MessagingGUI.currentChat.getId();
        writer.println("add_user_to_group " + group_id + " " + other_username);
        writer.flush();
    }

    public void removeUser(String other_username) {
        UUID group_id = MessagingGUI.currentChat.getId();
        writer.println("remove_user_to_group " + group_id + " " + other_username);
        writer.flush();
    }

    public void removeChat(UUID group_id) {
        awaitingResponse = true;
        currentUser.removeChat(group_id);
        writer.println("remove_chat " + group_id);
        writer.flush();
        awaitingResponse = false;
    }

    public void requestInfo(String username) {
        this.awaitingResponse = true;
        writer.println("request_info " + username);
        writer.flush();
    }

    public void editName(String new_name) {
        writer.println("edit_name !" + new_name + "!");
        writer.flush();
    }

    public void editAge(String new_age) {
        writer.println("edit_age " + new_age);
        writer.flush();
    }

    public void editPassword(String old_password, String new_password) {
        writer.println("edit_password " + old_password + " " + new_password);
        writer.flush();
    }

    public void deleteAccount() {
        writer.println("delete_account " + currentUser.getUsername() + " " + currentUser.getPassword());
        writer.flush();
        this.currentUser = null;
        this.error = false;
        this.closeGUI = true;
    }

    public void signOut() {
        this.currentUser = null;
        this.error = false;
        this.awaitingResponse = false;
        writer.println("signout");
        writer.flush();
    }

    public void closeConnection() {
        writer.println("end_connection");
        writer.flush();
    }

    public String determineObject(String line) {
        String objectIdentifier;
        objectIdentifier = line.substring(0, 1);
        String newline = line.substring(2);
        switch (objectIdentifier) {
            case "@":
                currentObject = "User";
                break;
            case "$":
                currentObject = "Chat";
                // currentObject = "DM";
                break;
            case "%":
                currentObject = "Group";
                break;
            case "^":
                currentObject = "Command";
                break;
            case "!":
                currentObject = "Error";
                return line;
        }
        return newline;
    }

    public static void main(String[] args) {
        NetworkClient nc = new NetworkClient();
        nc.receiveServerInput.set(true);

        JOptionPane.showMessageDialog(null, "Welcome to the GamerStage", "GamerStage.live", JOptionPane.PLAIN_MESSAGE);
        try {
            nc.socket = new Socket("localHost", 4242);
            JOptionPane.showMessageDialog(null, "Connection Established.", "GamerStage.live",
                    JOptionPane.INFORMATION_MESSAGE);

            InputStreamReader i = new InputStreamReader(nc.socket.getInputStream());
            nc.reader = new BufferedReader(i);
            try {
                nc.writer = new PrintWriter(nc.socket.getOutputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error. Connection was not established successfully. Exiting Program",
                    "GamerStage.live", JOptionPane.ERROR_MESSAGE);
            return;
        }

        MainMenu mainMenu = new MainMenu(nc);
        mainMenu.run();
        String line = "";

        while (nc.receiveServerInput.get()) {
            try {
                line = nc.reader.readLine();
            } catch (SocketException se) {
                JOptionPane.showMessageDialog(null, "Error. Connection Reset. Please Restart Program.",
                        "GamerStage.live", JOptionPane.ERROR_MESSAGE);
                nc.receiveServerInput.set(false);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            if (line == null)
                break;

            line = nc.determineObject(line);
            switch (nc.currentObject) {
                case "User":
                    nc.error = false;
                    try {
                        nc.currentUser = nc.deserializeUser(line);
                    } catch (IOException | ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                    break;
                case "Chat":
                    nc.error = false;
                    try {
                        Chat chat = nc.deserializeChat(line);
                        if (nc.currentUser.getChats().containsKey(chat.getId())) {
                            nc.currentUser.getChats().remove(chat.getId());
                        }
                        nc.currentUser.addChat(chat);
                        nc.newChat = true;

                    } catch (IOException | ClassNotFoundException ioException) {
                        ioException.printStackTrace();
                    }
                    break;
                case "Command":
                    nc.error = false;
                    String temp = line.substring(line.indexOf("!") + 1);
                    String name = temp.substring(0, temp.indexOf("!"));

                    nc.setRequestedName(name);
                    nc.setRequestedAge(temp.substring(temp.indexOf("!") + 2));

                    break;
                case "Error":
                    nc.error = true;

                    switch (line) {
                        case "!username_taken":
                            nc.errormsg = "That Username Is Taken. Please Try A Different One.";
                            break;
                        case "!invalid_login":
                            nc.errormsg = "Incorrect Login, Please Try Again.";
                            break;
                        case "!user_not_found":
                            nc.errormsg = "This Username Was Not Found.";
                            break;
                        case "!invalid_command":
                            nc.errormsg = "Invalid Command, Please Try Again.";
                            break;
                    }
                    break;
            }

            nc.awaitingResponse = false;

        }
        JOptionPane.showMessageDialog(null,
                "Thank You For Using GamerStage. I Hope To See You Back On The Battlefield Soon!", "GamerStage.live",
                JOptionPane.PLAIN_MESSAGE);
        nc.closeGUI = true;
        try {
            nc.reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        nc.writer.close();
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public void setWriter(PrintWriter writer) {
        this.writer = writer;
    }

    public void setReader(BufferedReader reader) {
        this.reader = reader;
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    public void setRequestedName(String name) {
        this.requestedName = name;
    }

    public void setRequestedAge(String age) {
        this.requestedAge = age;
    }

    public Socket getSocket() {
        return this.socket;
    }

}
