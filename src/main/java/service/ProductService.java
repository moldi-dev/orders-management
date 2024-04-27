package service;

import dao.ProductDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Product;

import java.util.List;

public class ProductService {
    private ProductDAO productDAO = new ProductDAO();

    public ProductService() {

    }

    public List<Product> getAllProducts() {
        return productDAO.findAll();
    }

    public Product insertProduct(String name, String description, Double price, Integer stock) {
        Product productToInsert = new Product(name, description, price, stock);
        return productDAO.insert(productToInsert);
    }

    public Product updateProductById(Long productId, Product updatedProduct) {
        return productDAO.updateById(productId, updatedProduct);
    }

    public void deleteProductById(Long productId) {
        productDAO.deleteById(productId);
    }

    public void deleteProductByName(String productName) {
        productDAO.deleteByName(productName);
    }

    public ObservableList<Product> convertProductListToObservableList(List<Product> productList) {
        ObservableList<Product> observableList = FXCollections.observableArrayList();
        observableList.addAll(productList);
        return observableList;
    }
}
