package org.projekt;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class BigBassController {

    @FXML
    private Button profilButton;

    @FXML
    private Button ulovkyButton;

    @FXML
    void zobrazProfilButton(ActionEvent event) {

    }

    @FXML
    void zobrazUlovkyButton(ActionEvent event) {
        try {
            // Načítaj FXML pre register okno
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/UlovkyController.fxml"));
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
