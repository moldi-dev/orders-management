package service;

import dao.ClientDAO;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import model.Client;
import session.SessionFactory;

public class ClientService {
    private final ClientDAO clientDAO = new ClientDAO();

    public ClientService() {

    }

    public void signInClient(String username, String password) {
        Alert ok = new Alert(Alert.AlertType.CONFIRMATION);
        Alert error = new Alert(Alert.AlertType.ERROR);
        ButtonType okButtonType = new ButtonType("OK");

        if (username.isEmpty() || password.isEmpty() || username.isBlank() || password.isBlank()) {
            error.setTitle("Orders management application");
            error.setContentText("Please enter your credentials!");
            error.getButtonTypes().setAll(okButtonType);
            error.showAndWait();
            return;
        }

        Client foundClient = clientDAO.findByUsername(username);

        if (foundClient != null && foundClient.getPassword().equals(password)) {
            ok.setTitle("Orders management application");
            ok.setContentText("You have successfully signed in!");
            ok.getButtonTypes().setAll(okButtonType);
            ok.showAndWait();

            SessionFactory.setSignedInClient(foundClient);

            // TODO: redirect the client to the products page
            // new SceneController(borderPane, "/view/products-view.fxml", 1280, 720);
        }

        else {
            error.setTitle("Orders management application");
            error.setContentText("Invalid credentials provided!");
            error.getButtonTypes().setAll(okButtonType);
            error.showAndWait();
        }
    }

    public void signUpClient(String username, String password, String firstName, String lastName, String email, String phoneNumber, String address, String confirmPassword) {
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
            error.setContentText("Please enter all the details!");
            error.showAndWait();
            return;
        }

        else if (!email.matches("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            error.setContentText("The email address must be of form 'example@email.com'");
            error.showAndWait();
            return;
        }

        else if (!password.equals(confirmPassword)) {
            error.setContentText("Passwords do not match!");
            error.showAndWait();
            return;
        }

        Client foundClient = clientDAO.findByUsername(username);

        if (foundClient != null) {
            error.setContentText("This username is already taken!");
            error.showAndWait();
            return;
        }

        if (username.length() > 100 ||
            password.length() > 100 ||
            firstName.length() > 100 ||
            lastName.length() > 100 ||
            email.length() > 100 ||
            phoneNumber.length() > 100 ||
            address.length() > 100) {
            error.setContentText("The credentials provided can have at most 100 characters!");
            error.showAndWait();
            return;
        }

        Client clientToInsert = new Client(username, firstName, lastName, email, password, phoneNumber, address);
        Client insertedClient = clientDAO.insert(clientToInsert);

        if (insertedClient != null) {
            ok.setContentText("You have successfuly registered! You can now sign in.");
            ok.showAndWait();
        }
    }
}
