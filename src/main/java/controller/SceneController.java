package controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import main.Application;

import java.io.IOException;
import java.util.Objects;

public class SceneController {
    public SceneController(BorderPane currentBorderPane, String fxml, Integer windowWidth, Integer windowHeight) throws IOException {
        BorderPane nextBorderPane = FXMLLoader.load(Objects.requireNonNull(Application.class.getResource(fxml)));
        currentBorderPane.getChildren().removeAll();
        currentBorderPane.getChildren().setAll(nextBorderPane);

        Stage currentStage = (Stage) currentBorderPane.getScene().getWindow();

        currentStage.setWidth(windowWidth);
        currentStage.setHeight(windowHeight);
    }
}
