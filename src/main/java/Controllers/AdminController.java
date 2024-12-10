package Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.projekt.Session;

public class AdminController {

    @FXML
    private Button odhlasitSaButton;

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

    @FXML
    void odhlasMaButton(ActionEvent event) {
        try {
            // 1. Vyčistenie session (odhlásenie používateľa)
            Session.aktualnyRybarId = 0;

            System.out.println("Používateľ bol úspešne odhlásený.");

            // 2. Načítanie login okna
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/LoginController.fxml"));
            Parent root = loader.load();

            // 3. Vytvorenie novej scény pre prihlasovacie okno
            Stage loginStage = new Stage();
            loginStage.setScene(new Scene(root));
            loginStage.setTitle("Prihlásenie");
            loginStage.show();

            // 4. Zavretie aktuálneho okna
            Stage currentStage = (Stage) odhlasitSaButton.getScene().getWindow();
            currentStage.close();
        } catch (Exception e) {
            e.printStackTrace(); // Výpis chyby, ak sa niečo nepodarí
        }
    }

}
