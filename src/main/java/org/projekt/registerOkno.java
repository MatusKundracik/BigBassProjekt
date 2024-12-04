package org.projekt;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class registerOkno extends Application {

    public void start(Stage stage) throws Exception {
        Parent rootPane = FXMLLoader.load(getClass().getResource("/RegisterController.fxml"));
        Parent ulovokPane = FXMLLoader.load(getClass().getResource("/UlovkyController.fxml"));
        Parent loginPane = FXMLLoader.load(getClass().getResource("/LoginController.fxml"));


//        Scene scene = new Scene(rootPane);
//        stage.setTitle("Register");
//        stage.setScene(scene);
//        stage.show();
//
////        Scene scene1 = new Scene(ulovokPane);
////        stage.setTitle("Ulovky");
////        stage.setScene(scene1);
////        stage.show();
//
//        Scene scene2 = new Scene(loginPane);
//        stage.setTitle("Login");
//        stage.setScene(scene2);
//        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
