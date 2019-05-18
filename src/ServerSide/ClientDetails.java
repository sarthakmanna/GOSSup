package ServerSide;

import java.util.ArrayList;
import java.util.HashMap;

public class ClientDetails {
    private final String USERNAME, UNIQUEID;
    private HashMap<String, ArrayList<MessageDetails>> CONVERSATIONS;
    private HashMap<String, Integer> UNREAD_COUNT;
    private int TOTAL_UNREAD_COUNT;
    private long lastSeenTime;

    public ClientDetails(String username, String uniqueID) {
        USERNAME = username;
        UNIQUEID = uniqueID;
        TOTAL_UNREAD_COUNT = 0;
        CONVERSATIONS = new HashMap<>();
        UNREAD_COUNT = new HashMap<>();
    }

    public String getUsername() { return USERNAME; }

    public String getUniqueID() { return UNIQUEID; }

    public int getUNREAD_COUNT(String username) {
        return UNREAD_COUNT.getOrDefault(username, 0);
    }

    public int getTOTAL_UNREAD_COUNT() { return TOTAL_UNREAD_COUNT; }

    public long getLastSeenTime() { return lastSeenTime; }

    public void setLastSeenTime(long lastSeenTime) {
        this.lastSeenTime = lastSeenTime;
    }

    public boolean hasChattedBefore(String username) {
        return CONVERSATIONS.containsKey(username);
    }

    public ArrayList<String> getFriends() {
        return new ArrayList<>(CONVERSATIONS.keySet());
    }

    public ArrayList<MessageDetails> getMessageList(String username) {
        if (!CONVERSATIONS.containsKey(username)) return new ArrayList<>();

        TOTAL_UNREAD_COUNT -= UNREAD_COUNT.get(username);
        UNREAD_COUNT.put(username, 0);
        return CONVERSATIONS.get(username);
    }

    public void sendMessage(String username, long timeStamp, String message) {
        if (username.equals(USERNAME)) return;

        MessageDetails sentMsg = new MessageDetails(username, timeStamp,
                message, MessageDetails.SENT);

        CONVERSATIONS.putIfAbsent(username, new ArrayList<>());
        CONVERSATIONS.get(username).add(sentMsg);
    }

    public void receiveMessage(String username, long timeStamp, String message) {
        if (username.equals(USERNAME)) return;

        MessageDetails receivedMsg = new MessageDetails(username, timeStamp, message, MessageDetails.RECEIVED);

        CONVERSATIONS.putIfAbsent(username, new ArrayList<>());
        CONVERSATIONS.get(username).add(receivedMsg);

        UNREAD_COUNT.put(username, getUNREAD_COUNT(username) + 1);
        ++TOTAL_UNREAD_COUNT;
    }

    public ArrayList<MessageDetails> getMessageAfterTime(
            String username, long time) {
        ArrayList<MessageDetails> allMessages =
                CONVERSATIONS.getOrDefault(username, new ArrayList<>());
        System.out.println("hello"+allMessages.size());
        ArrayList<MessageDetails> messagesAfterTime = new ArrayList<>();

        for (int i = allMessages.size() - 1; i >= 0; --i) {
            if (allMessages.get(i).getTimeStamp() > time) {
                messagesAfterTime.add(allMessages.get(i));
            } else {
                break;
            }
        }

        TOTAL_UNREAD_COUNT -= getUNREAD_COUNT(username);
        UNREAD_COUNT.put(username, 0);

        return messagesAfterTime;
    }

    public ArrayList<MessageDetails> getLastKMessages(
            String username, int K) {
        ArrayList<MessageDetails> allMessages =
                CONVERSATIONS.getOrDefault(username, new ArrayList<>());
        ArrayList<MessageDetails> lastKMessages = new ArrayList<>();

        for (int i = allMessages.size() - 1;
                i >= Math.max(0, allMessages.size() - K); --i) {
            lastKMessages.add(allMessages.get(i));
        }

        TOTAL_UNREAD_COUNT -= getUNREAD_COUNT(username);
        UNREAD_COUNT.put(username, 0);

        return lastKMessages;
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