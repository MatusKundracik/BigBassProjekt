package Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class KontrolaController {

    @FXML
    private TextField idUlovkyTextField;

    @FXML
    private ListView<String> kontrolaListView;

    @FXML
    private Button zapisKontroluButton;

    @FXML
    void addKontroluButton(ActionEvent event) {
        String idUlovku = idUlovkyTextField.getText();
        if (idUlovku == null || idUlovku.isEmpty()) {
            System.out.println("ID úlovku musí byť zadané!");
            return;
        }

        // Pripojenie k databáze
        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:bigbass.db")) {
            // SQL na aktualizáciu stĺpca "kontrola"
            String updateQuery = "UPDATE ulovok SET kontrola = ifnull(kontrola, 1) WHERE id_ulovok = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {
                preparedStatement.setInt(1, Integer.parseInt(idUlovku));

                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected > 0) {
                    String message = "Úlovok " + idUlovku + " bol úspešne skontrolovaný.";
                    kontrolaListView.getItems().add(message);
                } else {
                    kontrolaListView.getItems().add("ID úlovku neexistuje: " + idUlovku);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            kontrolaListView.getItems().add("Chyba pri aktualizácii úlovku: " + e.getMessage());
        } catch (NumberFormatException e) {
            kontrolaListView.getItems().add("Neplatný formát ID úlovku!");
        }
    }
    }


