package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.Order;
import model.Product;
import service.OrderService;
import service.ProductService;
import session.SessionFactory;

import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
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
    private OrderService orderService = new OrderService();

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

    public void onAdminPanelTextClicked() {
        if (SessionFactory.getSignedInUser() != null && SessionFactory.getSignedInUser().getRole().equals("ADMINISTRATOR")) {
            // TODO: redirect to the admin panel view
            System.out.println("REDIRECTED TO THE ADMIN PANEL");
        }
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

        actionColumn.setCellFactory(_ -> new TableCell<Product, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                if (item == null || empty || getTableView().getItems().get(getIndex()).getStock() <= 0) {
                    setGraphic(null);
                }

                else {
                    Button orderButton = new Button("ORDER");
                    orderButton.setOnAction(_ -> {
                        Product selectedProduct = getTableView().getItems().get(getIndex());

                        Stage stage = new Stage();
                        stage.setTitle("Orders management");

                        Text nameText = new Text("Product name: " + selectedProduct.getName());
                        Text descriptionText = new Text("Description: " + selectedProduct.getDescription());
                        Text priceText = new Text("Price: " + selectedProduct.getPrice());
                        Text stockText = new Text("Available stock: " + selectedProduct.getStock());

                        Spinner<Integer> spinner = new Spinner<>(1, selectedProduct.getStock(), 1);

                        Button placeOrderButton = new Button("PLACE ORDER");
                        placeOrderButton.setOnAction(_ -> {
                            int quantity = spinner.getValue();
                            Order order = new Order(SessionFactory.getSignedInUser().getUserId(), selectedProduct.getProductId(), quantity, selectedProduct.getPrice() * quantity, new Timestamp(System.currentTimeMillis()));
                            Order insertedOrder = orderService.insertOrder(order);

                            if (insertedOrder != null) {
                                selectedProduct.setStock(selectedProduct.getStock() - quantity);

                                if (selectedProduct.getStock() <= 0) {
                                    setGraphic(null);
                                }

                                productService.updateProductById(selectedProduct.getProductId(), selectedProduct);
                                tableView.setItems(productService.convertProductListToObservableList(productService.getAllProducts()));

                                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);

                                alert.setTitle("Orders management");
                                alert.setContentText("You have successfully placed a new order!");
                                alert.showAndWait();
                                stage.close();
                            }

                            else {
                                Alert alert = new Alert(Alert.AlertType.ERROR);
                                alert.setTitle("Orders management");
                                alert.setContentText("An error occured! Please try again later!");
                                alert.showAndWait();
                            }
                        });

                        VBox vbox = new VBox(10, nameText, descriptionText, priceText, stockText, new Label("Select the quantity:"), spinner, placeOrderButton);
                        vbox.setAlignment(Pos.CENTER);

                        stage.setScene(new Scene(vbox, 400, 400));
                        stage.setResizable(false);
                        stage.show();
                    });

                    HBox hBox = new HBox(orderButton);
                    hBox.setAlignment(Pos.CENTER);
                    setGraphic(hBox);
                }
            }
        });

        tableView.setItems(productService.convertProductListToObservableList(productService.getAllProducts()));
    }
}
