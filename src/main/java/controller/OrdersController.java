package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import service.OrderService;
import service.ProductService;
import session.SessionFactory;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class OrdersController implements Initializable {
    @FXML
    private Text adminPanelText;

    @FXML
    private BorderPane borderPane;

    @FXML
    private TableView tableView;

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

    public void onAdminPanelTextClicked() throws IOException {
        new SceneController(borderPane, "/view/admin-panel-view.fxml");
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

        tableView = orderService.initializeOrdersTableThroughReflectionForOrdersView(tableView);
    }
}
