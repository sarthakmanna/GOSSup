package ClientSide;

public class Message {
    private String userInvolved, message;
    private long timeStamp;
    private boolean isSent;

    public Message(String username, String time, String msg, String isSendType) {
        userInvolved = username;
        timeStamp = Long.parseLong(time);
        message = msg;
        isSent = isSendType.equals(ClientAPI.YES);
    }

    public String getUserInvolved() {
        return userInvolved;
    }

    public String getMessage() {
        return message;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public boolean isSent() {
        return isSent;
    }

    public void setUserInvolved(String userInvolved) {
        this.userInvolved = userInvolved;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public void setSent(boolean sent) {
        isSent = sent;
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