package dao;

import model.Client;

public class ClientDAO extends AbstractDAO<Client> {
    public ClientDAO() {

    }

    private String createFindByUsernameQuery(String username) {
        StringBuilder query = new StringBuilder();

        query.append("SELECT * FROM clients WHERE username = ").append(username).append(";");

        return query.toString();
    }
}
