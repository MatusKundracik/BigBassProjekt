package Controllers;

import Revir.Revir;
import Revir.RevirDAO;
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
    private ListView<String> revirListView;

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
                revirDAO.insertRevir(connection, revir);
                String message = "Revír \"" + nazov + "\" bol úspešne pridaný.";
                revirListView.getItems().add(message);
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


}
