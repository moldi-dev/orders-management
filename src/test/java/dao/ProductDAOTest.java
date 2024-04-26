package dao;

import model.Product;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProductDAOTest {

    private final ProductDAO productDAO = new ProductDAO();

    @Test
    void findByNameTestShouldReturnValue() {
        Product foundProduct = productDAO.findByName("product 1");

        assertNotNull(foundProduct);
        assertEquals(foundProduct.getName(), "product 1");
        assertTrue(foundProduct.getProductId() > 0);
    }

    @Test
    void updateByNameTestShouldReturnValue() {
        Product updatedProduct = new Product("product update test", null, null, 55.0, 13);

        productDAO.updateByName("product 1", updatedProduct);
        Product foundProduct = productDAO.findById(1L);

        assertEquals("product update test", foundProduct.getName());
    }

    @Test
    void deleteByNameTestShouldReturnValueGreaterThan0() {
        assertTrue(productDAO.deleteByName("product update test") > 0);
    }
}