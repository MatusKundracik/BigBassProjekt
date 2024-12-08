package Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class AdminController {

    @FXML
    private Button povolenieButton;

    @FXML
    private Button upravitReviryButton;

    @FXML
    void upravPovolenieButton(ActionEvent event) {
        try {
            // Načítaj FXML pre register okno
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/PovolenieController.fxml"));
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
    void upravRevirButton(ActionEvent event) {
        try {
            // Načítaj FXML pre register okno
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/RevirController.fxml"));
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
