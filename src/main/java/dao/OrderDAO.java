package dao;

import connection.ConnectionFactory;
import model.Order;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrderDAO extends AbstractDAO<Order> {
    public OrderDAO() {

    }

    public List<Order> findOrdersByUserId(Long userId) {
        String query = createFindOrdersByUserIdQuery(userId);
        System.out.println(query);

        List<Order> orderList = new ArrayList<>();

        try (PreparedStatement preparedStatement = ConnectionFactory.getConnection().prepareStatement(query)) {
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                orderList.add(mapFromResultSet(resultSet));
            }

            return orderList;
        }

        catch (SQLException e) {
            LOGGER.severe("Error occurred while finding all orders by user id: " + e.getMessage());
        }

        return null;
    }

    public int deleteOrdersWithProductId(Long productId) {
        String query = createDeleteOrdersWithProductIdQuery(productId);
        System.out.println(query);

        try (PreparedStatement preparedStatement = ConnectionFactory.getConnection().prepareStatement(query)) {
            return preparedStatement.executeUpdate();
        }

        catch (SQLException e) {
            LOGGER.severe("Error occurred while deleting orders with product id: " + e.getMessage());
        }

        return 0;
    }

    public int deleteOrdersWithProductName(String productName) {
        String query = createDeleteOrdersWithProductNameQuery(productName);
        System.out.println(query);

        try (PreparedStatement preparedStatement = ConnectionFactory.getConnection().prepareStatement(query)) {
            return preparedStatement.executeUpdate();
        }

        catch (SQLException e) {
            LOGGER.severe("Error occurred while deleting orders with product name: " + e.getMessage());
        }

        return 0;
    }

    private String createFindOrdersByUserIdQuery(Long userId) {
        StringBuilder query = new StringBuilder();

        query.append("SELECT * FROM orders WHERE user_id = ").append(userId).append(";");

        return query.toString();
    }

    private String createDeleteOrdersWithProductIdQuery(Long productId) {
        StringBuilder query = new StringBuilder();

        query.append("DELETE FROM orders WHERE product_id IN (SELECT product_id FROM products WHERE product_id = ").append(productId).append(");");

        return query.toString();
    }

    private String createDeleteOrdersWithProductNameQuery(String productName) {
        StringBuilder query = new StringBuilder();

        query.append("DELETE FROM orders WHERE product_id IN (SELECT product_id FROM products WHERE products.name = '").append(productName).append("');");

        return query.toString();
    }

    private Order mapFromResultSet(ResultSet resultSet) throws SQLException {
        Order order = new Order();

        order.setOrderId(resultSet.getLong("order_id"));
        order.setUserId(resultSet.getLong("user_id"));
        order.setProductId(resultSet.getLong("product_id"));
        order.setQuantity(resultSet.getInt("quantity"));
        order.setTotalPrice(resultSet.getDouble("total_price"));
        order.setCreatedAt(resultSet.getTimestamp("created_at"));

        return order;
    }
}
