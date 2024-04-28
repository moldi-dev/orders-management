package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import service.OrderService;
import service.ProductService;
import service.UserService;
import session.SessionFactory;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AdminPanelController implements Initializable {

    @FXML
    private Button addUserButton;

    @FXML
    private Button addProductButton;

    @FXML
    private TableView productTableView;

    @FXML
    private TableView orderTableView;

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
        userTableView = userService.initializeUsersTableThroughReflectionForAdminControlPanel(userTableView);
        userService.initializeAddUserButtonLogicForAdminControlPanel(addUserButton, userTableView);

        productTableView = productService.initializeProductsTableThroughReflectionForAdminControlPanel(productTableView);
        productService.initializeAddProductButtonLogicForAdminControlPanelThroughReflection(addProductButton, productTableView);

        orderTableView = orderService.initializeOrdersTableThroughReflectionForAdminControlPanel(orderTableView);
    }
}
