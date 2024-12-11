package Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.projekt.Session;

public class AdminController {

    @FXML
    private Button odhlasitSaButton;

    @FXML
    private StackPane contentStackPane;

    @FXML
    private Button povolenieButton;

    @FXML
    private Button upravitReviryButton;

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

    @FXML
    void upravPovolenieButton(ActionEvent event) {
        nahradObsah("/PovolenieController.fxml");
    }

    @FXML
    void upravRevirButton(ActionEvent event) {
        nahradObsah("/RevirController.fxml");
    }

    private void nahradObsah(String fxmlSubor) {
        try {
            // Načítaj nový obsah z FXML súboru
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlSubor));
            Parent obsah = loader.load();

            // Vymaž starý obsah a nahraď ho novým v StackPane
            contentStackPane.getChildren().clear();
            contentStackPane.getChildren().add(obsah);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
