package controller;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import service.ClientService;

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

    private ClientService clientService = new ClientService();

    public SignUpController() {

    }

    public void onSignInHyperlinkClicked() throws IOException {
        new SceneController(borderPane, "/view/sign-in-view.fxml", 700, 500);
    }

    public void onSignUpButtonClicked() {
        String username = usernameTextField.getText();
        String password = passwordTextField.getText();
        String firstName = firstNameTextField.getText();
        String lastName = lastNameTextField.getText();
        String email = emailTextField.getText();
        String address = addressTextField.getText();
        String phoneNumber = phoneNumberTextField.getText();
        String confirmPassword = confirmPasswordTextField.getText();

        clientService.signUpClient(username, password, firstName, lastName, email, address, phoneNumber, confirmPassword);
    }
}
