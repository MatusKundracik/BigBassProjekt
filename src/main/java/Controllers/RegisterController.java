package Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.mindrot.jbcrypt.BCrypt;
import org.projekt.Factory;
import Rybar.Rybar;
import Rybar.RybarDAO;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Objects;

public class RegisterController {
    private RybarDAO rybarDAO = Factory.INSTANCE.getRybarDAO();

    @FXML
    private TextField adresaTextField;
    @FXML
    private DatePicker datumNarodeniaDatePicker;
    @FXML
    private TextField emailTextField;
    @FXML
    private TextField hesloTextField;
    @FXML
    private TextField menoTextField;
    @FXML
    private TextField obcianskyPreukazTextField;
    @FXML
    private TextField priezviskoTextField;
    @FXML
    private TextField statnaPrislusnotTextField;

    @FXML
    void addRybarAction(ActionEvent event) {
        try {
            String meno = menoTextField.getText();
            String priezvisko = priezviskoTextField.getText();
            String adresa = adresaTextField.getText();
            String obcianskyPreukaz = obcianskyPreukazTextField.getText();
            String statnaPrislusnost = statnaPrislusnotTextField.getText();
            String email = emailTextField.getText();
            String heslo = hesloTextField.getText();
            LocalDate datumNarodenia = datumNarodeniaDatePicker.getValue();
            LocalDate pridanyDoEvidencie = LocalDate.now();
            LocalDate odhlasenyZEvidencie = null;

            if (rybarDAO.jeEmailPouzity(email)) {
                zobrazAlert("Chyba registrácie", "Email už je použitý!", "Zadajte iný email.");
                return;
            }
            String hashedHeslo = BCrypt.hashpw(heslo, BCrypt.gensalt());

            Rybar rybar = new Rybar(meno, priezvisko, datumNarodenia, adresa,
                    statnaPrislusnost, email, obcianskyPreukaz, pridanyDoEvidencie, odhlasenyZEvidencie, heslo);

            rybarDAO.save(rybar);

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/LoginController.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setTitle("Prihlásenie");
            stage.getIcons().add(new javafx.scene.image.Image(Objects.requireNonNull(getClass().getResourceAsStream("/icons/alfonso.png"))));
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void zobrazAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}