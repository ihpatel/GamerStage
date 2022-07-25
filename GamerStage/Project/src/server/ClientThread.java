package server;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Base64;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

import classes.Chat;
import classes.DirectMessage;
import classes.Group;
import classes.User;
import server.errors.DuplicateUsernameException;
import server.errors.InvalidCommandException;
import server.errors.InvalidLoginException;
import server.errors.NotFoundException;

public class ClientThread extends Thread {

    private final Socket clientSocket;

    private BufferedReader in;
    private PrintWriter out;

    private AtomicBoolean running;

    private String username;
    private boolean initialized;

    private final static String NTFY_LOGGED_IN = "A User Has Logged In: %s";
    private final static String NTFY_SIGNED_UP = "A User Has Signed Up: %s";
    private final static String NTFY_LOGGED_OUT = "A User Has Logged Out: %s";

    /* V Inputs V */
    private final static String CLSE_SERVER = "end_server";
    private final static String CLS_CONNECTION = "end_connection";
    private final static String SIGN_UP = "signup";
    private final static String LOG_IN = "login";
    private final static String SIGN_OUT = "signout";
    private final static String DELETE_ACCOUNT = "delete_account";

    private final static String SEND_MSG = "send_message";
    private final static String EDIT_MSG = "edit_message";
    private final static String DELETE_MSG = "remove_message";
    private final static String CREATE_GRP = "create_group";
    private final static String CREATE_DM = "create_dm";
    private final static String ADD_TO_GROUP = "add_user_to_group";
    private final static String REMOVE_CHAT = "remove_chat";
    private final static String REQUEST_INFO = "request_info";

    /* V Outputs V */
    private final static String USER_PREFIX = "@ ";
    private final static String DM_PREFIX = "$ ";
    private final static String GROUP_PREFIX = "% ";
    private final static String FETCH_INFO_PREFIX = "^ ";

    private final static String DUPL_USER = "!username_taken";
    private final static String STRM_FAIL = "!stream_failed";
    private final static String INVAL_LOGIN = "!invalid_login";
    private final static String NOT_FOUND = "!%s_not_found";
    private final static String INVAL_CMD = "!invalid_command";

    public ClientThread(Socket s) {
        this.clientSocket = s;
        this.username = "";
        this.running = new AtomicBoolean(true);
        this.initialized = false;
        ServerController.addUser(this);
        Database.initializeData();
    }

    public String getUsername() {
        return username;
    }

    public PrintWriter getOut() {
        return out;
    }

    public void initializeUser(String input) throws IOException {

        User user;
        StringBuilder sb;
        String[] temp = input.split("\\s");

        try {
            switch (temp[0]) {
                case SIGN_UP:
                
                    String almostName = input.substring(input.indexOf("!") + 1);
                    String name = almostName.substring(0, almostName.indexOf("!"));
                    


                    String[] newtemp = almostName.substring(almostName.indexOf("!") + 2).split(" ");

                    user = Database.signUp(temp[1], name, Integer.parseInt(newtemp[0]), newtemp[1]);
                    sb = new StringBuilder(USER_PREFIX);
                    sb.append(serializeUser(user));
                    this.username = user.getUsername();
                    ServerController.notify(String.format(NTFY_SIGNED_UP, this.username));
                    this.initialized = true;
                    out.println(sb.toString());
                    out.flush();
                    break;
                case LOG_IN:
                    if (temp.length != 3)
                        throw new InvalidCommandException();

                    user = Database.login(temp[1], temp[2]);
                    sb = new StringBuilder(USER_PREFIX);
                    sb.append(serializeUser(user));
                    this.username = user.getUsername();
                    ServerController.notify(String.format(NTFY_LOGGED_IN, this.username));
                    this.initialized = true;
                    out.println(sb.toString());
                    out.flush();
                    break;
                default:
                    out.println(INVAL_CMD);
            }
        } catch (DuplicateUsernameException e) {
            out.println(DUPL_USER);
            out.flush();
        } catch (InvalidLoginException e) {
            out.println(INVAL_LOGIN);
            out.flush();
        } catch (InvalidCommandException e) {
            out.println(INVAL_CMD);
            out.flush();
        } catch (NotFoundException e) {
            out.println(String.format(NOT_FOUND, e.getMessage()));
            out.flush();
        } catch (IOException e) {
            out.println(STRM_FAIL);
            out.flush();
        }

    }

