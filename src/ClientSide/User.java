package ClientSide;

public class User {
    private String USERNAME, STATUS;
    private boolean HAS_CHATTED_BEFORE;

    public User(String name, String status, String chattedBefore) {
        USERNAME = name;
        STATUS = status;
        HAS_CHATTED_BEFORE = chattedBefore.equals(ClientAPI.YES);
    }

    public String getUSERNAME() {
        return USERNAME;
    }

    public String getSTATUS() {
        return STATUS;
    }

    public boolean isHAS_CHATTED_BEFORE() {
        return HAS_CHATTED_BEFORE;
    }

    public void setUSERNAME(String USERNAME) {
        this.USERNAME = USERNAME;
    }

    public void setSTATUS(String STATUS) {
        this.STATUS = STATUS;
    }

    public void setHAS_CHATTED_BEFORE(boolean HAS_CHATTED_BEFORE) {
        this.HAS_CHATTED_BEFORE = HAS_CHATTED_BEFORE;
    }
}