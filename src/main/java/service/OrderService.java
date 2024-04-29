package service;

import dao.BillDAO;
import dao.OrderDAO;
import dao.ProductDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import model.Bill;
import model.Order;
import model.Product;
import model.User;
import session.SessionFactory;
import utility.OrderDetail;

import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

public class OrderService {
    private final OrderDAO orderDAO = new OrderDAO();
    private final ProductDAO productDAO = new ProductDAO();
    private final BillDAO billDAO = new BillDAO();

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
        Order insertedOrder = orderDAO.insert(order);
        Product foundProduct = productDAO.findById(insertedOrder.getProductId());
        User signedInUser = SessionFactory.getSignedInUser();

        billDAO.insertBill(new Bill(insertedOrder.getOrderId(),
                foundProduct.getName(),
                insertedOrder.getQuantity(),
                insertedOrder.getTotalPrice(),
                signedInUser.getUsername(),
                signedInUser.getFirstName(),
                signedInUser.getLastName(),
                signedInUser.getAddress(),
                signedInUser.getPhoneNumber(),
                new Timestamp(System.currentTimeMillis())));

       return insertedOrder;
    }

    public Order updateOrderById(Long orderId, Order orderToUpdate) {
        return orderDAO.updateById(orderId, orderToUpdate);
    }

    public int deleteOrderById(Long orderId) {
        billDAO.deleteById(orderId);
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
                    deleteButton.setStyle("-fx-background-color: #0598ff; -fx-text-fill: white;");

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

    @SuppressWarnings("unchecked")
    public TableView initializeOrdersTableThroughReflectionForAdminControlPanel(TableView orderTableView) {
        Field[] fields = Order.class.getDeclaredFields();

        for (Field field : fields) {
            field.setAccessible(true);

            TableColumn column = new TableColumn(field.getName());
            column.setCellValueFactory(new PropertyValueFactory<>(field.getName()));
            orderTableView.getColumns().add(column);
        }

        TableColumn actionColumn = new TableColumn("action");
        actionColumn.setCellValueFactory(new PropertyValueFactory<Order, Long>("orderId"));

        initializeActionColumnInOrderTableForAdminControlPanel(actionColumn, orderTableView);

        orderTableView.getColumns().add(actionColumn);
        orderTableView.setItems(convertOrderListToObservableList(findAllOrders()));

        return orderTableView;
    }

    @SuppressWarnings("unchecked")
    public TableView initializeOrdersTableThroughReflectionForOrdersView(TableView orderTableView) {
        ObservableList<Order> userOrders = convertOrderListToObservableList(findOrdersByUserId(SessionFactory.getSignedInUser().getUserId()));
        ObservableList<OrderDetail> userOrderDetails = FXCollections.observableArrayList();

        for (Order order : userOrders) {
            Product product = productDAO.findById(order.getProductId());
            userOrderDetails.add(new OrderDetail(order.getOrderId(), product.getName(), product.getDescription(), product.getPrice(), order.getQuantity(), order.getTotalPrice(), order.getCreatedAt()));
        }

        Field[] fields = OrderDetail.class.getDeclaredFields();

        for (Field field : fields) {
            field.setAccessible(true);

            TableColumn column = new TableColumn(field.getName());
            column.setCellValueFactory(new PropertyValueFactory<>(field.getName()));
            orderTableView.getColumns().add(column);
        }

        orderTableView.setItems(userOrderDetails);

        return orderTableView;
    }
}
