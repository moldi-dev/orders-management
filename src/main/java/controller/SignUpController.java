package controller;

import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import service.UserService;

import java.io.IOException;

public class SignUpController {
    public BorderPane borderPane;
    public TextField usernameTextField;
    public PasswordField passwordTextField;
    public Button signUpButton;
    public Hyperlink signInHyperlink;
    public TextField firstNameTextField;
    public TextField lastNameTextField;
    public TextField emailTextField;
    public TextField addressTextField;
    public TextField phoneNumberTextField;
    public PasswordField confirmPasswordTextField;

    private UserService userService = new UserService();

    public SignUpController() {

    }

    public void onSignInHyperlinkClicked() throws IOException {
        new SceneController(borderPane, "/view/sign-in-view.fxml");
    }

    public void onSignUpButtonClicked() throws IOException {
        String username = usernameTextField.getText();
        String password = passwordTextField.getText();
        String firstName = firstNameTextField.getText();
        String lastName = lastNameTextField.getText();
        String email = emailTextField.getText();
        String address = addressTextField.getText();
        String phoneNumber = phoneNumberTextField.getText();
        String confirmPassword = confirmPasswordTextField.getText();

        if (userService.signUpUser(username, password, firstName, lastName, email, address, phoneNumber, confirmPassword)) {
            new SceneController(borderPane,"/view/sign-in-view.fxml");
        }
    }
}
