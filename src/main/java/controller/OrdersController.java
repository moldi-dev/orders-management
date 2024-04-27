package controller;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import model.Order;
import model.Product;
import service.OrderService;
import service.ProductService;
import session.SessionFactory;
import utility.OrderProduct;

import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.util.ResourceBundle;

public class OrdersController implements Initializable {
    @FXML
    private Text adminPanelText;

    @FXML
    private BorderPane borderPane;

    @FXML
    private TableView tableView;

    @FXML
    private TableColumn productNameColumn;

    @FXML
    private TableColumn productDescriptionColumn;

    @FXML
    private TableColumn productPriceColumn;

    @FXML
    private TableColumn productQuantityColumn;

    @FXML
    private TableColumn totalPriceColumn;

    @FXML
    private TableColumn createdAtColumn;

    private OrderService orderService = new OrderService();
    private ProductService productService = new ProductService();

    public void onYourOrdersTextClicked() {
        // do nothing, we're already on this view
    }

    public void onProductsTextClicked() throws IOException {
        new SceneController(borderPane, "/view/products-view.fxml");
    }

    public void onSignOutTextClicked() throws IOException {
        SessionFactory.setSignedInUser(null);

        new SceneController(borderPane, "/view/sign-in-view.fxml");
    }

    public void onAdminPanelTextClicked() {

    }

    @Override
    @SuppressWarnings("unchecked")
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (SessionFactory.getSignedInUser() == null || !SessionFactory.getSignedInUser().getRole().equals("ADMINISTRATOR")) {
            adminPanelText.setText("");
            adminPanelText.setDisable(true);
        }

        else {
            adminPanelText.setText("Admin panel");
            adminPanelText.setDisable(false);
        }

        ObservableList<Order> userOrders = orderService.convertOrderListToObservableList(orderService.findOrdersByUserId(SessionFactory.getSignedInUser().getUserId()));
        ObservableList<OrderProduct> userOrderProducts = FXCollections.observableArrayList();

        for (Order order : userOrders) {
            Product product = productService.findProductById(order.getProductId());
            userOrderProducts.add(new OrderProduct(order, product));
        }

        productNameColumn.setCellValueFactory(new PropertyValueFactory<OrderProduct, String>("productName"));
        productDescriptionColumn.setCellValueFactory(new PropertyValueFactory<OrderProduct, String>("productDescription"));
        productPriceColumn.setCellValueFactory(new PropertyValueFactory<OrderProduct, Double>("productPrice"));

        productQuantityColumn.setCellValueFactory(new PropertyValueFactory<OrderProduct, Integer>("orderQuantity"));
        totalPriceColumn.setCellValueFactory(new PropertyValueFactory<OrderProduct, Double>("orderTotalPrice"));
        createdAtColumn.setCellValueFactory(new PropertyValueFactory<OrderProduct, Timestamp>("orderCreatedAt"));

        tableView.setItems(userOrderProducts);
    }
}
