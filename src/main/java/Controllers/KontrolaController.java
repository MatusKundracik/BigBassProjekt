package Controllers;

import Ulovok.UlovokDAO;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.projekt.Factory;

import java.util.List;
import java.util.stream.Collectors;

public class KontrolaController {
    private final UlovokDAO ulovokDAO = Factory.INSTANCE.getUlovokDAO();

    @FXML
    private ComboBox<String> emailComboBox;

    @FXML
    private ListView<String> kontrolaListView;

    @FXML
    private Button zapisKontroluButton;

    @FXML
    public void initialize() {
        // Načítanie registrovaných e-mailov do ComboBoxu
        List<String> registeredEmails = ulovokDAO.getRegisteredEmails();
        emailComboBox.setItems(FXCollections.observableArrayList(registeredEmails));

        // Listener na zmenu výberu e-mailu
        emailComboBox.setOnAction(event -> loadUlovkyForSelectedEmail());
    }

    private void loadUlovkyForSelectedEmail() {
        String selectedEmail = emailComboBox.getValue();
        if (selectedEmail != null && !selectedEmail.isEmpty()) {
            List<String> ulovky = ulovokDAO.getUlovkyByEmail(selectedEmail);
            List<String> formattedUlovky = ulovky.stream()
                    .map(ulovok -> ulovok.substring(ulovok.indexOf(' ') + 1)) // Odstrániť ID, ponechať popis
                    .collect(Collectors.toList());
            kontrolaListView.setItems(FXCollections.observableArrayList(formattedUlovky));
        }
    }

    @FXML
    void skontrolujUlovok(ActionEvent event) {
        String selectedEmail = emailComboBox.getValue();
        if (selectedEmail == null || selectedEmail.isEmpty()) {
            showAlert("Chyba", "Musíte vybrať e-mail používateľa.");
            return;
        }

        String selectedUlovok = kontrolaListView.getSelectionModel().getSelectedItem();
        if (selectedUlovok == null) {
            showAlert("Chyba", "Musíte vybrať úlovok na kontrolu.");
            return;
        }

        try {
            String originalUlovok = ulovokDAO.getUlovkyByEmail(selectedEmail).stream()
                    .filter(ulovok -> ulovok.contains(selectedUlovok))
                    .findFirst()
                    .orElseThrow(() -> new Exception("Úlovok nenájdený v databáze."));

            int idUlovku = Integer.parseInt(originalUlovok.split(" ")[0]); // ID sa berie z originálneho zoznamu
            boolean uspech = ulovokDAO.aktualizujKontrolu(idUlovku);
            if (uspech) {
                showAlert("Úspech", "Úlovok bol úspešne skontrolovaný.");
                loadUlovkyForSelectedEmail(); // Obnoviť zoznam
            } else {
                showAlert("Chyba", "ID úlovku neexistuje: " + idUlovku);
            }
        } catch (Exception e) {
            showAlert("Chyba", "Chyba pri aktualizácii úlovku: " + e.getMessage());
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
