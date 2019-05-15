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

    final String READY = "RD", SUCCESSFUL_LOGIN = "LG",
            WRONG_PASSWORD = "WP", USERNAME_NOT_FOUND_CREATE = "NF",
            YES = "Y", NO = "N";

    Socket clientSocket;
    ClientDetails clientDetails;

    DataInputStream inputStream;
    DataOutputStream outputStream;

    Server(Socket client) throws Exception {
        clientSocket = client;

        inputStream = new DataInputStream(client.getInputStream());
        outputStream = new DataOutputStream(client.getOutputStream());

        outputStream.writeUTF(READY);
        // Client is asked to send the username and hash(username + password)

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
            } else{
                outputStream.writeUTF(USERNAME_NOT_FOUND_CREATE);
                String response = inputStream.readUTF();

                if (response.equals(YES)) {
                    ClientDetails newClient = new ClientDetails(username, uniqueid);
                    DATABASE.put(username, newClient);
                    outputStream.writeUTF(SUCCESSFUL_LOGIN);
                    return;
                }
            }
        }
    }

    final String SEND = "SN", REFRESH_ALL = "RA", REFRESH_PERSONAL = "RP",
            BROADCAST = "BR", GET_ALL_USERNAMES = "AL", GET_ONLINE_USERNAMES = "CN";

    @Override
    public void run() {
        try {
            ACTIVE_CLIENTS.add(clientDetails.getUsername());

            while (clientSocket.isConnected()) {
                String command = inputStream.readUTF();

                if (command.equals(SEND)) {
                    sendMessage();
                } else if (command.equals(REFRESH_ALL)) {
                    refreshAllChatHistory();
                } else if (command.equals(REFRESH_PERSONAL)) {
                    refreshPersonalChatHistory();
                } else if (command.equals(BROADCAST)) {
                    broadcastMessage();
                } else if (command.equals(GET_ALL_USERNAMES)) {
                    getAllUsernames();
                } else if (command.equals(GET_ONLINE_USERNAMES)) {
                    getOnlineUsernames();
                } else {
                    System.out.println("WRONG COMMAND: " + command);
                }
            }
            System.out.println("User " + clientDetails.getUsername() + " has disconnected.");

            inputStream.close();
            outputStream.close();
            clientSocket.close();
        } catch (Exception e) {
        } finally {
            ACTIVE_CLIENTS.remove(clientDetails.getUsername());
        }
    }

    void sendMessage() throws Exception {
        String destUsername = inputStream.readUTF();
        Date timeStamp = new Date();
        String message = inputStream.readUTF();

        ClientDetails sender = clientDetails, receiver = DATABASE.get(destUsername);

        sender.sendMessage(destUsername, timeStamp, message);
        receiver.receiveMessage(destUsername, timeStamp, message);
    }

    void refreshPersonalChatHistory() throws Exception {
        String withUsername = inputStream.readUTF();

        ArrayList<Message> messageList = clientDetails.getMessageList(withUsername);

        outputStream.writeUTF(messageList.size() + "");

        for (Message message : messageList) {
            outputStream.writeUTF(message.isSent ? "SENT" : "RECEIVED");
            outputStream.writeUTF(message.userInvolved);
            outputStream.writeUTF(message.timeStamp);
            outputStream.writeUTF(message.message);
        }
    }

    void refreshAllChatHistory() throws Exception {
        outputStream.writeUTF(DATABASE.size() + "");

        for (String withUsername : DATABASE.keySet()) {
            outputStream.writeUTF(withUsername);

            ArrayList<Message> messageList = clientDetails.getMessageList(withUsername);

            outputStream.writeUTF(messageList.size() + "");

            for (Message message : messageList) {
                outputStream.writeUTF(message.isSent ? "SENT" : "RECEIVED");
                outputStream.writeUTF(message.userInvolved);
                outputStream.writeUTF(message.timeStamp);
                outputStream.writeUTF(message.message);
            }
        }
    }

    void broadcastMessage() throws Exception {
        Date timeStamp = new Date();
        String message = inputStream.readUTF();

        for (String destUsername : DATABASE.keySet()) {
            ClientDetails sender = clientDetails,
                    receiver = DATABASE.get(destUsername);

            sender.sendMessage(destUsername, timeStamp, message);
            receiver.receiveMessage(destUsername, timeStamp, message);
        }
    }

    void getAllUsernames() throws Exception {
        outputStream.writeUTF(DATABASE.size() + "");

        for (String user : DATABASE.keySet()) {
            outputStream.writeUTF(user);
        }
    }

    void getOnlineUsernames() throws Exception {
        outputStream.writeUTF(ACTIVE_CLIENTS.size() + "");

        for (String user : ACTIVE_CLIENTS) {
            outputStream.writeUTF(user);
        }
    }
}