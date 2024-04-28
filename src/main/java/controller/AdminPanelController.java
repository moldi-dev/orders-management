package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import model.Order;
import model.Product;
import model.User;
import service.OrderService;
import service.ProductService;
import service.UserService;
import session.SessionFactory;

import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.util.ResourceBundle;

public class AdminPanelController implements Initializable {

    @FXML
    private TableColumn userIdColumnUserTable;

    @FXML
    private TableColumn usernameColumnUserTable;

    @FXML
    private TableColumn firstNameColumnUserTable;

    @FXML
    private TableColumn lastNameColumnUserTable;

    @FXML
    private TableColumn emailColumnUserTable;

    @FXML
    private TableColumn phoneNumberColumnUserTable;

    @FXML
    private TableColumn addressColumnUserTable;

    @FXML
    private TableColumn roleColumnUserTable;

    @FXML
    private TableColumn actionColumnUserTable;

    @FXML
    private TableView productTableView;

    @FXML
    private TableColumn productIdColumnProductTable;

    @FXML
    private TableColumn productNameColumnProductTable;

    @FXML
    private TableColumn productDescriptionColumnProductTable;

    @FXML
    private TableColumn productPriceColumnProductTable;

    @FXML
    private TableColumn productStockColumnProductTable;

    @FXML
    private TableColumn actionColumnProductTable;

    @FXML
    private TableView orderTableView;

    @FXML
    private TableColumn orderIdColumnOrderTable;

    @FXML
    private TableColumn userIdColumnOrderTable;

    @FXML
    private TableColumn productIdColumnOrderTable;

    @FXML
    private TableColumn quantityColumnOrderTable;

    @FXML
    private TableColumn totalPriceColumnOrderTable;

    @FXML
    private TableColumn createdAtColumnOrderTable;

    @FXML
    private TableColumn actionColumnOrderTable;

    @FXML
    private TableView userTableView;

    @FXML
    private BorderPane borderPane;

    private UserService userService = new UserService();
    private ProductService productService = new ProductService();
    private OrderService orderService = new OrderService();

    public void onYourOrdersTextClicked() throws IOException {
        new SceneController(borderPane, "/view/orders-view.fxml");
    }

    public void onProductsTextClicked() throws IOException {
        new SceneController(borderPane, "/view/products-view.fxml");
    }

    public void onSignOutTextClicked() throws IOException {
        SessionFactory.setSignedInUser(null);

        new SceneController(borderPane, "/view/sign-in-view.fxml");
    }

    public void onAdminPanelTextClicked() {
        // do nothing, we're already on this view
    }

    @Override
    @SuppressWarnings("unchecked")
    public void initialize(URL url, ResourceBundle resourceBundle) {
        userIdColumnUserTable.setCellValueFactory(new PropertyValueFactory<User, Long>("userId"));
        usernameColumnUserTable.setCellValueFactory(new PropertyValueFactory<User, String>("username"));
        firstNameColumnUserTable.setCellValueFactory(new PropertyValueFactory<User, String>("firstName"));
        lastNameColumnUserTable.setCellValueFactory(new PropertyValueFactory<User, String>("lastName"));
        emailColumnUserTable.setCellValueFactory(new PropertyValueFactory<User, String>("email"));
        phoneNumberColumnUserTable.setCellValueFactory(new PropertyValueFactory<User, String>("phoneNumber"));
        addressColumnUserTable.setCellValueFactory(new PropertyValueFactory<User, String>("address"));
        roleColumnUserTable.setCellValueFactory(new PropertyValueFactory<User, String>("role"));
        actionColumnUserTable.setCellValueFactory(new PropertyValueFactory<User, String>("username"));

        userService.initializeActionColumnInUserTableForAdminControlPanel(actionColumnUserTable, userTableView);

        userTableView.setItems(userService.convertUserListToObservableList(userService.findAllUsers()));

        productIdColumnProductTable.setCellValueFactory(new PropertyValueFactory<Product, Long>("productId"));
        productNameColumnProductTable.setCellValueFactory(new PropertyValueFactory<Product, String>("name"));
        productDescriptionColumnProductTable.setCellValueFactory(new PropertyValueFactory<Product, String>("description"));
        productPriceColumnProductTable.setCellValueFactory(new PropertyValueFactory<Product, Double>("price"));
        productStockColumnProductTable.setCellValueFactory(new PropertyValueFactory<Product, Integer>("stock"));
        actionColumnProductTable.setCellValueFactory(new PropertyValueFactory<Product, String>("name"));

        productService.initializeActionColumnInProductTableForAdminControlPanel(actionColumnProductTable, productTableView);

        productTableView.setItems(productService.convertProductListToObservableList(productService.findAllProducts()));

        orderIdColumnOrderTable.setCellValueFactory(new PropertyValueFactory<Order, Long>("orderId"));
        userIdColumnOrderTable.setCellValueFactory(new PropertyValueFactory<Order, Long>("userId"));
        productIdColumnOrderTable.setCellValueFactory(new PropertyValueFactory<Order, Long>("productId"));
        quantityColumnOrderTable.setCellValueFactory(new PropertyValueFactory<Order, Integer>("quantity"));
        totalPriceColumnOrderTable.setCellValueFactory(new PropertyValueFactory<Order, Double>("totalPrice"));
        createdAtColumnOrderTable.setCellValueFactory(new PropertyValueFactory<Order, Timestamp>("createdAt"));
        actionColumnOrderTable.setCellValueFactory(new PropertyValueFactory<Order, Long>("orderId"));

        orderService.initializeActionColumnInOrderTableForAdminControlPanel(actionColumnOrderTable, orderTableView);

        orderTableView.setItems(orderService.convertOrderListToObservableList(orderService.findAllOrders()));
    }
}
