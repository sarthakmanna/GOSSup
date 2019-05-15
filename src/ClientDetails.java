import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class ClientDetails {
    private final String USERNAME, UNIQUEID;
    private HashMap<String, ArrayList<MessageDetails>> CONVERSATIONS;

    ClientDetails(String username, String uniqueID) {
        USERNAME = username; UNIQUEID = uniqueID;
        CONVERSATIONS = new HashMap<>();
    }

    String getUsername() { return USERNAME; }

    String getUniqueID() { return UNIQUEID; }

    ArrayList<MessageDetails> getMessageList(String username) { return CONVERSATIONS.get(username); }

    void sendMessage(String username, Date timeStamp, String message) {
        MessageDetails sentMsg = new MessageDetails(username, timeStamp, message, MessageDetails.SENT);

        CONVERSATIONS.putIfAbsent(username, new ArrayList<>());
        CONVERSATIONS.get(username).add(sentMsg);
    }

    void receiveMessage(String username, Date timeStamp, String message) {
        MessageDetails receivedMsg = new MessageDetails(username, timeStamp, message, MessageDetails.RECEIVED);

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