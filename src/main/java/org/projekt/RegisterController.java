package org.projekt;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import java.time.LocalDate;

public class RegisterController {
    private RybarDAO rybarDAO = RybarDAOFactory.INSTANCE.getRybarDAO();

    @FXML
    private TextField adresaTextField;

    @FXML
    private DatePicker datumNarodeniaDatePicker;

    @FXML
    private TextField emailTextField;

    @FXML
    private TextField hesloTextField;

    @FXML
    private TextField menoTextField;

    @FXML
    private TextField obcianskyPreukazTextField;

    @FXML
    private ListView<Rybar> rybarListView;

    @FXML
    private Button pridatRybaraButton;

    @FXML
    private TextField priezviskoTextField;

    @FXML
    private TextField statnaPrislusnotTextField;

    @FXML
    private TextField IDTextField;

    @FXML
    void addRybarAction(ActionEvent event) {
        try {

            String meno = menoTextField.getText();
            String priezvisko = priezviskoTextField.getText();
            String adresa = adresaTextField.getText();
            String obcianskyPreukaz = obcianskyPreukazTextField.getText();
            String statnaPrislusnost = statnaPrislusnotTextField.getText();
            LocalDate datumNarodenia = datumNarodeniaDatePicker.getValue();

            Rybar rybar = new Rybar(meno, priezvisko, datumNarodenia, adresa,
                    statnaPrislusnost, obcianskyPreukaz, LocalDate.now(), null);

            rybarDAO.save(rybar);

            rybarListView.getItems().clear();
            rybarListView.getItems().addAll(rybarDAO.getAll());
        } catch (NumberFormatException e) {
            System.out.println("ID musí byť číslo!");
        } catch (IllegalArgumentException e) {
            System.out.println("Chyba: " + e.getMessage());
        }
    }

    @FXML
    void initialize() {
        rybarListView.getItems().addAll(rybarDAO.getAll());
    }
}
