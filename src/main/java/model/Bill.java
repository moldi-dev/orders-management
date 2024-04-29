package model;

import java.sql.Timestamp;

public record Bill(Long orderId,
                   String productName,
                   Integer quantity,
                   Double totalPrice,
                   String username,
                   String firstName,
                   String lastName,
                   String address,
                   String phoneNumber,
                   Timestamp createdAt) {

    @Override
    public String toString() {
        return "Bill = {" +
                ", orderId=" + orderId +
                ", productName='" + productName + '\'' +
                ", quantity=" + quantity +
                ", totalPrice=" + totalPrice +
                ", username='" + username + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", address='" + address + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}