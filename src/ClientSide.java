import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

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
            inputStream.readUTF();
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

    void sendMessage(String toUser, String message) throws Exception {
        outputStream.writeUTF(SEND);
        outputStream.writeUTF(toUser);
        outputStream.writeUTF(message);
    }

    ArrayList<Message> refreshPersonalChatHistory(String username) throws Exception {
        outputStream.writeUTF(REFRESH_PERSONAL);
        outputStream.writeUTF(username);

        ArrayList<Message> personalInbox = new ArrayList<>();

        int i, messageCount = Integer.parseInt(inputStream.readUTF());
        for (i = 0; i < messageCount; ++i) {
            Message message = new Message(inputStream.readUTF(), inputStream.readUTF(),
                    inputStream.readUTF(), inputStream.readUTF());
            personalInbox.add(message);
        }

        return personalInbox;
    }

    HashMap<String, ArrayList<Message>> refreshAllChatHistory() throws Exception {
        outputStream.writeUTF(REFRESH_ALL);

        HashMap<String, ArrayList<Message>> fullChatHistory = new HashMap<>();

        int i, j, userCount = Integer.parseInt(inputStream.readUTF().trim());
        for (i = 0; i < userCount; ++i) {
            String username = inputStream.readUTF();
            ArrayList<Message> chats = new ArrayList<>();

            int messageCount = Integer.parseInt(inputStream.readUTF().trim());
            for (j = 0; j < messageCount; ++i) {
                Message message = new Message(inputStream.readUTF(), inputStream.readUTF(),
                        inputStream.readUTF(), inputStream.readUTF());
                chats.add(message);
            }

            fullChatHistory.put(username, chats);
        }
        return fullChatHistory;
    }

    void broadCastMessage(String message) throws Exception {
        outputStream.writeUTF(BROADCAST);
        outputStream.writeUTF(message);
    }

    ArrayList<String> getAllUsernames() throws Exception {
        outputStream.writeUTF(GET_ALL_USERNAMES);

        ArrayList<String> users = new ArrayList<>();
        int i, userCount = Integer.parseInt(inputStream.readUTF());

        for (i = 0; i < userCount; ++i) {
            users.add(inputStream.readUTF());
        }

        return users;
    }

    ArrayList<String> getOnlineUsernames() throws Exception {
        outputStream.writeUTF(GET_ONLINE_USERNAMES);

        ArrayList<String> users = new ArrayList<>();
        int i, userCount = Integer.parseInt(inputStream.readUTF());

        for (i = 0; i < userCount; ++i) {
            users.add(inputStream.readUTF());
        }

        return users;
    }


    private String encrypt(String originalString) {
        return originalString;
    }

    private String decrypt(String originalString) {
        return originalString;
    }
}

class Message {
    final String YES = "Y", NO = "N";
    String userInvolved, timeStamp, message;
    boolean isSent;

    Message(String username, String time, String msg, String isSendType) {
        userInvolved = username;
        timeStamp = time;
        message = msg;
        isSent = isSendType.equals(YES);
    }
}