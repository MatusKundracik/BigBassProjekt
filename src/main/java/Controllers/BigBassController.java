package Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.projekt.Session;
import javafx.scene.image.ImageView;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Objects;

public class BigBassController {

    @FXML
    private VBox buttonPanelVBox;

    @FXML
    private StackPane contentStackPane;

    @FXML
    private ImageView imageLabel;

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
        int aktualnyRybarId = Session.aktualnyRybarId;
        Image image = new Image("/images/bass.png");
        imageLabel.setImage(image);

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
                    return meno + " " + priezvisko;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @FXML
    void odhlasMaButton(ActionEvent event) {
        try {

            Session.aktualnyRybarId = 0;

            System.out.println("Používateľ bol úspešne odhlásený.");


            FXMLLoader loader = new FXMLLoader(getClass().getResource("/LoginController.fxml"));
            Parent root = loader.load();


            Stage loginStage = new Stage();
            loginStage.setScene(new Scene(root));
            loginStage.getIcons().add(new javafx.scene.image.Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/florida4bass.jpg"))));
            loginStage.setTitle("Prihlásenie");
            loginStage.show();

            Stage currentStage = (Stage) odhlasitSaButton.getScene().getWindow();
            currentStage.close();
        } catch (Exception e) {
            e.printStackTrace();
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

            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlSubor));
            Parent obsah = loader.load();

            contentStackPane.getChildren().clear();
            contentStackPane.getChildren().add(obsah);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
