package service;

import dao.UserDAO;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import model.User;
import session.SessionFactory;

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
            error.setContentText("Please enter your credentials!");
            error.getButtonTypes().setAll(okButtonType);
            error.showAndWait();
            return false;
        }

        User foundUser = userDAO.findByUsername(username);

        if (foundUser != null && foundUser.getPassword().equals(password)) {
            ok.setTitle("Orders management application");
            ok.setContentText("You have successfully signed in!");
            ok.getButtonTypes().setAll(okButtonType);
            ok.showAndWait();

            SessionFactory.setSignedInUser(foundUser);
            return true;
        }

        else {
            error.setTitle("Orders management application");
            error.setContentText("Invalid credentials provided!");
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
            error.setContentText("Please enter all the details!");
            error.showAndWait();
            return false;
        }

        else if (!email.matches("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            error.setContentText("The email address must be of form 'example@email.com'");
            error.showAndWait();
            return false;
        }

        else if (!password.equals(confirmPassword)) {
            error.setContentText("Passwords do not match!");
            error.showAndWait();
            return false;
        }

        User foundUser = userDAO.findByUsername(username);

        if (foundUser != null) {
            error.setContentText("This username is already taken!");
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
            error.setContentText("The credentials provided can have at most 100 characters!");
            error.showAndWait();
            return false;
        }

        User userToInsert = new User(username, firstName, lastName, email, password, phoneNumber, address, "CUSTOMER");
        User insertedUser = userDAO.insert(userToInsert);

        if (insertedUser != null) {
            ok.setContentText("You have successfuly registered! You can now sign in.");
            ok.showAndWait();
            return true;
        }

        else {
            error.setContentText("An error has occured, please try again later!");
            error.showAndWait();
            return false;
        }
    }
}
