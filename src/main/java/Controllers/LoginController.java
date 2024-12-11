package Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.mindrot.jbcrypt.BCrypt;
import org.projekt.Session;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LoginController {

    @FXML
    private TextField EmailTextField; // Textové pole na zadanie emailu

    @FXML
    private Button LoginButton; // Tlačidlo na prihlásenie

    @FXML
    private PasswordField PasswordTextField; // Textové pole na zadanie hesla

    @FXML
    private Button RegisterButton; // Tlačidlo na prechod na registračnú obrazovku

    // Metóda na prechod na registračnú obrazovku
    @FXML
    void zaregistrujSaButton(ActionEvent event) {
        try {
            // Načíta FXML súbor pre registračnú obrazovku
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/RegisterController.fxml"));
            Parent root = loader.load();

            // Vytvorí novú scénu
            Scene scene = new Scene(root);

            // Získaj aktuálne okno (Stage)
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();

            // Nastav novú scénu
            stage.setTitle("Registrácia");
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace(); // Vypíše chybu, ak sa FXML nepodarí načítať
        }
    }

    // Metóda na prihlásenie používateľa
    @FXML
    void prihlasSaButton(ActionEvent event) {
        // Získa údaje z textových polí
        String email = EmailTextField.getText();
        String heslo = PasswordTextField.getText();

        // Skontroluje, či je email a heslo "admin"
        if ("admin".equals(email) && "admin".equals(heslo)) {
            // Ak je email a heslo "admin", načíta sa AdminController
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/AdminController.fxml"));
                Parent root = loader.load();
                Scene scene = new Scene(root);
                Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
                stage.setTitle("Admin");
                stage.setScene(scene);
                stage.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (overitPouzivatela(email, heslo)) {
            // Ak sa používateľ nájde v databáze, načíta sa hlavná obrazovka
            Session.aktualnyRybarId = getUserIdByEmail(email);
            System.out.println("Prihlásený používateľ ID: " + Session.aktualnyRybarId + ", email: " + email); // Log ID a email
            try {

                FXMLLoader loader = new FXMLLoader(getClass().getResource("/BigBassController.fxml"));
                Parent root = loader.load();
                Scene scene = new Scene(root);
                Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
                stage.setTitle("Big Bass");
                stage.setScene(scene);
                stage.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            // Ak sú prihlasovacie údaje nesprávne, zobrazí sa chybové hlásenie
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Chyba pri prihlasovaní");
            alert.setHeaderText("Nesprávny email alebo heslo");
            alert.setContentText("Skontrolujte svoje prihlasovacie údaje a skúste to znova.");
            alert.showAndWait();
        }
    }

    // Metóda na overenie používateľa v databáze
    private boolean overitPouzivatela(String email, String heslo) {
        // SQL dotaz na získanie hesla pre daný email
        String sql = "SELECT heslo FROM rybar WHERE email = ?";
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:bigbass.db"); // Pripojenie k databáze
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email); // Nastavenie emailu do SQL dotazu

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    // Načítanie šifrovaného hesla z databázy
                    String ulozeneHeslo = rs.getString("heslo");
                    // Porovnanie zadaného hesla s uloženým heslom pomocou BCrypt
                    return BCrypt.checkpw(heslo, ulozeneHeslo);
                }
            }
        } catch (Exception e) {
            e.printStackTrace(); // Vypíše chybu pri práci s databázou
        }
        return false; // Ak sa používateľ nenašiel alebo došlo k chybe
    }

    private int getUserIdByEmail(String email) {
        String sql = "SELECT id_rybara FROM rybar WHERE email = ?";
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:bigbass.db");
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email); // Nastavenie emailu
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int id = rs.getInt("id_rybara");
                    System.out.println("Našiel som ID: " + id + " pre email: " + email);
                    return id; // Vráť nájdené ID
                } else {
                    System.out.println("Email: " + email + " nebol nájdený v databáze.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace(); // Vypíše chybu pri práci s databázou
        }
        return -1; // Ak ID neexistuje alebo došlo k chybe
    }


}
