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
import java.util.Objects;

public class LoginController {

    @FXML
    private TextField EmailTextField;

    @FXML
    private Button LoginButton;

    @FXML
    private PasswordField PasswordTextField;

    @FXML
    private Button RegisterButton;

    @FXML
    void zaregistrujSaButton(ActionEvent event) {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/RegisterController.fxml"));
            Parent root = loader.load();


            Scene scene = new Scene(root);


            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();

            stage.setTitle("Registrácia");
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void prihlasSaButton(ActionEvent event) {

        String email = EmailTextField.getText();
        String heslo = PasswordTextField.getText();

        if ("admin".equals(email) && "admin".equals(heslo)) {

            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/AdminController.fxml"));
                Parent root = loader.load();
                Scene scene = new Scene(root);

                scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/css/styling.css")).toExternalForm());

                Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
                stage.getIcons().add(new javafx.scene.image.Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/florida4bass.jpg"))));
                stage.setTitle("Admin");
                stage.setScene(scene);
                stage.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (overitPouzivatela(email, heslo)) {

            Session.aktualnyRybarId = getUserIdByEmail(email);
            System.out.println("Prihlásený používateľ ID: " + Session.aktualnyRybarId + ", email: " + email); // Log ID a email
            try {

                FXMLLoader loader = new FXMLLoader(getClass().getResource("/BigBassController.fxml"));
                Parent root = loader.load();
                Scene scene = new Scene(root);

                scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/css/styling.css")).toExternalForm());

                Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
                stage.setTitle("Big Bass");


                stage.setScene(scene);
                stage.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Chyba pri prihlasovaní");
            alert.setHeaderText("Nesprávny email alebo heslo");
            alert.setContentText("Skontrolujte svoje prihlasovacie údaje a skúste to znova.");
            alert.showAndWait();
        }
    }


    private boolean overitPouzivatela(String email, String heslo) {

        String sql = "SELECT heslo FROM rybar WHERE email = ?";
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:bigbass.db"); // Pripojenie k databáze
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {

                    String ulozeneHeslo = rs.getString("heslo");

                    return BCrypt.checkpw(heslo, ulozeneHeslo);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private int getUserIdByEmail(String email) {
        String sql = "SELECT id_rybara FROM rybar WHERE email = ?";
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:bigbass.db");
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int id = rs.getInt("id_rybara");
                    System.out.println("Našiel som ID: " + id + " pre email: " + email);
                    return id;
                } else {
                    System.out.println("Email: " + email + " nebol nájdený v databáze.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }


}
