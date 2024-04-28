package service;

import dao.UserDAO;
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
import model.User;
import session.SessionFactory;

import java.util.List;
import java.util.Optional;

public class UserService {
    private final UserDAO userDAO = new UserDAO();

    public UserService() {

    }

    public boolean signInUser(String username, String password) {
        Alert ok = new Alert(Alert.AlertType.CONFIRMATION);
        Alert error = new Alert(Alert.AlertType.ERROR);
        ButtonType okButtonType = new ButtonType("OK");

        if (username.isEmpty() || password.isEmpty() || username.isBlank() || password.isBlank()) {
            error.setTitle("Orders management application");
            error.setHeaderText("Please enter your credentials!");
            error.getButtonTypes().setAll(okButtonType);
            error.showAndWait();
            return false;
        }

        User foundUser = userDAO.findByUsername(username);

        if (foundUser != null && foundUser.getPassword().equals(password)) {
            ok.setTitle("Orders management application");
            ok.setHeaderText("You have successfully signed in!");
            ok.getButtonTypes().setAll(okButtonType);
            ok.showAndWait();

            SessionFactory.setSignedInUser(foundUser);
            return true;
        }

        else {
            error.setTitle("Orders management application");
            error.setHeaderText("Invalid credentials provided!");
            error.getButtonTypes().setAll(okButtonType);
            error.showAndWait();
            return false;
        }
    }

