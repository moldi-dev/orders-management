package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import model.Product;
import service.ProductService;
import session.SessionFactory;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ProductsController implements Initializable {
    @FXML
    private Text adminPanelText;

    @FXML
    private TableView tableView;

    @FXML
    private TableColumn idColumn;

    @FXML
    private TableColumn nameColumn;

    @FXML
    private TableColumn descriptionColumn;

    @FXML
    private TableColumn priceColumn;

    @FXML
    private TableColumn stockColumn;

    @FXML
    private TableColumn actionColumn;

    @FXML
    private BorderPane borderPane;

    private ProductService productService = new ProductService();

    public void onYourOrdersTextClicked() throws IOException {
        new SceneController(borderPane, "/view/orders-view.fxml");
    }

    public void onProductsTextClicked() {
        // do nothing, we're already on this view
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
    public void initialize(URL url, ResourceBundle resources) {
        if (SessionFactory.getSignedInUser() == null || !SessionFactory.getSignedInUser().getRole().equals("ADMINISTRATOR")) {
            adminPanelText.setText("");
            adminPanelText.setDisable(true);
        }

        else {
            adminPanelText.setText("Admin panel");
            adminPanelText.setDisable(false);
        }

        idColumn.setCellValueFactory(new PropertyValueFactory<Product, Long>("productId"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<Product, String>("name"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<Product, String>("description"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<Product, Double>("price"));
        stockColumn.setCellValueFactory(new PropertyValueFactory<Product, Double>("stock"));
        actionColumn.setCellValueFactory(new PropertyValueFactory<Product, String>("name"));

        productService.initializeActionColumnInProductsTableForProductsView(actionColumn, tableView);

        tableView.setItems(productService.convertProductListToObservableList(productService.findAllProducts()));
    }
}
