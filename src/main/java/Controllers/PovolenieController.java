package Controllers;

import Povolenie.Povolenie;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.projekt.*;
import Povolenie.PovolenieDAO;
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
                povolenieDAO.insertPovolenie(povolenie);

                String message = povolenieDAO.generatePovolenieMessage(rybarID,kaprove,lipnove,pstruhove);

                povolenieListVIew.getItems().add(message.toString());
            } catch (SQLException e) {
                e.printStackTrace();
                povolenieListVIew.getItems().add("Chyba pri ukladaní povolenia: " + e.getMessage());
            }

        } catch (NumberFormatException e) {
            povolenieListVIew.getItems().add("Chyba: ID rybára musí byť číslo.");
        }
    }


}
