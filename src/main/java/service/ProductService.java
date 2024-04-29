package service;

import dao.OrderDAO;
import dao.ProductDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Order;
import model.Product;
import session.SessionFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

public class ProductService {
    private ProductDAO productDAO = new ProductDAO();
    private OrderDAO orderDAO = new OrderDAO();
    private OrderService orderService = new OrderService();

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

    public void initializeAddProductButtonLogicForAdminControlPanelThroughReflection(Button addProductButton, TableView productTableView) {
        addProductButton.setStyle("-fx-background-color: #0598ff; -fx-text-fill: white;");

        addProductButton.setOnAction(_ -> {
            Stage stage = new Stage();
            stage.setTitle("Add a new product");

            Field[] fields = Product.class.getDeclaredFields();
            Label[] labels = new Label[fields.length];
            TextField[] textFields = new TextField[fields.length];
            Constructor[] constructors = Product.class.getDeclaredConstructors();
            Constructor defaultConstructor = null;

            for (Constructor constructor : constructors) {
                if (constructor.getParameterCount() == 0) {
                    defaultConstructor = constructor;
                }
            }

            for (int i = 0; i < fields.length; i++) {
                fields[i].setAccessible(true);
                textFields[i] = new TextField("");
                labels[i] = new Label(fields[i].getName().toUpperCase() + ":");
            }

            GridPane gridPane = new GridPane();
            gridPane.setHgap(10);
            gridPane.setVgap(10);
            gridPane.setPadding(new Insets(20, 20, 20, 20));

            for (int i = 1; i < labels.length; i++) {
                gridPane.add(labels[i], 0, i);
                gridPane.add(textFields[i], 1, i);
            }

            Button insertProductButton = new Button("Add a new product");
            insertProductButton.setStyle("-fx-background-color: #0598ff; -fx-text-fill: white;");

            Constructor finalDefaultConstructor = defaultConstructor;

            insertProductButton.setOnAction(_ -> {
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setTitle("Orders management");

                for (int i = 1; i < labels.length; i++) {
                    if (textFields[i].getText().isEmpty() || textFields[i].getText().isBlank()) {
                        errorAlert.setHeaderText("All the details must be filled in!");
                        errorAlert.showAndWait();
                        return;
                    }
                }

                if (!textFields[3].getText().matches("[0-9]*\\.[0-9]+") || Double.parseDouble(textFields[3].getText()) <= 0) {
                    errorAlert.setHeaderText("The price must be a positive real number!");
                    errorAlert.showAndWait();
                    return;
                }

                else if (!textFields[4].getText().matches("[0-9]+") || Integer.parseInt(textFields[4].getText()) < 0) {
                    errorAlert.setHeaderText("The stock must be an integer greater than or equal to 0!");
                    errorAlert.showAndWait();
                    return;
                }

                Product productToInsert;

                try {
                    finalDefaultConstructor.setAccessible(true);
                    productToInsert = (Product) finalDefaultConstructor.newInstance();

                    for (int i = 1; i < fields.length; i++) {
                        fields[i].setAccessible(true);

                        if (i == 3) {
                            fields[i].set(productToInsert, Double.parseDouble(textFields[i].getText()));
                        }

                        else if (i == 4) {
                            fields[i].set(productToInsert, Integer.parseInt(textFields[i].getText()));
                        }

                        else {
                            fields[i].set(productToInsert, textFields[i].getText());
                        }
                    }
                }

                catch (InvocationTargetException e) {
                    e.printStackTrace();
                    return;
                }

                catch (InstantiationException e) {
                    e.printStackTrace();
                    return;
                }

                catch (IllegalAccessException e) {
                    e.printStackTrace();
                    return;
                }

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

            Scene scene = new Scene(borderPane);
            stage.setScene(scene);
            stage.setResizable(true);
            stage.setMinWidth(350);
            stage.setMinHeight(200);
            stage.show();
        });
    }

    public void initializeActionColumnInProductsTableForProductsViewThroughReflection(TableColumn actionColumn, TableView tableView) {
        actionColumn.setCellFactory(_ -> new TableCell<Product, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                if (item == null || empty || getTableView().getItems().get(getIndex()).getStock() <= 0) {
                    setGraphic(null);
                }

                else {
                    Button orderButton = new Button("ORDER");
                    orderButton.setStyle("-fx-background-color: #0598ff; -fx-text-fill: white;");

                    orderButton.setOnAction(_ -> {
                        Product selectedProduct = getTableView().getItems().get(getIndex());

                        Stage stage = new Stage();
                        stage.setTitle("Order product '" + selectedProduct.getName() + "'");

                        Field[] productFields = Product.class.getDeclaredFields();
                        Object[] productFieldsValues = new Object[productFields.length];
                        String[] texts = new String[productFields.length];

                        for (int i = 0; i < productFields.length; i++) {
                            productFields[i].setAccessible(true);

                            texts[i] = productFields[i].getName().toUpperCase();

                            try {
                                productFieldsValues[i] = productFields[i].get(selectedProduct);
                                texts[i] = texts[i] + ": " + productFieldsValues[i];
                            }

                            catch (IllegalAccessException e) {
                                e.printStackTrace();
                                return;
                            }
                        }

                        Spinner<Integer> spinner = new Spinner<>(1, selectedProduct.getStock(), 1);

                        Button placeOrderButton = new Button("PLACE ORDER");
                        placeOrderButton.setStyle("-fx-background-color: #0598ff; -fx-text-fill: white;");

                        placeOrderButton.setOnAction(_ -> {
                            Integer quantity = spinner.getValue();

                            Order orderToInsert = new Order(SessionFactory.getSignedInUser().getUserId(),
                                    selectedProduct.getProductId(),
                                    quantity,
                                    selectedProduct.getPrice() * quantity,
                                    new Timestamp(System.currentTimeMillis()));

                            Order insertedOrder = orderService.insertOrder(orderToInsert);

                            if (insertedOrder != null) {
                                selectedProduct.setStock(selectedProduct.getStock() - quantity);

                                if (selectedProduct.getStock() <= 0) {
                                    setGraphic(null);
                                }

                                updateProductById(selectedProduct.getProductId(), selectedProduct);
                                tableView.setItems(convertProductListToObservableList(findAllProducts()));

                                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);

                                alert.setTitle("Orders management");
                                alert.setHeaderText("You have successfully placed a new order!");
                                alert.showAndWait();
                                stage.close();
                            }

                            else {
                                Alert alert = new Alert(Alert.AlertType.ERROR);
                                alert.setTitle("Orders management");
                                alert.setHeaderText("An error occured! Please try again later!");
                                alert.showAndWait();
                                stage.close();
                            }
                        });

                        VBox vBox = new VBox(10);

                        for (String text : texts) {
                            vBox.getChildren().add(new Label(text));
                        }

                        vBox.getChildren().add(spinner);

                        vBox.getChildren().add(placeOrderButton);

                        vBox.setAlignment(Pos.CENTER);

                        stage.setScene(new Scene(vBox, 400, 400));
                        stage.setResizable(true);
                        stage.setMinWidth(400);
                        stage.setMinHeight(400);
                        stage.show();
                    });

                    HBox hBox = new HBox(orderButton);
                    hBox.setAlignment(Pos.CENTER);
                    setGraphic(hBox);
                }
            }
        });
    }

    public void initializeActionColumnInProductTableForAdminControlPanelThroughReflection(TableColumn actionColumnProductTable, TableView productTableView) {
        actionColumnProductTable.setCellFactory(_ -> new TableCell<Product, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                if ((item == null || empty)) {
                    setGraphic(null);
                }

                else {
                    Button editButton = new Button("EDIT");
                    Button deleteButton = new Button("DELETE");

                    editButton.setStyle("-fx-background-color: #0598ff; -fx-text-fill: white;");
                    deleteButton.setStyle("-fx-background-color: #0598ff; -fx-text-fill: white;");

                    editButton.setOnAction(_ -> {
                        Product selectedProduct = getTableView().getItems().get(getIndex());

                        Stage stage = new Stage();
                        stage.setTitle("Edit product '" + selectedProduct.getName() + "'");

                        Field[] fields = Product.class.getDeclaredFields();
                        Label[] labels = new Label[fields.length];
                        TextField[] textFields = new TextField[fields.length];
                        Object[] fieldValues = new Object[fields.length];
                        Constructor[] constructors = Product.class.getDeclaredConstructors();
                        Constructor defaultConstructor = null;

                        for (Constructor constructor : constructors) {
                            if (constructor.getParameterCount() == 0) {
                                defaultConstructor = constructor;
                            }
                        }

                        for (int i = 0; i < fields.length; i++) {
                            fields[i].setAccessible(true);

                            labels[i] = new Label(fields[i].getName().toUpperCase() + ":");

                            try {
                                fieldValues[i] = fields[i].get(selectedProduct);
                                textFields[i] = new TextField(fieldValues[i].toString());
                            }

                            catch (IllegalAccessException e) {
                                e.printStackTrace();
                                return;
                            }
                        }

                        Button editProductButton = new Button("EDIT PRODUCT");
                        editProductButton.setStyle("-fx-background-color: #0598ff; -fx-text-fill: white;");
                        editProductButton.setMaxWidth(Double.MAX_VALUE);

                        Constructor finalDefaultConstructor = defaultConstructor;

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

                            if (!textFields[3].getText().matches("[0-9]*\\.[0-9]+") || Double.parseDouble(textFields[3].getText()) <= 0) {
                                errorAlert.setHeaderText("The price must be a positive real number!");
                                errorAlert.showAndWait();
                                return;
                            }

                            else if (!textFields[4].getText().matches("[0-9]+") || Integer.parseInt(textFields[4].getText()) < 0) {
                                errorAlert.setHeaderText("The stock must be a an integer greater than or equal to 0!");
                                errorAlert.showAndWait();
                                return;
                            }

                            Product editedProduct;

                            try {
                                finalDefaultConstructor.setAccessible(true);
                                editedProduct = (Product) finalDefaultConstructor.newInstance();

                                for (int i = 1; i < fields.length; i++) {
                                    fields[i].setAccessible(true);

                                    if (i == 3) {
                                        fields[i].set(editedProduct, Double.parseDouble(textFields[i].getText()));
                                    }

                                    else if (i == 4) {
                                        fields[i].set(editedProduct, Integer.parseInt(textFields[i].getText()));
                                    }

                                    else {
                                        fields[i].set(editedProduct, textFields[i].getText());
                                    }
                                }
                            }

                            catch (InvocationTargetException e) {
                                e.printStackTrace();
                                return;
                            }

                            catch (InstantiationException e) {
                                e.printStackTrace();
                                return;
                            }

                            catch (IllegalAccessException e) {
                                e.printStackTrace();
                                return;
                            }

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

                        for (int i = 1; i < labels.length; i++) {
                            gridPane.add(labels[i], 0, i);
                            gridPane.add(textFields[i], 1, i);
                        }

                        gridPane.add(editProductButton, 1, labels.length);

                        BorderPane borderPane = new BorderPane();
                        borderPane.setCenter(gridPane);

                        Scene scene = new Scene(borderPane);
                        stage.setScene(scene);
                        stage.setResizable(true);
                        stage.setMinWidth(350);
                        stage.setMinHeight(300);
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

    public TableView initializeProductsTableThroughReflectionForProductsView(TableView tableView) {
        Field[] fields = Product.class.getDeclaredFields();

        for (Field field : fields) {
            field.setAccessible(true);

            TableColumn column = new TableColumn(field.getName());
            column.setCellValueFactory(new PropertyValueFactory<>(field.getName()));
            tableView.getColumns().add(column);
        }

        TableColumn actionColumn = new TableColumn("action");
        actionColumn.setCellValueFactory(new PropertyValueFactory<Product, String>("name"));

        initializeActionColumnInProductsTableForProductsViewThroughReflection(actionColumn, tableView);

        tableView.getColumns().add(actionColumn);

        tableView.setItems(convertProductListToObservableList(findAllProducts()));

        return tableView;
    }

    public TableView initializeProductsTableThroughReflectionForAdminControlPanel(TableView productTableView) {
        Field[] fields = Product.class.getDeclaredFields();

        for (Field field : fields) {
            field.setAccessible(true);

            TableColumn column = new TableColumn(field.getName());
            column.setCellValueFactory(new PropertyValueFactory<>(field.getName()));
            productTableView.getColumns().add(column);
        }

        TableColumn actionColumn = new TableColumn("action");
        actionColumn.setCellValueFactory(new PropertyValueFactory<Product, String>("name"));

        initializeActionColumnInProductTableForAdminControlPanelThroughReflection(actionColumn, productTableView);

        productTableView.getColumns().add(actionColumn);

        productTableView.setItems(convertProductListToObservableList(findAllProducts()));

        return productTableView;
    }
}
