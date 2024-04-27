package service;

import dao.OrderDAO;
import model.Order;

public class OrderService {
    private final OrderDAO orderDAO = new OrderDAO();

    public OrderService() {

    }

    public Order insertOrder(Order order) {
        return orderDAO.insert(order);
    }
}
