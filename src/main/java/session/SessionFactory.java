package session;

import model.User;

public class SessionFactory {
    private static User signedInUser;

    public SessionFactory() {

    }

    public static User getSignedInUser() {
        return signedInUser;
    }

    public static void setSignedInUser(User user) {
        signedInUser = user;
    }
}
