package service;

import dao.OrderDAO;
import dao.ProductDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import model.Product;

import java.util.List;
import java.util.Optional;

public class ProductService {
    private ProductDAO productDAO = new ProductDAO();
    private OrderDAO orderDAO = new OrderDAO();

    public ProductService() {

    }

    public List<Product> findAllProducts() {
        return productDAO.findAll();
    }

    public Product findProductById(Long productId) {
        return productDAO.findById(productId);
    }

    public Product findProductByName(String name) {
        return productDAO.findByName(name);
    }

    public Product insertProduct(Product product) {
        return productDAO.insert(product);
    }

    public Product updateProductById(Long productId, Product updatedProduct) {
        return productDAO.updateById(productId, updatedProduct);
    }

    public Product updateProductByName(String productName, Product updatedProduct) {
        return productDAO.updateByName(productName, updatedProduct);
    }

    public int deleteProductById(Long productId) {
        orderDAO.deleteOrdersWithProductId(productId);
        return productDAO.deleteById(productId);
    }

    public int deleteProductByName(String productName) {
        orderDAO.deleteOrdersWithProductName(productName);
        return productDAO.deleteByName(productName);
    }

    public ObservableList<Product> convertProductListToObservableList(List<Product> productList) {
        ObservableList<Product> observableList = FXCollections.observableArrayList();
        observableList.addAll(productList);
        return observableList;
    }

