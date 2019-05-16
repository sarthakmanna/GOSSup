package ServerSide;

import java.util.ArrayList;
import java.util.HashMap;

public class ClientDetails {
    private final String USERNAME, UNIQUEID;
    private HashMap<String, ArrayList<MessageDetails>> CONVERSATIONS;
    private long lastSeenTime;

    public ClientDetails(String username, String uniqueID) {
        USERNAME = username; UNIQUEID = uniqueID;
        CONVERSATIONS = new HashMap<>();
    }

    public String getUsername() { return USERNAME; }

    public String getUniqueID() { return UNIQUEID; }

    public long getLastSeenTime() { return lastSeenTime; }

    public void setLastSeenTime(long lastSeenTime) { this.lastSeenTime = lastSeenTime; }

    public boolean hasChattedBefore(String username) { return CONVERSATIONS.containsKey(username); }

    public ArrayList<String> getFriends() {
        return new ArrayList<>(CONVERSATIONS.keySet()); }

    public ArrayList<MessageDetails> getMessageList(String username) {
        return CONVERSATIONS.getOrDefault(username, new ArrayList<>());
    }

    public void sendMessage(String username, long timeStamp, String message) {
        MessageDetails sentMsg = new MessageDetails(username, timeStamp, message, MessageDetails.SENT);

        CONVERSATIONS.putIfAbsent(username, new ArrayList<>());
        CONVERSATIONS.get(username).add(sentMsg);
    }

    public void receiveMessage(String username, long timeStamp, String message) {
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