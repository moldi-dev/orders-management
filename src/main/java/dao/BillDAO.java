package dao;

import connection.ConnectionFactory;
import model.Bill;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class BillDAO {
    public Bill insertBill(Bill bill) {
        String query = createInsertBillQuery(bill);
        System.out.println(query);

        try (PreparedStatement preparedStatement = ConnectionFactory.getConnection().prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows == 0) {
                return null;
            }

            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    Long id = generatedKeys.getLong(1);
                    return findById(id);
                }

                else {
                    return null;
                }
            }
        }

        catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public Bill findById(Long id) {
        String query = createFindByIdQuery(id);
        System.out.println(query);

        try (PreparedStatement preparedStatement = ConnectionFactory.getConnection().prepareStatement(query)) {
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return new Bill(resultSet.getLong(1),
                                     resultSet.getString(2),
                                     resultSet.getInt(3),
                                     resultSet.getDouble(4),
                                     resultSet.getString(5),
                                     resultSet.getString(6),
                                     resultSet.getString(7),
                                     resultSet.getString(8),
                                     resultSet.getString(9),
                                     resultSet.getTimestamp(10));
            }
        }

        catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public int deleteById(Long id) {
        String query = createDeleteByIdQuery(id);
        System.out.println(query);

        try (PreparedStatement preparedStatement = ConnectionFactory.getConnection().prepareStatement(query)) {
            return preparedStatement.executeUpdate();
        }

        catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    private String createInsertBillQuery(Bill bill) {
        StringBuilder query = new StringBuilder();

        query.append("INSERT INTO bills (order_id, product_name, quantity, total_price, username, first_name, last_name, address, phone_number, created_at) VALUES (");
        query.append(bill.orderId()).append(", ");
        query.append("'").append(bill.productName()).append("', ");
        query.append(bill.quantity()).append(", ");
        query.append(bill.totalPrice()).append(", ");
        query.append("'").append(bill.username()).append("', ");
        query.append("'").append(bill.firstName()).append("', ");
        query.append("'").append(bill.lastName()).append("', ");
        query.append("'").append(bill.address()).append("', ");
        query.append("'").append(bill.phoneNumber()).append("', ");
        query.append("'").append(bill.createdAt()).append("');");

        return query.toString();
    }

    private String createFindByIdQuery(Long id) {
        StringBuilder query = new StringBuilder();

        query.append("SELECT * FROM bills WHERE order_id = ");
        query.append(id);
        query.append(";");

        return query.toString();
    }

    private String createDeleteByIdQuery(Long id) {
        StringBuilder query = new StringBuilder();

        query.append("DELETE FROM bills WHERE order_id = ");
        query.append(id);
        query.append(";");

        return query.toString();
    }
}
