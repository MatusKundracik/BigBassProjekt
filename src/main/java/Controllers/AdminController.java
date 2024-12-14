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

import java.util.Objects;

public class AdminController {

    @FXML
    private Button odhlasitSaButton;

    @FXML
    private StackPane contentStackPane;

    @FXML
    private Button kontrolaButton;

    @FXML
    private Button povolenieButton;

    @FXML
    private Button upravitReviryButton;



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
    void skontrolujUlovokButton(ActionEvent event) {
        nahradObsah(("/KontrolaController.fxml"));
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

            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlSubor));
            Parent obsah = loader.load();

            contentStackPane.getChildren().clear();
            contentStackPane.getChildren().add(obsah);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
