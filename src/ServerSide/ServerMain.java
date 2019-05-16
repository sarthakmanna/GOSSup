package ServerSide;

import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ServerMain {
    public final static int PORT = 7777;
    public final static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

    private ServerSocket serverSocket;

    public ServerMain() throws Exception {
        serverSocket = new ServerSocket(PORT);
    }

    public void startServer() throws Exception {
        for (int i = 1; ; ++i) {
            System.out.println(i + ".");
            System.out.println("Waiting for a client to connect...");
            Socket client = serverSocket.accept();
            System.out.println("[" + DATE_FORMAT.format(new Date()) + "]");
            System.out.println("Client with IP Address "
                    + client.getRemoteSocketAddress() + " has connected.");

            System.out.println("Assigning a separate server to assist the client...");
            new ServerSide.Server(client).start();
            System.out.println("A new server has been assigned successfully.\n");
        }
    }

    public static void main(String[] args) throws Exception {
        new ServerMain().startServer();
    }
}