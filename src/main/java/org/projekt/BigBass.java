package org.projekt;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

public class BigBass extends Application {

    public void start(Stage stage) throws Exception {

       Parent loginPane = FXMLLoader.load(getClass().getResource("/LoginController.fxml"));

        Scene scene2 = new Scene(loginPane);
        stage.setTitle("Prihlásenie");
        stage.getIcons().add(new javafx.scene.image.Image(Objects.requireNonNull(getClass().getResourceAsStream("/icons/alfonso.png"))));
        stage.setScene(scene2);
        stage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }

}
