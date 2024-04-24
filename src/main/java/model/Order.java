package model;

public class Order {
    private Long orderId;
    private Long clientId;
    private Long productId;
    private Integer quantity;
    private Double totalPrice;

    public Order() {

    }

    public Order(Long clientId, Long productId, Integer quantity, Double totalPrice) {
        this.clientId = clientId;
        this.productId = productId;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
    }

    public Order(Long orderId, Long clientId, Long productId, Integer quantity, Double totalPrice) {
        this.orderId = orderId;
        this.clientId = clientId;
        this.productId = productId;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
    }

    public Long getOrderId() {
        return orderId;
    }

    public Long getClientId() {
        return clientId;
    }

    public Long getProductId() {
        return productId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }
}
