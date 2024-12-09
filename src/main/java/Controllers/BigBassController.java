package Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.projekt.Session;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class BigBassController {

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
