package Controllers;

import Povolenie.Povolenie;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.projekt.*;
import Povolenie.PovolenieDAO;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class PovolenieController {
    private PovolenieDAO povolenieDAO = Factory.INSTANCE.getPovolenieDAO();
    private boolean kaprove = false;
    private boolean lipnove = false;
    private boolean pstruhove = false;

    @FXML
    private CheckBox LipnoveCheckBox;

    @FXML
    private ComboBox<String> idRybaraComboBox;

    @FXML
    private ListView<String> povolenieListVIew;

    @FXML
    private CheckBox kaproveCheckBox;

    @FXML
    private DatePicker platnostOdDatePicker;

    @FXML
    private CheckBox pstruhoveCheckBox;

    @FXML
    private Button zapisPovolenieButton;

    @FXML
    public void initialize() {
        loadEmails();
    }

    private void loadEmails() {
        try {
            List<String> emails = povolenieDAO.getAllEmails();
            idRybaraComboBox.getItems().addAll(emails);
        } catch (Exception e) {
            e.printStackTrace();
            povolenieListVIew.getItems().add("Chyba pri načítaní e-mailov: " + e.getMessage());
        }
    }

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

            String email = idRybaraComboBox.getValue();
            if (email == null || email.isEmpty()) {
                povolenieListVIew.getItems().add("Chyba: Vyberte e-mail rybára.");
                return;
            }

            int rybarID = povolenieDAO.getIdByEmail(email);
            Povolenie povolenie = new Povolenie(platnostOd, platnostDo, kaprove, lipnove, pstruhove, rybarID);

            povolenieDAO.insertPovolenie(povolenie);
            String message = povolenieDAO.generatePovolenieMessage(rybarID, kaprove, lipnove, pstruhove);
            povolenieListVIew.getItems().add(message);
        } catch (Exception e) {
            povolenieListVIew.getItems().add("Chyba pri ukladaní povolenia: " + e.getMessage());
        }
    }
}
