package org.projekt;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import org.projekt.Rybar;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class RegisterController {
    private List<Rybar> rybari = new ArrayList<>();
    @FXML
    private TextField adresaTextField;

    @FXML
    private DatePicker datumNarodeniaDatePicker;

    @FXML
    private DatePicker datumPridaniaDoEvidencieDatePicker;

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
    int idRybara = Integer.parseInt(IDTextField.getText());
    String meno = menoTextField.getText();
    String priezvisko = priezviskoTextField.getText();
    String adresa = adresaTextField.getText();
    String obcianskyPreukaz = obcianskyPreukazTextField.getText();
    String statnaPrislusnost = statnaPrislusnotTextField.getText();
    LocalDate datumNarodenia = datumNarodeniaDatePicker.getValue();

    Rybar rybar = new Rybar(idRybara,meno,priezvisko,datumNarodenia,adresa,statnaPrislusnost,obcianskyPreukaz,LocalDate.now(),null);
    this.rybari.add(rybar);
    rybarListView.getItems().add(rybar);

    }

    @FXML
    void initialize() {

        this.rybarListView.getItems().addAll(this.rybari);
    }
}