    private void handleUser(String input) {
        Group g;
        DirectMessage dm;
        StringBuilder sb;
        String[] msTemp;
        String[] temp = input.split("\\s");

        try {
            switch (temp[0]) {
                case SEND_MSG:
                    msTemp = input.split(" ", 3);
                    Database.sendMessage(this.username, UUID.fromString(temp[1]), msTemp[2]);
                    break;
                case EDIT_MSG:
                    msTemp = input.split(" ", 4);
                    Database.editMessage(this.username, UUID.fromString(temp[1]), UUID.fromString(temp[2]), msTemp[3]);
                    break;
                case DELETE_MSG:
                    if (temp.length != 3)
                        throw new InvalidCommandException();
                    Database.removeMessage(this.username, UUID.fromString(temp[1]), UUID.fromString(temp[2]));
                    break;
                case "edit_name":
                    String name = input.substring(input.indexOf("!") + 1);
                    name = name.substring(0, name.indexOf("!"));
                    Database.editName(this.username, name);
                    break;
                case "edit_age":
                    if (temp.length != 2) 
                        throw new InvalidCommandException();
                    Database.editAge(this.username, Integer.parseInt(temp[1]));
                    break;
                case "edit_password":
                    if (temp.length != 3) 
                        throw new InvalidCommandException();
                    Database.editPassword(this.username, temp[1], temp[2]);
                    break;
                case CREATE_DM:
                    if (temp.length != 2)
                        throw new InvalidCommandException();
                    dm = Database.createDM(this.username, temp[1]);
                    sb = new StringBuilder("$ ");
                    sb.append(serializeChat(dm));
                    out.println(sb.toString());
                    out.flush();
                    break;
                case CREATE_GRP:
                    if (temp.length != 2)
                        throw new InvalidCommandException();
                    g = Database.createGroup(this.username, temp[1]);
                    sb = new StringBuilder("$ ");
                    sb.append(serializeChat(g));
                    out.println(sb.toString());
                    out.flush();
                    break;
                case ADD_TO_GROUP:
                    if (temp.length != 3)
                        throw new InvalidCommandException();
                    Database.addUserToGroup(this.username, UUID.fromString(temp[1]), temp[2]);
                    break;
                case REMOVE_CHAT:
                    if (temp.length != 2)
                        throw new InvalidCommandException();
                    Database.removeUserFromGroup(this.username, UUID.fromString(temp[1]));
                    break;
                case REQUEST_INFO:
                    if (temp.length != 2)
                        throw new InvalidCommandException();
                    sb = new StringBuilder(FETCH_INFO_PREFIX);
                    sb.append(Database.requestInfo(temp[1]));
                    out.println(sb.toString());
                    out.flush();
                    break;
                case SIGN_OUT:
                    ServerController.notify(String.format(NTFY_LOGGED_OUT, this.username));
                    Database.signOut(this.username);
                    this.initialized = false;
                    this.username = "";
                    break;
                case DELETE_ACCOUNT:
                    Database.deleteAccount(temp[1], temp[2]);
                    this.initialized = false;
                    ServerController.notify(String.format(NTFY_LOGGED_OUT, this.username));
                    break;
                default:
                    out.println(INVAL_CMD);
                    out.flush();
            }
        } catch (InvalidLoginException e) {
            out.println(INVAL_LOGIN);
            out.flush();
        } catch (InvalidCommandException e) {
            out.println(INVAL_CMD);
            out.flush();
        } catch (NotFoundException e) {
            out.println(String.format(NOT_FOUND, e.getMessage()));
            out.flush(); 
        } catch (IOException e) {
            out.println(STRM_FAIL);
            out.flush();
        }
    }

    public String serializeUser(User user) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(user);
        oos.flush();
        return Base64.getEncoder().encodeToString(baos.toByteArray());
    }

    public String serializeChat(Chat chat) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(chat);
        oos.flush();
        return Base64.getEncoder().encodeToString(baos.toByteArray());
    }

    public void stopClient() {
        // Print ServerClosed Command
        running.set(false);
        this.out.close();
        try {
            this.in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.interrupt();
    }

    @Override
    public void run() {

        try {
            this.out = new PrintWriter(clientSocket.getOutputStream());
            this.in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            String response = in.readLine();

            while (running.get()) {

                if (response.equals(CLSE_SERVER)) {
                    ServerController.close();
                    ServerController.removeUser(this);
                    this.stopClient();
                    break;
                } else if (response.equals(CLS_CONNECTION)) {
                    ServerController.removeUser(this);
                    this.stopClient();
                    break;
                }

                if (!initialized)
                    initializeUser(response);
                else
                    handleUser(response);

                response = in.readLine();
            }

        } catch (IOException ioe) {
            out.println(STRM_FAIL);
        } finally {
            try {
                this.in.close();
                this.out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

}
