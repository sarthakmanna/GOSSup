public class Message {
    String userInvolved, message;
    long timeStamp;
    boolean isSent;

    Message(String username, String time, String msg, String isSendType) {
        userInvolved = username;
        timeStamp = Long.parseLong(time);
        message = msg;
        isSent = isSendType.equals(ClientSide.YES);
    }
}
