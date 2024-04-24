package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import connection.ConnectionFactory;
import model.Client;

public class ClientDAO {
    private static final Logger LOGGER = Logger.getLogger(ClientDAO.class.getName());
    private static final String findAllClientsStatement = "SELECT * FROM clients";
    private static final String findClientByIdStatement = "SELECT * FROM clients WHERE id = ?";
    private static final String findClientByUsernameStatement = "SELECT * FROM clients WHERE username = ?";
    private static final String findClientsByFirstNameAndLastNameStatement = "SELECT * FROM clients WHERE firstName = ? AND lastName = ?";
    private static final String insertStatement = "INSERT INTO clients (username, first_name, last_name, email, password, address, phone_number) VALUES (?, ?, ?)";
    private static final String updateClientByIdStatement = "UPDATE clients SET username = ?, password = ?, first_name = ?, last_name = ?, email = ?, phone_number = ?, address = ? WHERE id = ?";
    private static final String updateClientByUsernameStatement = "UPDATE clients SET username = ?, password = ?, first_name = ?, last_name = ?, email = ?, phone_number = ?, address = ? WHERE username = ?";
    private static final String deleteClientByIdStatement = "DELETE FROM clients WHERE id = ?";
    private static final String deleteClientByUsernameStatement = "DELETE FROM clients WHERE username = ?";

    public static List<Client> findAllClients() {
        List<Client> clientList = new ArrayList<>();

        Connection connection = ConnectionFactory.getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            preparedStatement = connection.prepareStatement(findAllClientsStatement);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Client client = extractClientFromResultSet(resultSet);
                clientList.add(client);
            }
        }

        catch (SQLException e) {
            LOGGER.log(Level.WARNING, e.getMessage());
        }

        finally {
            ConnectionFactory.close(resultSet);
            ConnectionFactory.close(preparedStatement);
            ConnectionFactory.close(connection);
        }

        return clientList;
    }

    private static Client extractClientFromResultSet(ResultSet resultSet) throws SQLException {
        Client client = new Client();

        client.setClientId(resultSet.getLong("client_id"));
        client.setUsername(resultSet.getString("username"));
        client.setEmail(resultSet.getString("email"));
        client.setFirstName(resultSet.getString("first_name"));
        client.setLastName(resultSet.getString("last_name"));
        client.setPassword(resultSet.getString("password"));
        client.setAddress(resultSet.getString("address"));
        client.setPhoneNumber(resultSet.getString("phone_number"));

        return client;
    }
}