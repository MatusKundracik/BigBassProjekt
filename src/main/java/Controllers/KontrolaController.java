package Controllers;

import Ulovok.UlovokDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import org.projekt.Factory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class KontrolaController {
    private final UlovokDAO ulovokDAO = Factory.INSTANCE.getUlovokDAO();
    @FXML
    private TextField idUlovkyTextField;

    @FXML
    private ListView<String> kontrolaListView;

    @FXML
    private Button zapisKontroluButton;

    @FXML
    void addKontroluButton(ActionEvent event) {
        String idUlovkuText = idUlovkyTextField.getText();
        if (idUlovkuText == null || idUlovkuText.isEmpty()) {
            kontrolaListView.getItems().add("ID úlovku musí byť zadané!");
            return;
        }

        try {
            int idUlovku = Integer.parseInt(idUlovkuText);
            boolean uspech = ulovokDAO.aktualizujKontrolu(idUlovku);
            if (uspech) {
                kontrolaListView.getItems().add("Úlovok " + idUlovku + " bol úspešne skontrolovaný.");
            } else {
                kontrolaListView.getItems().add("ID úlovku neexistuje: " + idUlovku);
            }
        } catch (NumberFormatException e) {
            kontrolaListView.getItems().add("Neplatný formát ID úlovku!");
        } catch (Exception e) {
            kontrolaListView.getItems().add("Chyba pri aktualizácii úlovku: " + e.getMessage());
        }
    }
}


