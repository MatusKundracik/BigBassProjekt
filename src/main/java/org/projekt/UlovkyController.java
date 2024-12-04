package org.projekt;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import org.projekt.Ulovok;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class UlovkyController {

    private List<Ulovok> ulovky = new ArrayList<>();

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
    private ListView<?> ulovokListView;

    @FXML
    void addUlovokAction(ActionEvent event) {
        int idUlovok = Integer.parseInt(IDulovokTextField.getText());
        LocalDate datumUlovok = datumUlovkuDatePicker.getValue();
        int cisloReviru = Integer.parseInt(cisloReviruTextField.getText());
        String druhRyby = druhRybyTextField.getText();
        int dlzkaVcm = Integer.parseInt(dlzkaVcmTextField.getText());
        int hmotnostVkg = Integer.parseInt(hmotnostVkgTextField.getText());
        int kontrola = Integer.parseInt(kontrolaTextField.getText());

//        Ulovok ulovok = new Ulovok(idUlovok,datumUlovok,cisloReviru,druhRyby,dlzkaVcm,hmotnostVkg,kontrola);
//        this.ulovky.add(ulovok);
//        ulovokListView.getItems().add(ulovok);
    }

    @FXML
    void initialize() {
        //this.ulovokListView.getItems().addAll(this.ulovky);
    }

}