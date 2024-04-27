package controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import service.ClientService;

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

    private ClientService clientService = new ClientService();

    public SignInController() {

    }

    public void onSignInButtonClicked() {
        String username = usernameTextField.getText();
        String password = passwordTextField.getText();
        clientService.signInClient(username, password);
    }

    public void onSignUpHyperlinkClicked() throws IOException {
        new SceneController(borderPane, "/view/sign-up-view.fxml", 720, 500);
    }
}