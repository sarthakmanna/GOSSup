package ClientSide;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;

public class ClientAPI {
    public static final String STATIC_SERVER_IP = "192.168.0.15"; //*/"13.127.194.179";
    public static final int PORT = 7777;

    public final static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

    public static final String READY = "RD", SUCCESSFUL_LOGIN = "LG",
            WRONG_PASSWORD = "WP", USERNAME_NOT_FOUND_CREATE = "NF",
            YES = "Y", NO = "N";

    public static final String SEND = "SN", REFRESH_ALL = "RA",
            REFRESH_PERSONAL = "RP", BROADCAST = "BR", GET_ALL_USERS = "AL",
            GET_ONLINE_USERS = "CN", GET_RECENT_USERS = "RE",
            GET_ALL_USERNAMES = "AU", GET_ONLINE_USERNAMES = "OU",
            GET_RECENT_USERNAMES = "RU", GET_USER_DETAILS = "UD",
            GET_TOTAL_UNREAD_COUNT = "TC", GET_UNREAD_COUNT = "UC",
            GET_MESSAGES_AFTER_TIME = "MT", GET_LAST_K_MESSAGES = "LK";


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
        System.out.println(username + " " + password);

        String uniqueID = encrypt(username + " " + password);

        outputStream.writeUTF(encrypt(username));
        outputStream.writeUTF(encrypt(uniqueID));

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

        outputStream.writeUTF(encrypt(username));
        outputStream.writeUTF(encrypt(uniqueID));

        String response = inputStream.readUTF();
        if (response.equals(USERNAME_NOT_FOUND_CREATE)) {
            outputStream.writeUTF(createIfAbsent ? YES : NO);
            response = inputStream.readUTF();
        }

