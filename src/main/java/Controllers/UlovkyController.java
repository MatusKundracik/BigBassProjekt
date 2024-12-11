package Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.projekt.*;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UlovkyController {

    private UlovokDAO ulovokDAO = Factory.INSTANCE.getUlovokDAO();

    private List<Ulovok> ulovky = new ArrayList<>();

    private Map<String, Integer> revirMap = new HashMap<>();
    private int idRevir;
    private int idPovolenie;
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

            LocalDate datumUlovok = datumUlovkuDatePicker.getValue();
            String cisloReviru = nazovReviruComboBox.getValue();
            idRevir = revirMap.get(cisloReviru);
            String druhRyby = druhRybyTextField.getText();
            double dlzkaVcm = Double.parseDouble(dlzkaVcmTextField.getText());
            double hmotnostVkg = Double.parseDouble(hmotnostVkgTextField.getText());


            Ulovok ulovok = new Ulovok(datumUlovok, cisloReviru, druhRyby, dlzkaVcm, hmotnostVkg);


            this.ulovky.add(ulovok);
            ulovokListView.getItems().add(ulovok);


            try (Connection connection = DriverManager.getConnection("jdbc:sqlite:bigbass.db")) {
                insertUlovok(connection, ulovok);
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

            statement.setString(1, ulovok.getDatumUlovku().toString());
            statement.setString(2, ulovok.getCisloReviru());
            statement.setString(3, ulovok.getDruhRyby());
            statement.setDouble(4, ulovok.getDlzkaVcm());
            statement.setDouble(5, ulovok.getHmotnostVkg());
            statement.setInt(6, idPovolenie);
            statement.setInt(7, Session.aktualnyRybarId);
            statement.setInt(8, idRevir);

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Chyba pri vkladaní úlovku do databázy", e);
        }
    }

    @FXML
    private void naplnNazvyReviruDoChoiceBox() {
        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:bigbass.db")) {
            String selectQuery =  "SELECT r.nazov FROM revir r " +
                    "JOIN povolenie p ON (" +
                    "    (p.kaprové = 1 AND r.kaprove = 1) OR " +
                    "    (p.pstruhove = 1 AND r.pstruhove = 1) OR " +
                    "    (p.lipňove = 1 AND r.lipnove = 1)" +
                    ") " +
                    "WHERE p.rybar_id_rybara = ?";
            try (PreparedStatement statement = connection.prepareStatement(selectQuery)) {
                statement.setInt(1, Session.aktualnyRybarId);

                try (ResultSet resultSet = statement.executeQuery()) {
                    // Clear the existing items in the ChoiceBox
                    nazovReviruComboBox.getItems().clear();

                    // Add each "nazov" from the database to the ChoiceBox
                    while (resultSet.next()) {
                        String nazovReviru = resultSet.getString("nazov");
                        nazovReviruComboBox.getItems().add(nazovReviru);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Chyba při načítání názvů revírů.", e);
        }
    }


    ;

    private void nacitajUlovkyPreAktualnehoPouzivatela() {
        ulovky.clear();
        ulovokListView.getItems().clear();

        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:bigbass.db")) {
            String selectQuery = "SELECT * FROM ulovok WHERE povolenie_rybar_id_rybara = ?";
            try (PreparedStatement statement = connection.prepareStatement(selectQuery)) {
                statement.setInt(1, Session.aktualnyRybarId);

                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {

                        LocalDate datum = LocalDate.parse(resultSet.getString("datum"));
                        String cisloReviru = resultSet.getString("cislo_reviru");
                        String druhRyby = resultSet.getString("druh_ryby");
                        double dlzkaVcm = resultSet.getDouble("dlzka_v_cm");
                        double hmotnostVkg = resultSet.getDouble("hmotnost_v_kg");

                        Ulovok ulovok = new Ulovok(datum, cisloReviru, druhRyby, dlzkaVcm, hmotnostVkg);


                        ulovky.add(ulovok);
                        ulovokListView.getItems().add(ulovok);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void idPovoleniaPodlaRybaraID() {
        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:bigbass.db")) {
            String selectQuery = "SELECT id_povolenie FROM povolenie WHERE rybar_id_rybara = ?";
            try (PreparedStatement statement = connection.prepareStatement(selectQuery)) {
                statement.setInt(1, Session.aktualnyRybarId);

                try (ResultSet resultSet = statement.executeQuery()) {
                    // Predpokladáme, že rybár má iba jedno povolenie
                    if (resultSet.next()) {
                        this.idPovolenie = resultSet.getInt("id_povolenie");
                        System.out.println("ID povolenia pre aktuálneho rybára: " + idPovolenie);
                    } else {
                        System.out.println("Rybár nemá žiadne povolenie.");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Chyba pri načítaní ID povolenia pre rybára.", e);
        }
    }




    @FXML
    void initialize() {
        this.ulovokListView.getItems().addAll(this.ulovky);
        nacitajUlovkyPreAktualnehoPouzivatela();
        naplnNazvyReviruDoChoiceBox();
        idPovoleniaPodlaRybaraID();
    }

}