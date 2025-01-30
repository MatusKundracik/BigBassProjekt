package Controllers;

import Rybar.RybarDAO;
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
import org.projekt.Factory;
import org.projekt.Session;

import java.sql.*;
import java.util.Objects;

public class LoginController {

    Connection connection;

    {
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:bigbass.db");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private RybarDAO rybarDAO = Factory.INSTANCE.getRybarDAO();

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
        } else if (rybarDAO.overitPouzivatela(email, heslo)) {

            Session.aktualnyRybarId = rybarDAO.getUserIdByEmail(email);
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


}
