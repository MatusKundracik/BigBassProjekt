package Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import org.projekt.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RevirController {
    private RevirDAO revirDAO = Factory.INSTANCE.getRevirDAO();
    private List<Revir> revire = new ArrayList<>();

    private boolean kaprove = false;
    private boolean lipnove = false;
    private boolean pstruhove = false;

    @FXML
    private CheckBox kaproveCheckBox;

    @FXML
    private CheckBox lipnoveCheckBox;

    @FXML
    private ListView<String> revirListView; // Zmena na ListView<String>

    @FXML
    private TextField lokalitaTextField;

    @FXML
    private TextField nazovReviruTextField;

    @FXML
    private TextField popisTextField;

    @FXML
    private CheckBox pstruhoveCheckBox;

    @FXML
    private Button zapisRevirButton;

    @FXML
    void addRevirButton(ActionEvent event) {
        try {
            String nazov = nazovReviruTextField.getText();
            String lokalita = lokalitaTextField.getText();
            String popis = popisTextField.getText();

            if (nazov == null || nazov.isEmpty()) {
                revirListView.getItems().add("Názov revíru musí byť zadaný!");
                return;
            }

            kaprove = kaproveCheckBox.isSelected();
            lipnove = lipnoveCheckBox.isSelected();
            pstruhove = pstruhoveCheckBox.isSelected();

            Revir revir = new Revir(nazov, lokalita, popis, kaprove, lipnove, pstruhove);

            this.revire.add(revir);

            try (Connection connection = DriverManager.getConnection("jdbc:sqlite:bigbass.db")) {
                insertRevir(connection, revir);
                String message = "Revír \"" + nazov + "\" bol úspešne pridaný.";
                revirListView.getItems().add(message); // Pridanie správy do ListView
            } catch (SQLException e) {
                e.printStackTrace();
                revirListView.getItems().add("Chyba pri ukladaní revíru: " + e.getMessage());
            }

        } catch (NumberFormatException e) {
            revirListView.getItems().add("Chyba pri spracovaní údajov: " + e.getMessage());
        }
    }

    @FXML
    void kaproveAction(ActionEvent event) {
        kaprove = kaproveCheckBox.isSelected();
    }

    @FXML
    void lipnoveAction(ActionEvent event) {
        lipnove = lipnoveCheckBox.isSelected();
    }

    @FXML
    void pstruhoveAction(ActionEvent event) {
        pstruhove = pstruhoveCheckBox.isSelected();
    }

    private void insertRevir(Connection connection, Revir revir) throws SQLException {
        String insertQuery = "INSERT INTO revir (nazov, lokalita, popis, kaprove, lipnove, pstruhove) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(insertQuery)) {
            statement.setString(1, revir.getNazov());
            statement.setString(2, revir.getLokalita());
            statement.setString(3, revir.getPopis());

            statement.setInt(4, revir.isKaprove() ? 1 : 0);
            statement.setInt(5, revir.isLipnove() ? 1 : 0);
            statement.setInt(6, revir.isPstruhove() ? 1 : 0);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Chyba pri vkladaní revíru do databázy", e);
        }
    }
}
