package Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import org.projekt.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PovolenieController {
    private PovolenieDAO povolenieDAO = Factory.INSTANCE.getPovolenieDAO();
    private List<Povolenie> povolenia = new ArrayList<>();

    private boolean kaprove = false;
    private boolean lipnove = false;
    private boolean pstruhove = false;

    @FXML
    private CheckBox LipnoveCheckBox;

    @FXML
    private TextField idRybaraTextFIeld;

    @FXML
    private CheckBox kaproveCheckBox;

    @FXML
    private DatePicker platnostOdDatePicker;

    @FXML
    private CheckBox pstruhoveCheckBox;

    @FXML
    private Button zapisPovolenieButton;

    @FXML
    void addPovolenieButton(ActionEvent event) {
        try {
            LocalDate platnostOd = platnostOdDatePicker.getValue();
            LocalDate platnostDo = platnostOd.plusYears(1);
            kaprove = kaproveCheckBox.isSelected();
            lipnove = LipnoveCheckBox.isSelected();
            pstruhove = pstruhoveCheckBox.isSelected();
            int rybarID = Integer.parseInt(idRybaraTextFIeld.getText());

            // Vytvorenie objektu Ulovok
            Povolenie povolenie = new Povolenie(platnostOd, platnostDo, kaprove, lipnove, pstruhove, rybarID);

            // Pridanie úlovku do zoznamu a ListView
            this.povolenia.add(povolenie);

            try (Connection connection = DriverManager.getConnection("jdbc:sqlite:bigbass.db")) {
                insertPovolenie(connection, povolenie);
            } catch (SQLException e) {
                throw new RuntimeException("Chyba pri ukladaní úlovku do databázy", e);
            }

        } catch (NumberFormatException e) {
            throw new RuntimeException("Chyba pri spracovaní údajov: " + e.getMessage(), e);
        }
    }

    private void insertPovolenie(Connection connection, Povolenie povolenie) throws SQLException {
        // SQL dotaz pre vloženie povolenia
        String insertQuery = "INSERT INTO povolenie (platnost_od, platnost_do, pstruhove, lipňove, kaprové, rybar_id_rybara) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(insertQuery)) {
            // Nastavenie parametrov pre PreparedStatement
            statement.setString(1, povolenie.getPlatnostOd().toString());
            statement.setString(2, povolenie.getPlatnostDo().toString());
            statement.setInt(3, povolenie.isPstruhove() ? 1 : 0);
            statement.setInt(4, povolenie.isLipnove() ? 1 : 0);
            statement.setInt(5, povolenie.isKaprove() ? 1 : 0);
            statement.setInt(6, (int) povolenie.getRybarIdRybara());

            // Vykonanie dotazu
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Chyba pri vkladaní povolenia do databázy", e);
        }
    }


}