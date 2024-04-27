package utility;

import model.Order;
import model.Product;

import java.sql.Timestamp;

public class OrderProduct {
    private final Order order;
    private final Product product;

    public OrderProduct(Order order, Product product) {
        this.order = order;
        this.product = product;
    }

    public String getProductName() {
        return product.getName();
    }

    public String getProductDescription() {
        return product.getDescription();
    }

    public Double getProductPrice() {
        return product.getPrice();
    }

    public Integer getOrderQuantity() {
        return order.getQuantity();
    }

    public Double getOrderTotalPrice() {
        return order.getTotalPrice();
    }

    public Timestamp getOrderCreatedAt() {
        return order.getCreatedAt();
    }
}