    public boolean signUpUser(String username, String password, String firstName, String lastName, String email, String phoneNumber, String address, String confirmPassword) {
        Alert ok = new Alert(Alert.AlertType.CONFIRMATION);
        Alert error = new Alert(Alert.AlertType.ERROR);
        ButtonType okButtonType = new ButtonType("OK");
        ok.setTitle("Orders management application");
        error.setTitle("Orders management application");
        ok.getButtonTypes().setAll(okButtonType);
        error.getButtonTypes().setAll(okButtonType);

        if (username.isEmpty() || username.isBlank() ||
            password.isEmpty() || password.isBlank() ||
            firstName.isEmpty() || firstName.isBlank() ||
            lastName.isEmpty() || lastName.isBlank() ||
            email.isEmpty() || email.isBlank() ||
            phoneNumber.isEmpty() || phoneNumber.isBlank() ||
            address.isEmpty() || address.isBlank() ||
            confirmPassword.isEmpty() || confirmPassword.isBlank())
        {
            error.setHeaderText("Please enter all the details!");
            error.showAndWait();
            return false;
        }

        else if (!email.matches("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            error.setHeaderText("The email address must be of form 'example@email.com'");
            error.showAndWait();
            return false;
        }

        else if (!password.equals(confirmPassword)) {
            error.setHeaderText("Passwords do not match!");
            error.showAndWait();
            return false;
        }

        User foundUser = userDAO.findByUsername(username);

        if (foundUser != null) {
            error.setHeaderText("This username is already taken!");
            error.showAndWait();
            return false;
        }

        if (username.length() > 100 ||
            password.length() > 100 ||
            firstName.length() > 100 ||
            lastName.length() > 100 ||
            email.length() > 100 ||
            phoneNumber.length() > 100 ||
            address.length() > 100) {
            error.setHeaderText("The credentials provided can have at most 100 characters!");
            error.showAndWait();
            return false;
        }

        User userToInsert = new User(username, firstName, lastName, email, password, phoneNumber, address, "CUSTOMER");
        User insertedUser = userDAO.insert(userToInsert);

        if (insertedUser != null) {
            ok.setHeaderText("You have successfuly registered! You can now sign in.");
            ok.showAndWait();
            return true;
        }

        else {
            error.setHeaderText("An error has occured, please try again later!");
            error.showAndWait();
            return false;
        }
    }

    public User findUserById(Long id) {
        return userDAO.findById(id);
    }

    public User findUserByUsername(String username) {
        return userDAO.findByUsername(username);
    }

    public List<User> findAllUsers() {
        return userDAO.findAll();
    }

    public User insertUser(User user) {
        return userDAO.insert(user);
    }

    public User updateUserById(Long id, User updatedUser) {
        return userDAO.updateById(id, updatedUser);
    }

    public User updateUserByUsername(String username, User updatedUser) {
        return userDAO.updateByUsername(username, updatedUser);
    }

    public int deleteUserById(Long id) {
        return userDAO.deleteById(id);
    }

    public int deleteUserByUsername(String username) {
        return userDAO.deleteByUsername(username);
    }

    public ObservableList<User> convertUserListToObservableList(List<User> userList) {
        ObservableList<User> observableList = FXCollections.observableArrayList();
        observableList.addAll(userList);
        return observableList;
    }

    public void initializeActionColumnInUserTableForAdminControlPanel(TableColumn actionColumnUserTable, TableView userTableView) {
        actionColumnUserTable.setCellFactory(_ -> new TableCell<User, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                if (item == null || empty) {
                    if (getIndex() == getTableView().getItems().size()) {
                        Button addUserButton = new Button("ADD USER");

                        addUserButton.setOnAction(_ -> {
                            Stage stage = new Stage();
                            stage.setTitle("Add a new user");

                            Label[] labels = {
                                    new Label("Username: "),
                                    new Label("First name: "),
                                    new Label("Last name: "),
                                    new Label("Email: "),
                                    new Label("Password: "),
                                    new Label("Phone number: "),
                                    new Label("Address: "),
                                    new Label("Role: ")
                            };

                            TextField[] textFields = {
                                    new TextField(),
                                    new TextField(),
                                    new TextField(),
                                    new TextField(),
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

                            Button insertUserButton = new Button("Add a new user");

                            insertUserButton.setOnAction(_ -> {
                                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                                errorAlert.setTitle("Orders management");

                                for (int i = 0; i < labels.length; i++) {
                                    if (textFields[i].getText().isEmpty() || textFields[i].getText().isBlank()) {
                                        errorAlert.setHeaderText("All the details must be filled in!");
                                        errorAlert.showAndWait();
                                        return;
                                    }

                                    else if (textFields[i].getText().length() > 100) {
                                        errorAlert.setHeaderText("The fields must contain at most 100 characters!");
                                        errorAlert.showAndWait();
                                        return;
                                    }
                                }

                                if (!textFields[3].getText().matches("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
                                    errorAlert.setHeaderText("The email address must be of form 'example@email.com'");
                                    errorAlert.showAndWait();
                                    return;
                                }

                                User userToInsert = new User(textFields[0].getText(),
                                        textFields[1].getText(),
                                        textFields[2].getText(),
                                        textFields[3].getText(),
                                        textFields[4].getText(),
                                        textFields[5].getText(),
                                        textFields[6].getText(),
                                        textFields[7].getText());

                                User insertedUser = insertUser(userToInsert);

                                if (insertedUser != null) {
                                    Alert successAlert = new Alert(Alert.AlertType.CONFIRMATION);
                                    successAlert.setHeaderText("The user has been successfully added!");
                                    successAlert.showAndWait();
                                    stage.close();

                                    userTableView.setItems(convertUserListToObservableList(findAllUsers()));
                                }

                                else {
                                    errorAlert.setHeaderText("An error has occured! Please try again later!");
                                    errorAlert.showAndWait();
                                    stage.close();
                                }
                            });

                            gridPane.add(insertUserButton, 1, labels.length);

                            BorderPane borderPane = new BorderPane();
                            borderPane.setCenter(gridPane);

                            Scene scene = new Scene(borderPane, 350, 325);
                            stage.setScene(scene);
                            stage.setResizable(false);
                            stage.show();
                        });

                        HBox hBox = new HBox(addUserButton);
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
                            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                            errorAlert.setTitle("Orders management");

                            for (int i = 0; i < textFields.length; i++) {
                                if (textFields[i].getText().length() > 100) {
                                    errorAlert.setHeaderText("The fields can have at most 100 characters!");
                                    errorAlert.showAndWait();
                                    return;
                                }

                                else if (textFields[i].getText().isBlank() || textFields[i].getText().isEmpty()) {
                                    errorAlert.setHeaderText("All the fields must be filled in!");
                                    errorAlert.showAndWait();
                                    return;
                                }
                            }

                            if (!textFields[3].getText().matches("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
                                errorAlert.setHeaderText("The email address must be of form 'example@email.com'");
                                errorAlert.showAndWait();
                                return;
                            }

                            User editedUser = new User(textFields[0].getText(),
                                    textFields[1].getText(),
                                    textFields[2].getText(),
                                    textFields[3].getText(),
                                    textFields[4].getText(),
                                    textFields[5].getText(),
                                    textFields[6].getText(),
                                    textFields[7].getText());

                            User updatedUser = updateUserById(selectedUser.getUserId(), editedUser);

                            if (updatedUser != null) {
                                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                                alert.setTitle("Orders management");
                                alert.setHeaderText("The user has been successfully updated!");
                                ButtonType okButton = new ButtonType("OK");
                                alert.getButtonTypes().setAll(okButton);
                                alert.showAndWait();

                                userTableView.setItems(convertUserListToObservableList(findAllUsers()));
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
                            int affectedRows = deleteUserById(selectedUser.getUserId());

                            if (affectedRows > 0) {
                                Dialog<ButtonType> confirmationDialog = new Dialog<>();
                                confirmationDialog.setTitle("Orders management");
                                confirmationDialog.setHeaderText("The user has been successfully deleted!");
                                confirmationDialog.getDialogPane().getButtonTypes().addAll(okButton);
                                confirmationDialog.showAndWait();

                                userTableView.setItems(convertUserListToObservableList(findAllUsers()));
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