    public void initializeActionColumnInProductTableForAdminControlPanel(TableColumn actionColumnProductTable, TableView productTableView) {
        actionColumnProductTable.setCellFactory(_ -> new TableCell<Product, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                if ((item == null || empty)) {
                    if (getIndex() == getTableView().getItems().size()) {
                        Button addProductButton = new Button("ADD PRODUCT");

                        addProductButton.setOnAction(_ -> {
                            Stage stage = new Stage();
                            stage.setTitle("Add a new product");

                            Label[] labels = {
                                    new Label("Name: "),
                                    new Label("Description: "),
                                    new Label("Price: "),
                                    new Label("Stock: ")
                            };

                            TextField[] textFields = {
                                    new TextField(),
                                    new TextField(),
                                    new TextField(),
                                    new TextField(),
                            };

                            GridPane gridPane = new GridPane();
                            gridPane.setHgap(10);
                            gridPane.setVgap(10);
                            gridPane.setPadding(new Insets(20, 20, 20, 20));

                            for (int i = 0; i < labels.length; i++) {
                                gridPane.add(labels[i], 0, i);
                                gridPane.add(textFields[i], 1, i);
                            }

                            Button insertProductButton = new Button("Add a new product");

                            insertProductButton.setOnAction(_ -> {
                                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                                errorAlert.setTitle("Orders management");

                                for (int i = 0; i < labels.length; i++) {
                                    if (textFields[i].getText().isEmpty() || textFields[i].getText().isBlank()) {
                                        errorAlert.setHeaderText("All the details must be filled in!");
                                        errorAlert.showAndWait();
                                        return;
                                    }
                                }

                                if (!textFields[2].getText().matches("[0-9]*\\.[0-9]+") || Double.parseDouble(textFields[2].getText()) <= 0) {
                                    errorAlert.setHeaderText("The price must be a positive real number!");
                                    errorAlert.showAndWait();
                                    return;
                                }

                                else if (!textFields[3].getText().matches("[0-9]+") || Integer.parseInt(textFields[3].getText()) < 0) {
                                    errorAlert.setHeaderText("The stock must be an integer greater than or equal to 0!");
                                    errorAlert.showAndWait();
                                    return;
                                }

                                String productName = textFields[0].getText();
                                String productDescription = textFields[1].getText();
                                Double productPrice = Double.parseDouble(textFields[2].getText());
                                Integer stock = Integer.parseInt(textFields[3].getText());

                                Product productToInsert = new Product(productName, productDescription, productPrice, stock);
                                Product insertedProduct = insertProduct(productToInsert);

                                if (insertedProduct != null) {
                                    Alert successAlert = new Alert(Alert.AlertType.CONFIRMATION);
                                    successAlert.setHeaderText("The product has been successfully added!");
                                    successAlert.showAndWait();
                                    stage.close();

                                    productTableView.setItems(convertProductListToObservableList(findAllProducts()));
                                }

                                else {
                                    errorAlert.setHeaderText("An error has occured! Please try again later!");
                                    errorAlert.showAndWait();
                                    stage.close();
                                }
                            });

                            gridPane.add(insertProductButton, 1, labels.length);

                            BorderPane borderPane = new BorderPane();
                            borderPane.setCenter(gridPane);

                            Scene scene = new Scene(borderPane, 350, 200);
                            stage.setScene(scene);
                            stage.setResizable(false);
                            stage.show();
                        });

                        HBox hBox = new HBox(addProductButton);
                        hBox.setAlignment(Pos.CENTER);

                        setGraphic(hBox);
                    }

                    else {
                        setGraphic(null);
                    }
                }

                else {
                    Button editButton = new Button("EDIT");
                    Button deleteButton = new Button("DELETE");

                    editButton.setOnAction(_ -> {
                        Product selectedProduct = getTableView().getItems().get(getIndex());

                        Stage stage = new Stage();
                        stage.setTitle("Edit product '" + selectedProduct.getName() + "'");

                        Label[] labels = {
                                new Label("Name: "), new Label("Description: "), new Label("Price: "),
                                new Label("Stock: ")
                        };

                        TextField[] textFields = {
                                new TextField(selectedProduct.getName()), new TextField(selectedProduct.getDescription()),
                                new TextField(selectedProduct.getPrice().toString()), new TextField(selectedProduct.getStock().toString())
                        };

                        Button editProductButton = new Button("EDIT PRODUCT");
                        editProductButton.setMaxWidth(Double.MAX_VALUE);

                        editProductButton.setOnAction(_ -> {
                            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                            errorAlert.setTitle("Orders management");

                            for (int i = 0; i < labels.length; i++) {
                                if (textFields[i].getText().isEmpty() || textFields[i].getText().isBlank()) {
                                    errorAlert.setHeaderText("All the details must be filled in!");
                                    errorAlert.showAndWait();
                                    return;
                                }
                            }

                            if (!textFields[2].getText().matches("[0-9]*\\.[0-9]+") || Double.parseDouble(textFields[2].getText()) <= 0) {
                                errorAlert.setHeaderText("The price must be a positive real number!");
                                errorAlert.showAndWait();
                                return;
                            }

                            else if (!textFields[3].getText().matches("[0-9]+") || Integer.parseInt(textFields[3].getText()) < 0) {
                                errorAlert.setHeaderText("The stock must be a an integer greater than or equal to 0!");
                                errorAlert.showAndWait();
                                return;
                            }

                            Product editedProduct = new Product(textFields[0].getText(),
                                    textFields[1].getText(),
                                    Double.parseDouble(textFields[2].getText()),
                                    Integer.parseInt(textFields[3].getText()));

                            Product updatedProduct = updateProductById(selectedProduct.getProductId(), editedProduct);

                            if (updatedProduct != null) {
                                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                                alert.setTitle("Orders management");
                                alert.setHeaderText("The product has been successfully updated!");
                                ButtonType okButton = new ButtonType("OK");
                                alert.getButtonTypes().setAll(okButton);
                                alert.showAndWait();

                                productTableView.setItems(convertProductListToObservableList(findAllProducts()));
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

                        gridPane.add(editProductButton, 1, labels.length);

                        BorderPane borderPane = new BorderPane();
                        borderPane.setCenter(gridPane);

                        Scene scene = new Scene(borderPane, 350, 200);
                        stage.setScene(scene);
                        stage.setResizable(false);
                        stage.show();
                    });

                    deleteButton.setOnAction(_ -> {
                        Product selectedProduct = getTableView().getItems().get(getIndex());

                        Dialog<ButtonType> dialog = new Dialog<>();
                        dialog.setTitle("Orders management");
                        dialog.setHeaderText("Are you sure that you want to delete the product '" + selectedProduct.getName() + "'? This will also erase all the orders associated with this product and this operation cannot be undone!");

                        ButtonType okButton = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
                        ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

                        dialog.getDialogPane().getButtonTypes().addAll(okButton, cancelButton);

                        Optional<ButtonType> result = dialog.showAndWait();

                        if (result.isPresent() && result.get() == okButton) {
                            int affectedRows = deleteProductById(selectedProduct.getProductId());

                            if (affectedRows > 0) {
                                Dialog<ButtonType> confirmationDialog = new Dialog<>();
                                confirmationDialog.setTitle("Orders management");
                                confirmationDialog.setHeaderText("The product has been successfully deleted!");
                                confirmationDialog.getDialogPane().getButtonTypes().addAll(okButton);
                                confirmationDialog.showAndWait();

                                productTableView.setItems(convertProductListToObservableList(findAllProducts()));
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
    }
}
