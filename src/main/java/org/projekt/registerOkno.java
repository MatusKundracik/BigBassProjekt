package org.projekt;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class registerOkno extends Application {

    public void start(Stage stage) throws Exception {
        Parent rootPane = FXMLLoader.load(getClass().getResource("/RegisterController.fxml"));

        Scene scene = new Scene(rootPane);
        stage.setTitle("Register");
        stage.setScene(scene);
        stage.show();
//
    }

    public static void main(String[] args) {
        launch(args);
    }

}
