package Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import org.mindrot.jbcrypt.BCrypt;
import org.projekt.Factory;
import org.projekt.Rybar;
import org.projekt.RybarDAO;
import org.projekt.Factory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;

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
            LocalDate pridanyDoEvidencie = LocalDate.now(); // Dátum pridania
            LocalDate odhlasenyZEvidencie = null; // Predpokladáme, že nového používateľa neodhlasujeme

            // Šifrovanie hesla pomocou Bcrypt
            String hashedHeslo = BCrypt.hashpw(heslo, BCrypt.gensalt());

            Rybar rybar = new Rybar(meno, priezvisko, datumNarodenia, adresa,
                    statnaPrislusnost, email, obcianskyPreukaz, pridanyDoEvidencie, odhlasenyZEvidencie);

            rybarDAO.save(rybar);

            // Vložíme používateľa priamo cez JDBC
            try (Connection connection = DriverManager.getConnection("jdbc:sqlite:bigbass.db")) {
                insertUser(connection, meno, priezvisko, adresa, obcianskyPreukaz, statnaPrislusnost,
                        datumNarodenia, pridanyDoEvidencie, odhlasenyZEvidencie, email, hashedHeslo);
                System.out.println("Používateľ bol úspešne pridaný do databázy.");
            } catch (SQLException e) {
                System.err.println("Chyba pri vkladaní používateľa do databázy: " + e.getMessage());
            }

            rybarListView.getItems().clear();
            rybarListView.getItems().addAll(rybarDAO.getAll());
        } catch (NumberFormatException e) {
            System.out.println("ID musí byť číslo!");
        } catch (IllegalArgumentException e) {
            System.out.println("Chyba: " + e.getMessage());
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

}
