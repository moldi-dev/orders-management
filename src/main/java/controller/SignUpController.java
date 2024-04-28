package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import service.UserService;

import java.io.IOException;

public class SignUpController {
    @FXML
    private BorderPane borderPane;

    @FXML
    private TextField usernameTextField;

    @FXML
    private PasswordField passwordTextField;

    @FXML
    private Button signUpButton;

    @FXML
    private Hyperlink signInHyperlink;

    @FXML
    private TextField firstNameTextField;

    @FXML
    private TextField lastNameTextField;

    @FXML
    private TextField emailTextField;

    @FXML
    private TextField addressTextField;

    @FXML
    private TextField phoneNumberTextField;

    @FXML
    private PasswordField confirmPasswordTextField;

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
