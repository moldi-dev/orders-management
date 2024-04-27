package service;

import dao.OrderDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Order;

import java.util.List;

public class OrderService {
    private final OrderDAO orderDAO = new OrderDAO();

    public OrderService() {

    }

    public List<Order> findAllOrders() {
        return orderDAO.findAll();
    }

    public Order findOrderById(Long orderId) {
        return orderDAO.findById(orderId);
    }

    public List<Order> findOrdersByUserId(Long userId) {
        return orderDAO.findOrdersByUserId(userId);
    }

    public Order insertOrder(Order order) {
        return orderDAO.insert(order);
    }

    public Order updateOrderById(Long orderId, Order orderToUpdate) {
        return orderDAO.updateById(orderId, orderToUpdate);
    }

    public int deleteOrderById(Long orderId) {
        return orderDAO.deleteById(orderId);
    }

    public int deleteOrdersWithProductId(Long productId) {
        return orderDAO.deleteOrdersWithProductId(productId);
    }

    public int deleteOrdersWithProductName(String productName) {
        return orderDAO.deleteOrdersWithProductName(productName);
    }

    public ObservableList<Order> convertOrderListToObservableList(List<Order> orderList) {
        ObservableList<Order> observableList = FXCollections.observableArrayList();
        observableList.addAll(orderList);
        return observableList;
    }
}
