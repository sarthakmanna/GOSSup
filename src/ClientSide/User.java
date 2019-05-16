package ClientSide;

public class User {
    String USERNAME;
    String STATUS;
    boolean HAS_CHATTED_BEFORE;

    User(String name, String status, String chattedBefore) {
        USERNAME = name;
        STATUS = status;
        HAS_CHATTED_BEFORE = chattedBefore.equals(ClientAPI.YES);
    }
}