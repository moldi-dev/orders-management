package service;

import dao.OrderDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import model.Order;

import java.util.List;
import java.util.Optional;

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

    public void initializeActionColumnInOrderTableForAdminControlPanel(TableColumn actionColumnOrderTable, TableView orderTableView) {
        actionColumnOrderTable.setCellFactory(_ -> new TableCell<Order, Long>() {
            @Override
            protected void updateItem(Long item, boolean empty) {
                super.updateItem(item, empty);

                if (item == null || empty) {
                    setGraphic(null);
                }

                else {
                    Button deleteButton = new Button("DELETE");

                    deleteButton.setOnAction(_ -> {
                        Order selectedOrder = getTableView().getItems().get(getIndex());

                        Dialog<ButtonType> dialog = new Dialog<>();
                        dialog.setTitle("Orders management");
                        dialog.setHeaderText("Are you sure that you want to delete the order with id " + selectedOrder.getOrderId() + "? This operation cannot be undone!");

                        ButtonType okButton = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
                        ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

                        dialog.getDialogPane().getButtonTypes().addAll(okButton, cancelButton);

                        Optional<ButtonType> result = dialog.showAndWait();

                        if (result.isPresent() && result.get() == okButton) {
                            int affectedRows = deleteOrderById(selectedOrder.getOrderId());

                            if (affectedRows > 0) {
                                Dialog<ButtonType> confirmationDialog = new Dialog<>();
                                confirmationDialog.setTitle("Orders management");
                                confirmationDialog.setHeaderText("The order has been successfully deleted!");
                                confirmationDialog.getDialogPane().getButtonTypes().addAll(okButton);
                                confirmationDialog.showAndWait();

                                orderTableView.setItems(convertOrderListToObservableList(findAllOrders()));
                            }

                            else {
                                Dialog<ButtonType> errorDialog = new Dialog<>();
                                errorDialog.setTitle("Orders management");
                                errorDialog.setHeaderText("An error has occured! Please try again!");
                                errorDialog.getDialogPane().getButtonTypes().addAll(okButton);
                                errorDialog.showAndWait();
                            }
                        }
                    });

                    HBox hBox = new HBox(deleteButton);
                    hBox.setSpacing(20);
                    hBox.setAlignment(Pos.CENTER);
                    setGraphic(hBox);
                }
            }
        });
    }
}
