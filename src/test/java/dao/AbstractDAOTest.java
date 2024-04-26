package dao;

import model.Client;
import model.Product;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AbstractDAOTest {

    private final ClientDAO clientDAO = new ClientDAO();
    private final ProductDAO productDAO = new ProductDAO();

    @Test
    void findByIdTestShouldReturnValue() {
        Client expectedClient = new Client(1L, "moldi", null, null, null, null, null, null);

        Client foundClient = clientDAO.findById(1L);

        assertNotNull(foundClient);
        assertEquals(expectedClient.getClientId(), foundClient.getClientId());
        assertEquals(expectedClient.getUsername(), foundClient.getUsername());
    }

    @Test
    void findByIdTestShouldReturnNull() {
        Client foundClient = clientDAO.findById(0L);

        assertNull(foundClient);
    }

    @Test
    void findAllTestShouldReturnValue() {
        List<Client> foundClients = clientDAO.findAll();

        assertFalse(foundClients.isEmpty());
    }

    @Test
    void insertTestShouldReturnValue() {
        Product product = new Product("test prod", "test", "test", 20.00, 1);

        Product insertedProduct = productDAO.insert(product);

        assertTrue(insertedProduct.getProductId() > 0);
        assertEquals("test prod", insertedProduct.getName());
    }

    @Test
    void updateByIdTestShouldReturnValue() {
        Product updatedProduct = new Product("test product update", "test", "test", 15.00, 1000);
        productDAO.updateById(1L, updatedProduct);
        Product foundProduct = productDAO.findById(1L);

        assertEquals("test product update", foundProduct.getName());
    }

    @Test
    void deleteByIdTestShouldReturnValueGreaterThan0() {
        assertEquals(1, clientDAO.deleteById(1L));
    }

    @Test
    void deleteByIdTestShouldReturnValueEqualTo0() {
        assertEquals(0, clientDAO.deleteById(0L));
    }
}