        return response;
    }

    public void sendMessage(String toUser, String message) throws Exception {
        outputStream.writeUTF(SEND);
        outputStream.writeUTF(encrypt(toUser));
        outputStream.writeUTF(encrypt(message));
    }

    public void broadCastMessage(String message) throws Exception {
        outputStream.writeUTF(BROADCAST);
        outputStream.writeUTF(encrypt(message));
    }

    public ArrayList<Message> getPersonalChatHistory(String username) throws Exception {
        outputStream.writeUTF(REFRESH_PERSONAL);
        outputStream.writeUTF(encrypt(username));

        ArrayList<Message> personalInbox = new ArrayList<>();

        int i, messageCount = Integer.parseInt(inputStream.readUTF());
        for (i = 0; i < messageCount; ++i) {
            Message message = new Message(
                    decrypt(inputStream.readUTF()),
                    inputStream.readUTF(),
                    decrypt(inputStream.readUTF()),
                    inputStream.readUTF());
            personalInbox.add(message);
        }

        return personalInbox;
    }

    public HashMap<String, ArrayList<Message>> getAllChatHistory() throws Exception {
        outputStream.writeUTF(REFRESH_ALL);

        HashMap<String, ArrayList<Message>> fullChatHistory = new HashMap<>();

        int i, j, userCount = Integer.parseInt(inputStream.readUTF().trim());
        for (i = 0; i < userCount; ++i) {
            String username = decrypt(inputStream.readUTF());
            ArrayList<Message> chats = new ArrayList<>();

            int messageCount = Integer.parseInt(inputStream.readUTF().trim());
            for (j = 0; j < messageCount; ++j) {
                Message message = new Message(
                        decrypt(inputStream.readUTF()),
                        inputStream.readUTF(),
                        decrypt(inputStream.readUTF()),
                        inputStream.readUTF());
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
            User user = new User(
                    decrypt(inputStream.readUTF()),
                    inputStream.readUTF(),
                    inputStream.readUTF());
            users.add(user);
        }

        return users;
    }

    public ArrayList<User> getAllUserDetails() throws Exception {
        outputStream.writeUTF(GET_ALL_USERS);

        ArrayList<User> users = new ArrayList<>();
        int i, userCount = Integer.parseInt(inputStream.readUTF().trim());

        for (i = 0; i < userCount; ++i) {
            User user = new User(
                    decrypt(inputStream.readUTF()),
                    inputStream.readUTF(),
                    inputStream.readUTF());
            users.add(user);
        }

        return users;
    }

    public ArrayList<User> getOnlineUserDetails() throws Exception {
        outputStream.writeUTF(GET_ONLINE_USERS);

        ArrayList<User> users = new ArrayList<>();
        int i, userCount = Integer.parseInt(inputStream.readUTF());

        for (i = 0; i < userCount; ++i) {
            User user = new User(
                    decrypt(inputStream.readUTF()),
                    inputStream.readUTF(),
                    inputStream.readUTF());
            users.add(user);
        }

        return users;
    }

    public ArrayList<String> getRecentUsernames() throws Exception {
        outputStream.writeUTF(GET_RECENT_USERNAMES);

        ArrayList<String> users = new ArrayList<>();
        int i, userCount = Integer.parseInt(inputStream.readUTF());

        for (i = 0; i < userCount; ++i) {
            users.add(decrypt(inputStream.readUTF()));
        }

        return users;
    }

    public ArrayList<String> getAllUsernames() throws Exception {
        outputStream.writeUTF(GET_ALL_USERNAMES);

        ArrayList<String> users = new ArrayList<>();
        int i, userCount = Integer.parseInt(inputStream.readUTF());

        for (i = 0; i < userCount; ++i) {
            users.add(decrypt(inputStream.readUTF()));
        }

        return users;
    }

    public ArrayList<String> getOnlineUsernames() throws Exception {
        outputStream.writeUTF(GET_ONLINE_USERNAMES);

        ArrayList<String> users = new ArrayList<>();
        int i, userCount = Integer.parseInt(inputStream.readUTF());

        for (i = 0; i < userCount; ++i) {
            users.add(decrypt(inputStream.readUTF()));
        }

        return users;
    }

    public User getUserDetails(String username) throws Exception {
        outputStream.writeUTF(GET_USER_DETAILS);
        outputStream.writeUTF(encrypt(username));

        User userDetails = new User(username, inputStream.readUTF(),
                inputStream.readUTF());
        return userDetails;
    }

    public int getTotalUnreadMessageCount() throws Exception {
        outputStream.writeUTF(GET_TOTAL_UNREAD_COUNT);
        return Integer.parseInt(inputStream.readUTF());
    }

    public int getUnreadMessageCount(String username) throws Exception {
        outputStream.writeUTF(GET_UNREAD_COUNT);
        outputStream.writeUTF(username);
        return Integer.parseInt(inputStream.readUTF());
    }

    public ArrayList<Message> getMessageAfterTime(String username, long time)
            throws Exception {
        outputStream.writeUTF(GET_MESSAGES_AFTER_TIME);
        outputStream.writeUTF(username);
        outputStream.writeUTF(time + "");

        ArrayList<Message> messages = new ArrayList<>();

        int i, messageCount = Integer.parseInt(inputStream.readUTF());
        for (i = 0; i < messageCount; ++i) {
            Message message = new Message(
                    decrypt(inputStream.readUTF()),
                    inputStream.readUTF(),
                    decrypt(inputStream.readUTF()),
                    inputStream.readUTF());
            messages.add(message);
        }

        return messages;
    }

    public ArrayList<Message> getLastKMessages(String username, int K)
            throws Exception {
        outputStream.writeUTF(GET_LAST_K_MESSAGES);
        outputStream.writeUTF(username);
        outputStream.writeUTF(K + "");

        ArrayList<Message> messages = new ArrayList<>();

        int i, messageCount = Integer.parseInt(inputStream.readUTF());
        for (i = 0; i < messageCount; ++i) {
            Message message = new Message(
                    decrypt(inputStream.readUTF()),
                    inputStream.readUTF(),
                    decrypt(inputStream.readUTF()),
                    inputStream.readUTF());
            messages.add(message);
        }

        return messages;
    }


    private String encrypt(String originalString) {
        return "$$$" + originalString;
    }

    private String decrypt(String originalString) {
        return originalString.substring(3);
    }
}