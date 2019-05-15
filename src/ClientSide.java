import java.io.*;
import java.net.Socket;

public class ClientSide {
    final String READY = "RD", SUCCESSFUL_LOGIN = "LG",
            WRONG_PASSWORD = "WP", USERNAME_NOT_FOUND_CREATE = "NF",
            YES = "Y", NO = "N";

    final String SEND = "SN", REFRESH_ALL = "RA", REFRESH_PERSONAL = "RP",
            BROADCAST = "BR", GET_ALL_USERNAMES = "AL", GET_ONLINE_USERNAMES = "CN";


    Socket serverSocket;

    DataInputStream inputStream;
    DataOutputStream outputStream;

    ClientSide(String ip, int port) throws Exception {
        serverSocket = new Socket(ip, port);
        inputStream = new DataInputStream(serverSocket.getInputStream());
        outputStream = new DataOutputStream(serverSocket.getOutputStream());

        if (!inputStream.readUTF().equals(READY)) {
            System.out.println("Error in Server !!! Process terminated...");
            System.exit(1);
        }
    }

    String attemptLogin(String username, String password) throws Exception {
        String uniqueID = encrypt(username + " " + password);

        outputStream.writeUTF(username);
        outputStream.writeUTF(uniqueID);

        String response = inputStream.readUTF();
        if (response.equals(USERNAME_NOT_FOUND_CREATE)) {
            outputStream.writeUTF(NO);
        }

        return response;
    }

    String attemptLogin(String username, String password, boolean createIfAbsent)
            throws Exception {
        System.out.println(username + " " + password + " " + createIfAbsent);

        String uniqueID = encrypt(username + " " + password);

        outputStream.writeUTF(username);
        outputStream.writeUTF(uniqueID);

        String response = inputStream.readUTF();
        if (response.equals(USERNAME_NOT_FOUND_CREATE)) {
            outputStream.writeUTF(createIfAbsent ? YES : NO);
            response = inputStream.readUTF();
        }

        return response;
    }

    private String encrypt(String originalString) {
        return originalString;
    }

    private String decrypt(String originalString) {
        return originalString;
    }






    void startProcess() throws Exception {
        System.out.println("Process started successfully...");
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String response;

        do {
            System.out.print("Choice: ");

            if (br.readLine().equals("1")) {
                response = attemptLogin(br.readLine(), br.readLine());
            } else {
                response = attemptLogin(br.readLine(), br.readLine(), br.readLine().equals("1"));
            }

            System.out.println(response);
        } while (!response.equals(SUCCESSFUL_LOGIN));

        System.out.println("Successful login");
    }

    public static void main(String[] args) throws Exception {
        new ClientSide("192.168.0.15", 7777).startProcess();
    }
}