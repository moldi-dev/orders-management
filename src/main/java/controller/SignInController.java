package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import service.UserService;

import java.io.IOException;

public class SignInController {

    @FXML
    private Hyperlink signUpHyperlink;

    @FXML
    private Button signInButton;

    @FXML
    private PasswordField passwordTextField;

    @FXML
    private TextField usernameTextField;

    @FXML
    private BorderPane borderPane;

    private UserService userService = new UserService();

    public SignInController() {

    }

    public void onSignInButtonClicked() throws IOException {
        String username = usernameTextField.getText();
        String password = passwordTextField.getText();

        if (userService.signInUser(username, password)) {
            new SceneController(borderPane, "/view/products-view.fxml");
        }
    }

    public void onSignUpHyperlinkClicked() throws IOException {
        new SceneController(borderPane, "/view/sign-up-view.fxml");
    }
}