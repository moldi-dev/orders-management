package dao;

import connection.ConnectionFactory;
import model.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO extends AbstractDAO<User> {
    public UserDAO() {

    }

    public User findByUsername(String username) {
        String query = createFindByUsernameQuery(username);
        System.out.println(query);

        try (PreparedStatement preparedStatement = ConnectionFactory.getConnection().prepareStatement(query)) {
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return mapFromResultSet(resultSet);
            }
        }

        catch (SQLException e) {
            LOGGER.severe("Error occurred while finding user by username: " + e.getMessage());
        }

        return null;
    }

    public User updateByUsername(String username, User updatedUser) {
        String query = createUpdateByUsernameQuery(username, updatedUser);
        System.out.println(query);

        try (PreparedStatement preparedStatement = ConnectionFactory.getConnection().prepareStatement(query)) {
            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows == 0) {
                LOGGER.severe("Updating user by username failed, no rows affected.");
                return null;
            }

            return updatedUser;
        }

        catch (SQLException e) {
            LOGGER.severe("Error occurred while updating user by username: " + e.getMessage());
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

        query.append("SELECT * FROM users WHERE username = '").append(username).append("';");

        return query.toString();
    }

    private String createUpdateByUsernameQuery(String username, User updatedUser) {
        StringBuilder query = new StringBuilder();

        query.append("UPDATE users SET ");
        query.append("username = '").append(updatedUser.getUsername()).append("', ");
        query.append("last_name = '").append(updatedUser.getLastName()).append("', ");
        query.append("first_name = '").append(updatedUser.getFirstName()).append("', ");
        query.append("email = '").append(updatedUser.getEmail()).append("', ");
        query.append("address = '").append(updatedUser.getAddress()).append("', ");
        query.append("password = '").append(updatedUser.getPassword()).append("', ");
        query.append("role = '").append(updatedUser.getRole()).append("', ");
        query.append("phone_number = '").append(updatedUser.getPhoneNumber()).append("' ");
        query.append("WHERE username = '").append(username).append("';");

        return query.toString();
    }

    private String createDeleteByUsernameQuery(String username) {
        StringBuilder query = new StringBuilder();

        query.append("DELETE FROM users WHERE username = '").append(username).append("';");

        return query.toString();
    }

    private User mapFromResultSet(ResultSet resultSet) throws SQLException {
        User user = new User();

        user.setUserId(resultSet.getLong("user_id"));
        user.setFirstName(resultSet.getString("first_name"));
        user.setLastName(resultSet.getString("last_name"));
        user.setUsername(resultSet.getString("username"));
        user.setPassword(resultSet.getString("password"));
        user.setEmail(resultSet.getString("email"));
        user.setPhoneNumber(resultSet.getString("phone_number"));
        user.setAddress(resultSet.getString("address"));
        user.setRole(resultSet.getString("role"));

        return user;
    }
}
