package Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import org.projekt.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class UlovkyController {

    private UlovokDAO ulovokDAO = Factory.INSTANCE.getUlovokDAO();

    private List<Ulovok> ulovky = new ArrayList<>();

    @FXML
    private Label IDulovokLabel;

    @FXML
    private TextField IDulovokTextField;

    @FXML
    private TextField cisloReviruTextField;

    @FXML
    private DatePicker datumUlovkuDatePicker;

    @FXML
    private TextField dlzkaVcmTextField;

    @FXML
    private TextField druhRybyTextField;

    @FXML
    private TextField hmotnostVkgTextField;

    @FXML
    private TextField kontrolaTextField;

    @FXML
    private Button pridajUlovokButton;

    @FXML
    private ListView<Ulovok> ulovokListView;

    @FXML
    void addUlovokAction(ActionEvent event) {
        try {
            // Získanie údajov z formulára
            LocalDate datumUlovok = datumUlovkuDatePicker.getValue();
            int cisloReviru = Integer.parseInt(cisloReviruTextField.getText());
            String druhRyby = druhRybyTextField.getText();
            double dlzkaVcm = Double.parseDouble(dlzkaVcmTextField.getText());  // Dĺžka v cm ako Double
            double hmotnostVkg = Double.parseDouble(hmotnostVkgTextField.getText());  // Hmotnosť v kg ako Double
            int kontrola = Integer.parseInt(kontrolaTextField.getText());

            // Vytvorenie objektu Ulovok
            Ulovok ulovok = new Ulovok(datumUlovok, cisloReviru, druhRyby, dlzkaVcm, hmotnostVkg, kontrola);

            // Pridanie úlovku do zoznamu a ListView
            this.ulovky.add(ulovok);
            ulovokListView.getItems().add(ulovok);

            // Uloženie úlovku do databázy
            try (Connection connection = DriverManager.getConnection("jdbc:sqlite:bigbass.db")) {
                insertUlovok(connection, ulovok); // Zavolanie metódy na uloženie do databázy
            } catch (SQLException e) {
                throw new RuntimeException("Chyba pri ukladaní úlovku do databázy", e);
            }

        } catch (NumberFormatException e) {
            throw new RuntimeException("Chyba pri spracovaní údajov: " + e.getMessage(), e);
        }
    }

    private void insertUlovok(Connection connection, Ulovok ulovok) throws SQLException {
        // SQL dotaz pre vloženie úlovku
        String insertQuery = "INSERT INTO ulovok (datum, cislo_reviru, druh_ryby, dlzka_v_cm, hmotnost_v_kg, kontrola, " +
                "povolenie_id_povolenie, povolenie_rybar_id_rybara, revir_id_revira) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(insertQuery)) {
            // Nastavenie parametrov pre PreparedStatement
            statement.setString(1, ulovok.getDatumUlovku().toString());
            statement.setInt(2, ulovok.getCisloReviru());
            statement.setString(3, ulovok.getDruhRyby());
            statement.setDouble(4, ulovok.getDlzkaVcm());
            statement.setDouble(5, ulovok.getHmotnostVkg());
            statement.setInt(6, ulovok.getKontrola());
            statement.setInt(7, 1);
            statement.setInt(8, Session.aktualnyRybarId);
            statement.setInt(9, 1);

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Chyba pri vkladaní úlovku do databázy", e);
        }
    }

    @FXML
    void initialize() {
        this.ulovokListView.getItems().addAll(this.ulovky);
    }

}