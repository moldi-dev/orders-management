package session;

import model.Client;

public class SessionFactory {
    public static Client SIGNED_IN_CLIENT;

    public SessionFactory() {

    }

    public static Client getSignedInClient() {
        return SIGNED_IN_CLIENT;
    }

    public static void setSignedInClient(Client client) {
        SIGNED_IN_CLIENT = client;
    }
}
