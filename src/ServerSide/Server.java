package ServerSide;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;

public class Server extends Thread {
    static final HashSet<String> ACTIVE_CLIENTS = new HashSet<>();
    static final HashMap<String, ClientDetails> DATABASE = new HashMap<>();

    private final String READY = "RD", SUCCESSFUL_LOGIN = "LG",
            WRONG_PASSWORD = "WP", USERNAME_NOT_FOUND_CREATE = "NF",
            YES = "Y", NO = "N";

    private final String SEND = "SN", REFRESH_ALL = "RA", REFRESH_PERSONAL = "RP",
            BROADCAST = "BR", GET_ALL_USERS = "AL", GET_ONLINE_USERS = "CN",
            GET_RECENT_USERS = "GF";


    private Socket clientSocket;
    private ClientDetails clientDetails;

    private DataInputStream inputStream;
    private DataOutputStream outputStream;

    public Server(Socket client) throws Exception {
        clientSocket = client;

        inputStream = new DataInputStream(client.getInputStream());
        outputStream = new DataOutputStream(client.getOutputStream());

        outputStream.writeUTF(READY);
        // Client is expected to send the username and hash(username + password)
    }

    @Override
    public void run() {
        try {
            login();

            ACTIVE_CLIENTS.add(clientDetails.getUsername());

            while (clientSocket.isConnected()) {
                String command = inputStream.readUTF();

                switch (command) {
                    case SEND: sendMessage(); break;
                    case REFRESH_ALL: refreshAllChatHistory(); break;
                    case REFRESH_PERSONAL: refreshPersonalChatHistory(); break;
                    case BROADCAST: broadcastMessage(); break;
                    case GET_ALL_USERS: getAllUsers(); break;
                    case GET_ONLINE_USERS: getOnlineUsers(); break;
                    case GET_RECENT_USERS: getRecentUsers(); break;
                    default: System.out.println("WRONG COMMAND: " + command);
                }
            }
            System.out.println("ClientSide.User " + clientDetails.getUsername()
                    + " has disconnected.");

            inputStream.close();
            outputStream.close();
            clientSocket.close();
        } catch (Exception e) {
        } finally {
            if (clientDetails != null) {
                clientDetails.setLastSeenTime(new Date().getTime());
                ACTIVE_CLIENTS.remove(clientDetails.getUsername());
            }
        }
    }

    private void login() throws Exception {
        while (true) {
            String username = inputStream.readUTF();
            // username sent by Client

            String uniqueid = inputStream.readUTF();
            // hash(username + password) sent by Client

            if (DATABASE.containsKey(username)) {
                ClientDetails targetClient = DATABASE.get(username);

                if (uniqueid.equals(targetClient.getUniqueID())) {
                    clientDetails = targetClient;
                    outputStream.writeUTF(SUCCESSFUL_LOGIN);
                    return;
                } else {
                    outputStream.writeUTF(WRONG_PASSWORD);
                }
            } else {
                outputStream.writeUTF(USERNAME_NOT_FOUND_CREATE);

                String response = inputStream.readUTF();

                if (response.equals(YES)) {
                    ClientDetails newClient = new ClientDetails(username, uniqueid);
                    DATABASE.put(username, newClient);
                    outputStream.writeUTF(SUCCESSFUL_LOGIN);
                    return;
                } else if (response.equals(NO)) {
                    outputStream.writeUTF(WRONG_PASSWORD);
                } else {
                    System.err.println("Error in communicating...");
                }
            }
        }
    }

    public void sendMessage() throws Exception {
        String destUsername = inputStream.readUTF();
        long timeStamp = new Date().getTime();
        String message = inputStream.readUTF();

        ClientDetails sender = clientDetails,
                receiver = DATABASE.get(destUsername);

        sender.sendMessage(destUsername, timeStamp, message);
        receiver.receiveMessage(destUsername, timeStamp, message);
    }

    public void refreshPersonalChatHistory() throws Exception {
        String withUsername = inputStream.readUTF();

        ArrayList<MessageDetails> messageList = clientDetails.getMessageList(withUsername);

        outputStream.writeUTF(messageList.size() + "");

        for (MessageDetails message : messageList) {
            outputStream.writeUTF(message.getUserInvolved());
            outputStream.writeUTF(message.getTimeStamp() + "");
            outputStream.writeUTF(message.getMessage());
            outputStream.writeUTF(message.isSent() ? YES : NO);
        }
    }

    public void refreshAllChatHistory() throws Exception {
        outputStream.writeUTF(DATABASE.size() + "");

        for (String withUsername : DATABASE.keySet()) {
            outputStream.writeUTF(withUsername);

            ArrayList<MessageDetails> messageList = clientDetails.getMessageList(withUsername);

            outputStream.writeUTF(messageList.size() + "");

            for (MessageDetails message : messageList) {
                outputStream.writeUTF(message.getUserInvolved());
                outputStream.writeUTF(message.getTimeStamp() + "");
                outputStream.writeUTF(message.getMessage());
                outputStream.writeUTF(message.isSent() ? YES : NO);
            }
        }
    }

    public void broadcastMessage() throws Exception {
        long timeStamp = new Date().getTime();
        String message = inputStream.readUTF();

        for (String destUsername : DATABASE.keySet()) {
            ClientDetails sender = clientDetails,
                    receiver = DATABASE.get(destUsername);

            sender.sendMessage(destUsername, timeStamp, message);
            receiver.receiveMessage(destUsername, timeStamp, message);
        }
    }

    public void getAllUsers() throws Exception {
        outputStream.writeUTF(DATABASE.size() + "");

        for (String user : DATABASE.keySet()) {
            ClientDetails userDetails = DATABASE.get(user);

            outputStream.writeUTF(userDetails.getUsername());
            outputStream.writeUTF(ACTIVE_CLIENTS.contains(user) ? "ONLINE" :
                    userDetails.getLastSeenTime() + "");
            outputStream.writeUTF(clientDetails.hasChattedBefore(user) ?
                    YES : NO);
        }
    }

    public void getOnlineUsers() throws Exception {
        outputStream.writeUTF(ACTIVE_CLIENTS.size() + "");

        for (String user : ACTIVE_CLIENTS) {
            ClientDetails userDetails = DATABASE.get(user);

            outputStream.writeUTF(userDetails.getUsername());
            outputStream.writeUTF(ACTIVE_CLIENTS.contains(user) ? "ONLINE" :
                    userDetails.getLastSeenTime() + "");
            outputStream.writeUTF(clientDetails.hasChattedBefore(user) ?
                    YES : NO);
        }
    }

    public void getRecentUsers() throws Exception {
        ArrayList<String> recentUsers = clientDetails.getFriends();
        outputStream.writeUTF(recentUsers.size() + "");

        for (String user : recentUsers) {
            ClientDetails userDetails = DATABASE.get(user);

            outputStream.writeUTF(userDetails.getUsername());
            outputStream.writeUTF(ACTIVE_CLIENTS.contains(user) ? "ONLINE" :
                    userDetails.getLastSeenTime() + "");
            outputStream.writeUTF(clientDetails.hasChattedBefore(user) ?
                    YES : NO);
        }
    }
}