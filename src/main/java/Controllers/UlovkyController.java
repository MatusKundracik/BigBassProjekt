package Controllers;

import Povolenie.PovolenieDAO;
import Ulovok.Ulovok;
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
import Ulovok.UlovokDAO;

public class UlovkyController {

    private UlovokDAO ulovokDAO = Factory.INSTANCE.getUlovokDAO();
    private PovolenieDAO povolenieDAO = Factory.INSTANCE.getPovolenieDAO();

    private List<Ulovok> ulovky = new ArrayList<>();

    private Map<String, Integer> revirMap = new HashMap<>();
    private int idRevir;

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
    private ComboBox<String> druhRybyComboBox;

    public void nacitajRybyNaSlovensku() {


        druhRybyComboBox.getItems().addAll(
                "Amur biely",
                "Blatňák tmavý",
                "Candát obecný",
                "Candát východný",
                "Cejn perleťový",
                "Cejn siný",
                "Cejn veľký",
                "Cejnek mal",
                "Drsek menší",
                "Jeseter malý",
                "Drsek väčší",
                "Hlavačka mramorovaná",
                "Hlavačkovec Glenov",
                "Hlavatka obecná podunajská",
                "Hlaváč čiernoústý",
                "Hlaváč holokrký",
                "Hlaváč Kesslerov",
                "Hlaváč riečny",
                "Hořavka dúhová",
                "Hrouzek beloplutvý",
                "Hrouzek dlhovúsy",
                "Hrouzek Kesslerov",
                "Hrouzek obecný",
                "Jelec jesenný",
                "Jelec prúdnatý",
                "Jelec tĺšť",
                "Jeseter hviezdnatý",
                "Jeseter ruský",
                "Ježdík dunajský",
                "Ježdík obyčajný",
                "Ježdík žltý)",
                "Kapr obyčajný",
                "Karas obyčajný",
                "Karas striebristý",
                "Koljuška tŕnočiarna",
                "Lipňan podhorný",
                "Mník jednovúsy",
                "Mrenka mramorovaná",
                "Ostriež riečny",
                "Plotica obyčajná",
                "Sumec veľký",
                "Šťuka obyčajná",
                "Tolstolobec pestrofarebný",
                "Úhor riečny",
                "Vranka obyčajná"
        );
    }
    @FXML
    void addUlovokAction(ActionEvent event) {
        try {

            LocalDate datumUlovok = datumUlovkuDatePicker.getValue();
            String cisloReviru = nazovReviruComboBox.getValue();
            idRevir = revirMap.get(cisloReviru);
            String druhRyby = druhRybyComboBox.getValue();
            double dlzkaVcm = Double.parseDouble(dlzkaVcmTextField.getText());
            double hmotnostVkg = Double.parseDouble(hmotnostVkgTextField.getText());
            int kontrola = 0;


            Ulovok ulovok = new Ulovok(datumUlovok, cisloReviru, druhRyby, dlzkaVcm, hmotnostVkg,kontrola);


            this.ulovky.add(ulovok);
            ulovokListView.getItems().add(ulovok);


            try  {
                ulovokDAO.insertUlovok(ulovok);
            } catch (SQLException e) {
                throw new RuntimeException("Chyba pri ukladaní úlovku do databázy", e);
            }

        } catch (RuntimeException e) {
            System.out.println("Chyba pri spracovaní údajov: ");
        }
    }



    @FXML
    private void naplnNazvyReviruDoChoiceBox() {
        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:bigbass.db")) {
            String selectQuery =  "SELECT r.nazov, r.id_revira FROM revir r " +
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
                        int idRevir = resultSet.getInt("id_revira");
                        revirMap.put(nazovReviru, idRevir);
                        nazovReviruComboBox.getItems().add(nazovReviru);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Chyba pri nacitani reviru.", e);
        }
    }




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
                        int kontrola = resultSet.getInt("kontrola");
                        Ulovok ulovok = new Ulovok(datum, cisloReviru, druhRyby, dlzkaVcm, hmotnostVkg, kontrola);


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
        povolenieDAO.idPovoleniaPodlaRybaraID();
        nacitajRybyNaSlovensku();
    }

}