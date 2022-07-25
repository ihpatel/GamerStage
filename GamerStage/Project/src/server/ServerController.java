package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.LinkedList;
import java.util.List;

public class ServerController {

    public static boolean serverOnline = false;
    private static ServerSocket server;
    private static final int PORT_NUMBER = 4242;

    private static final String SRVR_OPEN = "Server Opened Successfully";
    private static final String SRVR_CLOSE = "Server Closed";

    private static final String SRVR_ERR = "Unexcpected Error";
    private static final String SRVR_CLOSE_ERR = "Unable To Close Server: ";

    private static final String CLNT_CNCT = "A Client Has Connected";
    private static final String CLNT_DISCNCT = "A Client Has Disconnected";
    private static final String CLNT_ACCPT_ERR = "Unable To Accept Client: ";

    private static List<ClientThread> connectedUsers = new LinkedList<>();
    private static boolean stopRequest = false;

    public static void addUser(ClientThread clientThread) {
        System.out.println(CLNT_CNCT);
        connectedUsers.add(clientThread);
    }

    public static List<ClientThread> getConnectedUsers() {
        return connectedUsers;
    }

    public static boolean removeUser(ClientThread clientThread) {
        System.out.println(CLNT_DISCNCT); 
        return connectedUsers.remove(clientThread);
    }

    public static void notify(String message) {
        System.out.println(message);
    }

    public synchronized static boolean close() {
        if (connectedUsers.size() > 1) {
            notify("Can't Close Server: Users Still Connected");
            return false;
        }

        try {
            stopRequest = true;
            server.close();
            return true;
        } catch (IOException e) {
            System.err.println(SRVR_CLOSE_ERR);
            e.printStackTrace();
            return false;
        }
    }

    private static void start() {
        try {

            server = new ServerSocket(PORT_NUMBER);

            System.out.println(SRVR_OPEN);

            while (!stopRequest) {
                try {
                    Socket clientSocket = server.accept();

                    ClientThread clientThread = new ClientThread(clientSocket);

                    clientThread.start();

                } catch (SocketException e) {
                    if (!stopRequest) {
                        System.err.println(CLNT_ACCPT_ERR);
                        break;
                    }
                }

            }
            
        } catch (IOException e) {
            System.err.println(SRVR_ERR);
            e.printStackTrace();
        } finally {
            Database.writeData();
            System.out.println(SRVR_CLOSE);
        }
    }

    public static void main(String[] args) {

        ServerController.start();

    }

}

