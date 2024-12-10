package Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.projekt.Session;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class BigBassController {

    @FXML
    private VBox buttonPanelVBox;

    @FXML
    private StackPane contentStackPane;

    @FXML
    private Button odhlasitSaButton;

    @FXML
    private Label prihlasenyPouzivatelLabel;

    @FXML
    private Button profilButton;

    @FXML
    private Button ulovkyButton;

    @FXML
    public void initialize() {
        // Načítanie údajov o aktuálne prihlásenom používateľovi zo Session
        int aktualnyRybarId = Session.aktualnyRybarId;

        if (aktualnyRybarId > 0) {
            // Načítaj meno používateľa podľa ID
            String menoPouzivatela = getRybarNameById(aktualnyRybarId);
            if (menoPouzivatela != null) {
                prihlasenyPouzivatelLabel.setText("Prihlásený používateľ: " + menoPouzivatela);
            } else {
                prihlasenyPouzivatelLabel.setText("Neznámy používateľ (ID: " + aktualnyRybarId + ")");
            }
        } else {
            prihlasenyPouzivatelLabel.setText("Používateľ nie je prihlásený.");
        }
        //nahradObsah("/Profil.fxml");
    }

    private String getRybarNameById(int idRybara) {
        String sql = "SELECT meno, priezvisko FROM rybar WHERE id_rybara = ?";
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:bigbass.db");
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idRybara);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String meno = rs.getString("meno");
                    String priezvisko = rs.getString("priezvisko");
                    return meno + " " + priezvisko; // Skombinuj meno a priezvisko
                }
            }
        } catch (Exception e) {
            e.printStackTrace(); // Vypíše chybu pri práci s databázou
        }
        return null; // Ak sa meno nepodarilo načítať
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

    @FXML
    void zobrazProfilButton(ActionEvent event) {
        nahradObsah("/ProfilController.fxml");
    }

    @FXML
    void zobrazUlovkyButton(ActionEvent event) {
        nahradObsah("/UlovkyController.fxml");
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
