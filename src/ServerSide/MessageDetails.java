package ServerSide;

public class MessageDetails {
    public final static boolean SENT = true, RECEIVED = false;

    private String userInvolved;
    private long timeStamp;
    private String message;
    private boolean isSent;

    public MessageDetails(String username, long time, String msg, boolean type) {
        userInvolved = username;
        timeStamp = time;
        message = msg;
        isSent = type;
    }

    public static boolean isSENT() {
        return SENT;
    }

    public static boolean isRECEIVED() {
        return RECEIVED;
    }

    public String getUserInvolved() {
        return userInvolved;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public String getMessage() {
        return message;
    }

    public boolean isSent() {
        return isSent;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("User involved: ").append(userInvolved).append("\n");
        sb.append("Time stamp: ").append(timeStamp).append("\n");
        sb.append("Message: ").append(message).append("\n");
        sb.append("isSent: ").append(isSent).append("\n");
        return "(" + sb + ")";
    }
}
