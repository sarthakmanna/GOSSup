package ClientSide;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

public class ClientAPI {
    public static final String STATIC_SERVER_IP = "13.157.194.179";
    public static final int PORT = 7777;

    public static final String READY = "RD", SUCCESSFUL_LOGIN = "LG",
            WRONG_PASSWORD = "WP", USERNAME_NOT_FOUND_CREATE = "NF",
            YES = "Y", NO = "N";

    public static final String SEND = "SN", REFRESH_ALL = "RA", REFRESH_PERSONAL = "RP",
            BROADCAST = "BR", GET_ALL_USERS = "AL", GET_ONLINE_USERS = "CN",
            GET_RECENT_USERS = "RE", GET_ALL_USERNAMES = "AU",
            GET_ONLINE_USERNAMES = "OU", GET_RECENT_USERNAMES = "RU";


    private Socket serverSocket;

    private DataInputStream inputStream;
    private DataOutputStream outputStream;

    public ClientAPI() throws Exception {
        serverSocket = new Socket(STATIC_SERVER_IP, PORT);
        inputStream = new DataInputStream(serverSocket.getInputStream());
        outputStream = new DataOutputStream(serverSocket.getOutputStream());

        if (!inputStream.readUTF().equals(READY)) {
            System.out.println("Error in ServerSide.Server !!! Process terminated...");
            System.exit(1);
        }
    }

    public String attemptLogin(String username, String password) throws Exception {
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

    public String attemptLogin(String username, String password, boolean createIfAbsent)
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

    public void sendMessage(String toUser, String message) throws Exception {
        outputStream.writeUTF(SEND);
        outputStream.writeUTF(toUser);
        outputStream.writeUTF(message);
    }

    public void broadCastMessage(String message) throws Exception {
        outputStream.writeUTF(BROADCAST);
        outputStream.writeUTF(message);
    }

    public ArrayList<Message> refreshPersonalChatHistory(String username) throws Exception {
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

    public HashMap<String, ArrayList<Message>> refreshAllChatHistory() throws Exception {
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

    public ArrayList<User> getRecentUserDetails() throws Exception {
        outputStream.writeUTF(GET_RECENT_USERS);

        ArrayList<User> users = new ArrayList<>();
        int i, userCount = Integer.parseInt(inputStream.readUTF().trim());

        for (i = 0; i < userCount; ++i) {
            User user = new User(inputStream.readUTF(),
                    inputStream.readUTF(), inputStream.readUTF());
            users.add(user);
        }

        return users;
    }

    public ArrayList<User> getAllUserDetails() throws Exception {
        outputStream.writeUTF(GET_ALL_USERS);

        ArrayList<User> users = new ArrayList<>();
        int i, userCount = Integer.parseInt(inputStream.readUTF().trim());

        for (i = 0; i < userCount; ++i) {
            User user = new User(inputStream.readUTF(),
                    inputStream.readUTF(), inputStream.readUTF());
            users.add(user);
        }

        return users;
    }

    public ArrayList<User> getOnlineUserDetails() throws Exception {
        outputStream.writeUTF(GET_ONLINE_USERS);

        ArrayList<User> users = new ArrayList<>();
        int i, userCount = Integer.parseInt(inputStream.readUTF());

        for (i = 0; i < userCount; ++i) {
            User user = new User(inputStream.readUTF(),
                    inputStream.readUTF(), inputStream.readUTF());
            users.add(user);
        }

        return users;
    }

    public ArrayList<String> getRecentUsernames() throws Exception {
        outputStream.writeUTF(GET_RECENT_USERNAMES);

        ArrayList<String> users = new ArrayList<>();
        int i, userCount = Integer.parseInt(inputStream.readUTF());

        for (i = 0; i < userCount; ++i) {
            users.add(inputStream.readUTF());
        }

        return users;
    }

    public ArrayList<String> getAllUsernames() throws Exception {
        outputStream.writeUTF(GET_ALL_USERNAMES);

        ArrayList<String> users = new ArrayList<>();
        int i, userCount = Integer.parseInt(inputStream.readUTF());

        for (i = 0; i < userCount; ++i) {
            users.add(inputStream.readUTF());
        }

        return users;
    }

    public ArrayList<String> getOnlineUsernames() throws Exception {
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