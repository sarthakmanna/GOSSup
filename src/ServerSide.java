import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ServerSide {
    final static int PORT = 7777;
    final static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

    ServerSocket serverSocket;

    ServerSide() throws Exception {
        serverSocket = new ServerSocket(PORT);
    }

    void startServer() throws Exception {
        while (true) {
            System.out.println("Waiting for a client to connect...");
            Socket client = serverSocket.accept();
            System.out.println("[" + DATE_FORMAT.format(new Date()) + "]");
            System.out.println("Client with IP Address "
                    + client.getRemoteSocketAddress() + " has connected.");

            System.out.println("Assigning a separate server to assist the client...");
            new Server(client).start();
            System.out.println("A new server has been assigned successfully.\n");
        }
    }

    public static void main(String[] args) throws Exception {
        new ServerSide().startServer();
    }
}