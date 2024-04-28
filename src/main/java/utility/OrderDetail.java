package utility;

import java.sql.Timestamp;

public class OrderDetail {
    private final Long orderId;
    private final String productName;
    private final String productDescription;
    private final Double productPrice;
    private final Integer productQuantity;
    private final Double totalPrice;
    private final Timestamp orderDate;

    public OrderDetail(Long orderId, String productName, String productDescription, Double productPrice, Integer productQuantity, Double totalPrice, Timestamp orderDate) {
        this.orderId = orderId;
        this.productName = productName;
        this.productDescription = productDescription;
        this.productPrice = productPrice;
        this.productQuantity = productQuantity;
        this.totalPrice = totalPrice;
        this.orderDate = orderDate;
    }

    public Long getOrderId() {
        return orderId;
    }

    public String getProductName() {
        return productName;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public Double getProductPrice() {
        return productPrice;
    }

    public Integer getProductQuantity() {
        return productQuantity;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public Timestamp getOrderDate() {
        return orderDate;
    }
}
