package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.Socket;

public class ServerShutdown {
    private static final String HOST = "localhost";
    private static final int PORT_NUMBER = 4242;
    private static final String SHUTDOWN = "end_server";

    private static final String CLS_SERVER_SUC = "Sent Shutdown Message";

    private static final String SRVR_NOT_OPEN = "Server Is Not Open";
    private static final String CLS_SERVER_FAIL_IO = "Failed To Close Server | IOException";

    public static void main(String[] args) {
        try {
            Socket s = new Socket(HOST, PORT_NUMBER);
            BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            PrintWriter pw = new PrintWriter(s.getOutputStream());
            pw.write(SHUTDOWN);
            pw.println();
            pw.flush();
            System.out.println(CLS_SERVER_SUC);
            s.close();
        } catch (ConnectException e) {
            System.err.println(SRVR_NOT_OPEN);
        } catch (IOException e) {
            System.err.println(CLS_SERVER_FAIL_IO);
            e.printStackTrace();
        }
    }
}
