package controller;

import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;
import session.SessionFactory;

import java.io.IOException;

public class AdminPanelController {
    @FXML
    private BorderPane borderPane;

    public void onYourOrdersTextClicked() throws IOException {
        new SceneController(borderPane, "/view/orders-view.fxml");
    }

    public void onProductsTextClicked() throws IOException {
        new SceneController(borderPane, "/view/products-view.fxml");
    }

    public void onSignOutTextClicked() throws IOException {
        SessionFactory.setSignedInUser(null);

        new SceneController(borderPane, "/view/sign-in-view.fxml");
    }

    public void onAdminPanelTextClicked() {
        // do nothing, we're already on this view
    }
}
