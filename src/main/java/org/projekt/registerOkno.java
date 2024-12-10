package org.projekt;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.Objects;

public class registerOkno extends Application {

    double x,y = 0;

    public void start(Stage stage) throws Exception {

       // Parent ulovokPane = FXMLLoader.load(getClass().getResource("/UlovkyController.fxml"));
       Parent loginPane = FXMLLoader.load(getClass().getResource("/LoginController.fxml"));
       //Parent bigBass = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/BigBassController.fxml")));


//  //      Scene scene1 = new Scene(ulovokPane);
//        stage.setTitle("Ulovky");
//        stage.setScene(scene1);
//        stage.show();

        Scene scene2 = new Scene(loginPane);
        stage.setTitle("Login");
        stage.setScene(scene2);
        stage.show();

//        Scene sc = new Scene(bigBass);
//        stage.setTitle("Big Bass");
//        stage.initStyle(StageStyle.UNDECORATED);
//        bigBass.setOnMousePressed(evt ->{
//            x = evt.getSceneX();
//            y = evt.getSceneY();
//                });
//        bigBass.setOnMouseDragged(evt ->{
//            stage.setX(evt.getScreenX() - x);
//            stage.setY(evt.getScreenY() - y);
//        });
//        stage.setScene(sc);
//        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
