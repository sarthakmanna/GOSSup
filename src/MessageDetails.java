public class MessageDetails {
    final static boolean SENT = true, RECEIVED = false;

    String userInvolved;
    long timeStamp;
    String message;
    boolean isSent;

    MessageDetails(String username, long time, String msg, boolean type) {
        userInvolved = username;
        timeStamp = time;
        message = msg;
        isSent = type;
    }
}
