package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
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
import java.util.Optional;
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
    private TableColumn productActionColumnProductTable;

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

        actionColumnUserTable.setCellFactory(_ -> new TableCell<User, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                if (item == null || empty) {
                    setGraphic(null);
                }

                else {
                    Button editButton = new Button("EDIT");
                    Button deleteButton = new Button("DELETE");

                    editButton.setOnAction(_ -> {
                        User selectedUser = getTableView().getItems().get(getIndex());

                        Stage stage = new Stage();
                        stage.setTitle("Edit user '" + selectedUser.getUsername() + "'");

                        Label[] labels = {
                                new Label("Username: "), new Label("First name: "), new Label("Last name: "),
                                new Label("Email: "), new Label("Password: "), new Label("Phone number: "),
                                new Label("Address: "), new Label("Role: ")
                        };

                        TextField[] textFields = {
                                new TextField(selectedUser.getUsername()), new TextField(selectedUser.getFirstName()),
                                new TextField(selectedUser.getLastName()), new TextField(selectedUser.getEmail()),
                                new TextField(selectedUser.getPassword()), new TextField(selectedUser.getPhoneNumber()),
                                new TextField(selectedUser.getAddress()), new TextField(selectedUser.getRole())
                        };

                        Button editUserButton = new Button("EDIT USER");
                        editUserButton.setMaxWidth(Double.MAX_VALUE);

                        editUserButton.setOnAction(_ -> {
                            User editedUser = new User(textFields[0].getText(),
                                    textFields[1].getText(),
                                    textFields[2].getText(),
                                    textFields[3].getText(),
                                    textFields[4].getText(),
                                    textFields[5].getText(),
                                    textFields[6].getText(),
                                    textFields[7].getText());

                            User updatedUser = userService.updateUserById(selectedUser.getUserId(), editedUser);

                            if (updatedUser != null) {
                                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                                alert.setTitle("Orders management");
                                alert.setHeaderText("The user has been successfully updated!");
                                ButtonType okButton = new ButtonType("OK");
                                alert.getButtonTypes().setAll(okButton);
                                alert.showAndWait();

                                userTableView.setItems(userService.convertUserListToObservableList(userService.findAllUsers()));
                                stage.close();
                            }

                            else {
                                Alert alert = new Alert(Alert.AlertType.ERROR);
                                alert.setTitle("Orders management");
                                alert.setHeaderText("An error has occured! Please try again later!");
                                ButtonType okButton = new ButtonType("OK");
                                alert.getButtonTypes().setAll(okButton);
                                alert.showAndWait();

                                stage.close();
                            }
                        });

                        GridPane gridPane = new GridPane();
                        gridPane.setHgap(10);
                        gridPane.setVgap(10);
                        gridPane.setPadding(new Insets(20, 20, 20, 20));

                        for (int i = 0; i < labels.length; i++) {
                            gridPane.add(labels[i], 0, i);
                            gridPane.add(textFields[i], 1, i);
                        }

                        gridPane.add(editUserButton, 1, labels.length);

                        BorderPane borderPane = new BorderPane();
                        borderPane.setCenter(gridPane);

                        Scene scene = new Scene(borderPane, 350, 325);
                        stage.setScene(scene);
                        stage.setResizable(false);
                        stage.show();
                    });

                    deleteButton.setOnAction(_ -> {
                        User selectedUser = getTableView().getItems().get(getIndex());

                        Dialog<ButtonType> dialog = new Dialog<>();
                        dialog.setTitle("Orders management");
                        dialog.setHeaderText("Are you sure that you want to delete the user '" + selectedUser.getUsername() + "'? This operation cannot be undone!");

                        ButtonType okButton = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
                        ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

                        dialog.getDialogPane().getButtonTypes().addAll(okButton, cancelButton);

                        Optional<ButtonType> result = dialog.showAndWait();

                        if (result.isPresent() && result.get() == okButton) {
                            int affectedRows = userService.deleteUserById(selectedUser.getUserId());

                            if (affectedRows > 0) {
                                Dialog<ButtonType> confirmationDialog = new Dialog<>();
                                confirmationDialog.setTitle("Orders management");
                                confirmationDialog.setHeaderText("The user has been successfully deleted!");
                                confirmationDialog.getDialogPane().getButtonTypes().addAll(okButton);
                                confirmationDialog.showAndWait();

                                userTableView.setItems(userService.convertUserListToObservableList(userService.findAllUsers()));
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

                    HBox hBox = new HBox(editButton, deleteButton);
                    hBox.setSpacing(20);
                    hBox.setAlignment(Pos.CENTER);
                    setGraphic(hBox);
                }
            }
        });

        userTableView.setItems(userService.convertUserListToObservableList(userService.findAllUsers()));

        productIdColumnProductTable.setCellValueFactory(new PropertyValueFactory<Product, Long>("productId"));
        productNameColumnProductTable.setCellValueFactory(new PropertyValueFactory<Product, String>("name"));
        productDescriptionColumnProductTable.setCellValueFactory(new PropertyValueFactory<Product, String>("description"));
        productPriceColumnProductTable.setCellValueFactory(new PropertyValueFactory<Product, Double>("price"));
        productStockColumnProductTable.setCellValueFactory(new PropertyValueFactory<Product, Integer>("stock"));
        productActionColumnProductTable.setCellValueFactory(new PropertyValueFactory<Product, String>("name"));

        productActionColumnProductTable.setCellFactory(_ -> new TableCell<Product, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                if (item == null || empty) {
                    setGraphic(null);
                }

                else {
                    Button editButton = new Button("EDIT");
                    Button deleteButton = new Button("DELETE");

                    editButton.setOnAction(_ -> {
                        // Handle edit functionality
                    });

                    deleteButton.setOnAction(_ -> {
                        // Handle delete functionality
                    });

                    HBox hBox = new HBox(editButton, deleteButton);
                    hBox.setSpacing(20);
                    hBox.setAlignment(Pos.CENTER);
                    setGraphic(hBox);
                }
            }
        });

        productTableView.setItems(productService.convertProductListToObservableList(productService.findAllProducts()));

        orderIdColumnOrderTable.setCellValueFactory(new PropertyValueFactory<Order, Long>("orderId"));
        userIdColumnOrderTable.setCellValueFactory(new PropertyValueFactory<Order, Long>("userId"));
        productIdColumnOrderTable.setCellValueFactory(new PropertyValueFactory<Order, Long>("productId"));
        quantityColumnOrderTable.setCellValueFactory(new PropertyValueFactory<Order, Integer>("quantity"));
        totalPriceColumnOrderTable.setCellValueFactory(new PropertyValueFactory<Order, Double>("totalPrice"));
        createdAtColumnOrderTable.setCellValueFactory(new PropertyValueFactory<Order, Timestamp>("createdAt"));
        actionColumnOrderTable.setCellValueFactory(new PropertyValueFactory<Order, Long>("orderId"));

        actionColumnOrderTable.setCellFactory(_ -> new TableCell<Order, Long>() {
            @Override
            protected void updateItem(Long item, boolean empty) {
                super.updateItem(item, empty);

                if (item == null || empty) {
                    setGraphic(null);
                }

                else {
                    Button editButton = new Button("EDIT");
                    Button deleteButton = new Button("DELETE");

                    editButton.setOnAction(_ -> {
                        // Handle edit functionality
                    });

                    deleteButton.setOnAction(_ -> {
                        // Handle delete functionality
                    });

                    HBox hBox = new HBox(editButton, deleteButton);
                    hBox.setSpacing(20);
                    hBox.setAlignment(Pos.CENTER);
                    setGraphic(hBox);
                }
            }
        });

        orderTableView.setItems(orderService.convertOrderListToObservableList(orderService.findAllOrders()));
    }
}
