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
import org.projekt.Rybar;
import org.projekt.RybarDAO;
import org.projekt.Factory;

import java.io.IOException;
import java.sql.*;
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
    private ListView<Rybar> rybarListView;

    @FXML
    private Button pridatRybaraButton;

    @FXML
    private TextField priezviskoTextField;

    @FXML
    private TextField statnaPrislusnotTextField;

    @FXML
    private TextField IDTextField;

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

            if (jeEmailPouzity(email)) {
                zobrazAlert("Chyba registrácie", "Email už je použitý!", "Zadajte iný email.");
                return;
            }

            String hashedHeslo = BCrypt.hashpw(heslo, BCrypt.gensalt());

            Rybar rybar = new Rybar(meno, priezvisko, datumNarodenia, adresa,
                    statnaPrislusnost, email, obcianskyPreukaz, pridanyDoEvidencie, odhlasenyZEvidencie);

            rybarDAO.save(rybar);

            try (Connection connection = DriverManager.getConnection("jdbc:sqlite:bigbass.db")) {
                insertUser(connection, meno, priezvisko, adresa, obcianskyPreukaz, statnaPrislusnost,
                        datumNarodenia, pridanyDoEvidencie, odhlasenyZEvidencie, email, hashedHeslo);
                System.out.println("Používateľ bol úspešne pridaný do databázy.");
            } catch (SQLException e) {
                System.err.println("Chyba pri vkladaní používateľa do databázy: " + e.getMessage());
            }

            rybarListView.getItems().clear();
            rybarListView.getItems().addAll(rybarDAO.getAll());

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/LoginController.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root);

            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();

            stage.setTitle("Prihlásenie");
            stage.getIcons().add(new javafx.scene.image.Image(Objects.requireNonNull(getClass().getResourceAsStream("/icons/alfonso.png"))));
            stage.setScene(scene);
            stage.show();

        } catch (NumberFormatException e) {
            System.out.println("ID musí byť číslo!");
        } catch (IllegalArgumentException e) {
            System.out.println("Chyba: " + e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private void insertUser(Connection connection, String meno, String priezvisko, String adresa,
                            String obcianskyPreukaz, String statnaPrislusnost, LocalDate datumNarodenia,
                            LocalDate pridanyDoEvidencie, LocalDate odhlasenyZEvidencie, String email, String heslo) throws SQLException {
        String insertQuery = "INSERT INTO rybar (meno, priezvisko, adresa, cislo_obcianskeho_preukazu, statna_prislusnost, datum_narodenia, pridany_do_evidencie, odhlaseny_z_evidencie, email, heslo) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(insertQuery)) {
            statement.setString(1, meno);
            statement.setString(2, priezvisko);
            statement.setString(3, adresa);
            statement.setString(4, obcianskyPreukaz);
            statement.setString(5, statnaPrislusnost);
            statement.setString(6, datumNarodenia.toString());
            statement.setString(7, pridanyDoEvidencie.toString());
            statement.setString(8, odhlasenyZEvidencie != null ? odhlasenyZEvidencie.toString() : null);
            statement.setString(9, email);
            statement.setString(10, heslo);

            statement.executeUpdate();
        }
    }

    private boolean jeEmailPouzity(String email) {
        String sql = "SELECT COUNT(*) AS pocet FROM rybar WHERE email = ?";
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:bigbass.db");
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next() && rs.getInt("pocet") > 0) {
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void zobrazAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

}
