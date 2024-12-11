package Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.projekt.*;

import java.sql.*;
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
    private ComboBox<String> nazovReviruComboBox;

    @FXML
    void addUlovokAction(ActionEvent event) {
        try {
            // Získanie údajov z formulára
            LocalDate datumUlovok = datumUlovkuDatePicker.getValue();
            String cisloReviru = nazovReviruComboBox.getValue();
            String druhRyby = druhRybyTextField.getText();
            double dlzkaVcm = Double.parseDouble(dlzkaVcmTextField.getText());
            double hmotnostVkg = Double.parseDouble(hmotnostVkgTextField.getText());


            // Vytvorenie objektu Ulovok
            Ulovok ulovok = new Ulovok(datumUlovok, cisloReviru, druhRyby, dlzkaVcm, hmotnostVkg);

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
        String insertQuery = "INSERT INTO ulovok (datum, cislo_reviru, druh_ryby, dlzka_v_cm, hmotnost_v_kg," +
                "povolenie_id_povolenie, povolenie_rybar_id_rybara, revir_id_revira) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(insertQuery)) {
            // Nastavenie parametrov pre PreparedStatement
            statement.setString(1, ulovok.getDatumUlovku().toString());
            statement.setString(2, ulovok.getCisloReviru());
            statement.setString(3, ulovok.getDruhRyby());
            statement.setDouble(4, ulovok.getDlzkaVcm());
            statement.setDouble(5, ulovok.getHmotnostVkg());
            statement.setInt(6, 1);
            statement.setInt(7, Session.aktualnyRybarId);
            statement.setInt(8, 1);

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Chyba pri vkladaní úlovku do databázy", e);
        }
    }

    @FXML
    private void naplnNazvyReviruDoChoiceBox() {
        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:bigbass.db")) {
            String selectQuery = "SELECT nazov FROM revir";
            try (PreparedStatement statement = connection.prepareStatement(selectQuery);
                 ResultSet resultSet = statement.executeQuery()) {

                // Clear the existing items in the ChoiceBox
                nazovReviruComboBox.getItems().clear();

                // Add each "nazov" from the database to the ChoiceBox
                while (resultSet.next()) {
                    String nazovReviru = resultSet.getString("nazov");
                    nazovReviruComboBox.getItems().add(nazovReviru);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Chyba při načítání názvů revírů.", e);
        }
    }

    ;

    private void nacitajUlovkyPreAktualnehoPouzivatela() {
        ulovky.clear(); // Vyčistenie lokálneho zoznamu
        ulovokListView.getItems().clear(); // Vyčistenie zobrazenia

        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:bigbass.db")) {
            String selectQuery = "SELECT * FROM ulovok WHERE povolenie_rybar_id_rybara = ?";
            try (PreparedStatement statement = connection.prepareStatement(selectQuery)) {
                statement.setInt(1, Session.aktualnyRybarId); // ID aktuálne prihláseného používateľa

                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        // Načítanie údajov z databázy
                        LocalDate datum = LocalDate.parse(resultSet.getString("datum"));
                        String cisloReviru = resultSet.getString("cislo_reviru");
                        String druhRyby = resultSet.getString("druh_ryby");
                        double dlzkaVcm = resultSet.getDouble("dlzka_v_cm");
                        double hmotnostVkg = resultSet.getDouble("hmotnost_v_kg");
                        int kontrola = resultSet.getInt("kontrola");

                        // Vytvorenie objektu Ulovok
                        Ulovok ulovok = new Ulovok(datum, cisloReviru, druhRyby, dlzkaVcm, hmotnostVkg);

                        // Pridanie do zoznamu a ListView
                        ulovky.add(ulovok);
                        ulovokListView.getItems().add(ulovok);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void initialize() {
        this.ulovokListView.getItems().addAll(this.ulovky);
        nacitajUlovkyPreAktualnehoPouzivatela();
        naplnNazvyReviruDoChoiceBox();
    }

}