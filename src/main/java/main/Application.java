package main;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.User;
import session.SessionFactory;

import java.io.IOException;

public class Application extends javafx.application.Application {
    @Override
    public void start(Stage stage) throws IOException {
        //FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("/view/sign-in-view.fxml"));
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/admin-panel-view.fxml"));
        SessionFactory.setSignedInUser(new User(1L, "admin", "admin", "t", "t", "t", "t", "t", "ADMINISTRATOR"));
        Scene scene = new Scene(fxmlLoader.load(), 1280, 720);
        stage.setResizable(false);
        stage.setTitle("Orders management application");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}