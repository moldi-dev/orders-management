package dao;

import connection.ConnectionFactory;
import model.Product;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductDAO extends AbstractDAO<Product> {
    public ProductDAO() {

    }

    public Product findByName(String name) {
        String query = createFindByNameQuery(name);
        System.out.println(query);

        try (PreparedStatement preparedStatement = ConnectionFactory.getConnection().prepareStatement(query)) {
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return mapFromResultSet(resultSet);
            }
        }

        catch (SQLException e) {
            LOGGER.severe("Error occured while finding product by name: " + e.getMessage());
        }

        return null;
    }

    public Product updateByName(String name, Product updatedProduct) {
        String query = createUpdateByNameQuery(name, updatedProduct);
        System.out.println(query);

        try (PreparedStatement preparedStatement = ConnectionFactory.getConnection().prepareStatement(query)) {
            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows == 0) {
                LOGGER.severe("Error occured while updating product by name: " + name);
                return null;
            }

            return updatedProduct;
        }

        catch (SQLException e) {
            LOGGER.severe("Error occured while updating product by name: " + e.getMessage());
        }

        return null;
    }

    public int deleteByName(String name) {
        String query = createDeleteByNameQuery(name);
        System.out.println(query);

        try (PreparedStatement preparedStatement = ConnectionFactory.getConnection().prepareStatement(query)) {
            return preparedStatement.executeUpdate();
        }

        catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    private String createFindByNameQuery(String name) {
        StringBuilder query = new StringBuilder();

        query.append("SELECT * FROM products WHERE name = '").append(name).append("';");

        return query.toString();
    }

    private String createUpdateByNameQuery(String name, Product updatedProduct) {
        StringBuilder query = new StringBuilder();

        query.append("UPDATE products SET ");
        query.append("name = '").append(updatedProduct.getName()).append("', ");
        query.append("price = '").append(updatedProduct.getPrice()).append("', ");
        query.append("description = '").append(updatedProduct.getDescription()).append("', ");
        query.append("stock = '").append(updatedProduct.getStock()).append("' ");
        query.append("WHERE name = '").append(name).append("';");

        return query.toString();
    }

    private String createDeleteByNameQuery(String name) {
        StringBuilder query = new StringBuilder();

        query.append("DELETE FROM products WHERE name = '").append(name).append("';");

        return query.toString();
    }

    private Product mapFromResultSet(ResultSet resultSet) throws SQLException {
        Product product = new Product();

        product.setProductId(resultSet.getLong("product_id"));
        product.setName(resultSet.getString("name"));
        product.setPrice(resultSet.getDouble("price"));
        product.setDescription(resultSet.getString("description"));
        product.setStock(resultSet.getInt("stock"));

        return product;
    }
}
