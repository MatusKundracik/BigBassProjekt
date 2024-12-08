package org.projekt;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController {

    @FXML
    private TextField EmailTextField;

    @FXML
    private Button LoginButton;

    @FXML
    private TextField PasswordTextField;

    @FXML
    private Button RegisterButton;

    @FXML
    void zaregistrujSaButton(ActionEvent event) {
        try {
            // Načítaj FXML pre register okno
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/RegisterController.fxml"));
            Parent root = loader.load();

            // Vytvor novú scénu
            Scene scene = new Scene(root);

            // Získaj aktuálne okno (Stage)
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();

            // Nastav novú scénu
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void prihlasSaButton(ActionEvent event) {
        try {
            // Načítaj FXML pre register okno
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/BigBassController.fxml"));
            Parent root = loader.load();

            // Vytvor novú scénu
            Scene scene = new Scene(root);

            // Získaj aktuálne okno (Stage)
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();

            // Nastav novú scénu
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}