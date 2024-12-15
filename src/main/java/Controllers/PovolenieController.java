package Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
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
    private ListView<String> povolenieListVIew; // Zmena na ListView<String>

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
            if (platnostOd == null) {
                povolenieListVIew.getItems().add("Chyba: Zvoľte platnosť od dátum.");
                return;
            }

            LocalDate platnostDo = platnostOd.plusYears(1);
            kaprove = kaproveCheckBox.isSelected();
            lipnove = LipnoveCheckBox.isSelected();
            pstruhove = pstruhoveCheckBox.isSelected();

            String rybarIdText = idRybaraTextFIeld.getText();
            if (rybarIdText == null || rybarIdText.isEmpty()) {
                povolenieListVIew.getItems().add("Chyba: Zadajte ID rybára.");
                return;
            }

            int rybarID = Integer.parseInt(rybarIdText);

            Povolenie povolenie = new Povolenie(platnostOd, platnostDo, kaprove, lipnove, pstruhove, rybarID);

            this.povolenia.add(povolenie);

            try (Connection connection = DriverManager.getConnection("jdbc:sqlite:bigbass.db")) {
                insertPovolenie(connection, povolenie);

                // Vytvorenie správy na základe zvolených typov povolení
                StringBuilder message = new StringBuilder("Rybárovi " + rybarID + " bolo pridané ");
                List<String> typyPovolenia = new ArrayList<>();
                if (kaprove) typyPovolenia.add("kaprové");
                if (lipnove) typyPovolenia.add("lipňové");
                if (pstruhove) typyPovolenia.add("pstruhové");

                if (typyPovolenia.isEmpty()) {
                    message.append("žiadne povolenie.");
                } else {
                    message.append(String.join(", ", typyPovolenia)).append(" povolenie.");
                }

                povolenieListVIew.getItems().add(message.toString());
            } catch (SQLException e) {
                e.printStackTrace();
                povolenieListVIew.getItems().add("Chyba pri ukladaní povolenia: " + e.getMessage());
            }

        } catch (NumberFormatException e) {
            povolenieListVIew.getItems().add("Chyba: ID rybára musí byť číslo.");
        }
    }

    private void insertPovolenie(Connection connection, Povolenie povolenie) throws SQLException {
        String insertQuery = "INSERT INTO povolenie (platnost_od, platnost_do, pstruhove, lipňove, kaprové, rybar_id_rybara) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(insertQuery)) {
            statement.setString(1, povolenie.getPlatnostOd().toString());
            statement.setString(2, povolenie.getPlatnostDo().toString());
            statement.setInt(3, povolenie.isPstruhove() ? 1 : 0);
            statement.setInt(4, povolenie.isLipnove() ? 1 : 0);
            statement.setInt(5, povolenie.isKaprove() ? 1 : 0);
            statement.setInt(6, (int) povolenie.getRybarIdRybara());

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Chyba pri vkladaní povolenia do databázy", e);
        }
    }
}
