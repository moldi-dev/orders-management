package service;

import dao.OrderDAO;
import dao.ProductDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Product;

import java.util.List;

public class ProductService {
    private ProductDAO productDAO = new ProductDAO();
    private OrderDAO orderDAO = new OrderDAO();

    public ProductService() {

    }

    public List<Product> findAllProducts() {
        return productDAO.findAll();
    }

    public Product findProductById(Long productId) {
        return productDAO.findById(productId);
    }

    public Product findProductByName(String name) {
        return productDAO.findByName(name);
    }

    public Product insertProduct(String name, String description, Double price, Integer stock) {
        Product productToInsert = new Product(name, description, price, stock);
        return productDAO.insert(productToInsert);
    }

    public Product updateProductById(Long productId, Product updatedProduct) {
        return productDAO.updateById(productId, updatedProduct);
    }

    public Product updateProductByName(String productName, Product updatedProduct) {
        return productDAO.updateByName(productName, updatedProduct);
    }

    public int deleteProductById(Long productId) {
        orderDAO.deleteOrdersWithProductId(productId);
        return productDAO.deleteById(productId);
    }

    public int deleteProductByName(String productName) {
        orderDAO.deleteOrdersWithProductName(productName);
        return productDAO.deleteByName(productName);
    }

    public ObservableList<Product> convertProductListToObservableList(List<Product> productList) {
        ObservableList<Product> observableList = FXCollections.observableArrayList();
        observableList.addAll(productList);
        return observableList;
    }
}
