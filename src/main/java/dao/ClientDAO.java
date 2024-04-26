package dao;

import connection.ConnectionFactory;
import model.Client;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ClientDAO extends AbstractDAO<Client> {
    public ClientDAO() {

    }

    public Client findByUsername(String username) {
        String query = createFindByUsernameQuery(username);
        System.out.println(query);

        try (PreparedStatement preparedStatement = ConnectionFactory.getConnection().prepareStatement(query)) {
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return mapFromResultSet(resultSet);
            }
        }

        catch (SQLException e) {
            LOGGER.severe("Error occurred while finding client by username: " + e.getMessage());
        }

        return null;
    }

    public Client updateByUsername(String username, Client updatedClient) {
        String query = createUpdateByUsernameQuery(username, updatedClient);
        System.out.println(query);

        try (PreparedStatement preparedStatement = ConnectionFactory.getConnection().prepareStatement(query)) {
            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows == 0) {
                LOGGER.severe("Updating client by username failed, no rows affected.");
                return null;
            }

            return updatedClient;
        }

        catch (SQLException e) {
            LOGGER.severe("Error occurred while updating client by username: " + e.getMessage());
        }

        return null;
    }

    public int deleteByUsername(String username) {
        String query = createDeleteByUsernameQuery(username);
        System.out.println(query);

        try (PreparedStatement preparedStatement = ConnectionFactory.getConnection().prepareStatement(query)) {
            return preparedStatement.executeUpdate();
        }

        catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    private String createFindByUsernameQuery(String username) {
        StringBuilder query = new StringBuilder();

        query.append("SELECT * FROM clients WHERE username = '").append(username).append("';");

        return query.toString();
    }

    private String createUpdateByUsernameQuery(String username, Client updatedClient) {
        StringBuilder query = new StringBuilder();

        query.append("UPDATE clients SET ");
        query.append("username = '").append(updatedClient.getUsername()).append("', ");
        query.append("last_name = '").append(updatedClient.getLastName()).append("', ");
        query.append("first_name = '").append(updatedClient.getFirstName()).append("', ");
        query.append("email = '").append(updatedClient.getEmail()).append("', ");
        query.append("address = '").append(updatedClient.getAddress()).append("', ");
        query.append("password = '").append(updatedClient.getPassword()).append("', ");
        query.append("phone_number = '").append(updatedClient.getPhoneNumber()).append("' ");
        query.append("WHERE username = '").append(username).append("';");

        return query.toString();
    }

    private String createDeleteByUsernameQuery(String username) {
        StringBuilder query = new StringBuilder();

        query.append("DELETE FROM clients WHERE username = '").append(username).append("';");

        return query.toString();
    }

    private Client mapFromResultSet(ResultSet resultSet) throws SQLException {
        Client client = new Client();

        client.setClientId(resultSet.getLong("client_id"));
        client.setFirstName(resultSet.getString("first_name"));
        client.setLastName(resultSet.getString("last_name"));
        client.setUsername(resultSet.getString("username"));
        client.setPassword(resultSet.getString("password"));
        client.setEmail(resultSet.getString("email"));
        client.setPhoneNumber(resultSet.getString("phone_number"));
        client.setAddress(resultSet.getString("address"));

        return client;
    }
}
