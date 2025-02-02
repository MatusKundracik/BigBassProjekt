package Controllers;

import Revir.Revir;
import Revir.RevirDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
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
    private ListView<Revir> revirListView;

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
                showAlert("Chyba", "Názov revíru musí byť zadaný!");
                return;
            }

            kaprove = kaproveCheckBox.isSelected();
            lipnove = lipnoveCheckBox.isSelected();
            pstruhove = pstruhoveCheckBox.isSelected();

            Revir revir = new Revir(nazov, lokalita, popis, kaprove, lipnove, pstruhove);
            revirDAO.insertRevir(revir);
            revire.add(revir);
            revirListView.getItems().add(revir);
        } catch (IllegalArgumentException e) {
            showAlert("Chyba", e.getMessage());
        } catch (SQLException e) {
            showAlert("Chyba", "Chyba pri ukladaní revíru: " + e.getMessage());
        }
    }

    // Pomocná metóda na zobrazenie Alertu
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
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

    private void nacitajRevireZDatabazy() {
        try {
            revire = revirDAO.getAllReviry();
            revirListView.getItems().setAll(revire);
        } catch (SQLException e) {
            revirListView.getItems().add(new Revir("Chyba", "", "Názov revíru musí byť zadaný!", false, false, false));

        }
    }

    @FXML
    void initialize() {
        nacitajRevireZDatabazy();
    }


}
