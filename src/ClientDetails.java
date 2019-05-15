import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class ClientDetails {
    private final String USERNAME, UNIQUEID;
    private HashMap<String, ArrayList<Message>> CONVERSATIONS;

    ClientDetails(String username, String uniqueID) {
        USERNAME = username; UNIQUEID = uniqueID;
        CONVERSATIONS = new HashMap<>();
    }

    String getUsername() { return USERNAME; }

    String getUniqueID() { return UNIQUEID; }

    ArrayList<Message> getMessageList(String username) { return CONVERSATIONS.get(username); }

    void sendMessage(String username, Date timeStamp, String message) {
        Message sentMsg = new Message(username, timeStamp, message, Message.SENT);

        CONVERSATIONS.putIfAbsent(username, new ArrayList<>());
        CONVERSATIONS.get(username).add(sentMsg);
    }

    void receiveMessage(String username, Date timeStamp, String message) {
        Message receivedMsg = new Message(username, timeStamp, message, Message.RECEIVED);

        CONVERSATIONS.putIfAbsent(username, new ArrayList<>());
        CONVERSATIONS.get(username).add(receivedMsg);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Viewing details of user: ").append(USERNAME).append("\n");
        sb.append("Unique ID: ").append(UNIQUEID).append("\n");
        sb.append("Conversations: ").append("\n");
        for (String user : CONVERSATIONS.keySet()) {
            sb.append(user).append("\n");
            sb.append(CONVERSATIONS.get(user));
            sb.append("\n\n");
        }
        return "{" + sb + "}";
    }
}

class Message {
    static final boolean SENT = true, RECEIVED = false;

    String userInvolved, timeStamp, message;
    boolean isSent;

    Message(String username, Date time, String msg, boolean flag) {
        userInvolved = username;
        timeStamp = ServerSide.DATE_FORMAT.format(time);
        message = msg;
        isSent = flag;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(isSent ? "Sent" : "Received").append("\n");
        sb.append("User involved: ").append(userInvolved).append("\n");
        sb.append("Time: ").append(timeStamp).append("\n");
        sb.append("Message: ").append(message).append("\n");
        return "[" + sb + "]";
    }
}