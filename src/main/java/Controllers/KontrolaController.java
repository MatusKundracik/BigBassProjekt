package Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import org.projekt.Povolenie;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class KontrolaController {

    @FXML
    private TextField idUlovkyTextField;

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
            PreparedStatement preparedStatement = connection.prepareStatement(updateQuery);
            preparedStatement.setInt(1, Integer.parseInt(idUlovku));

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Kontrola bola úspešne zapísaná pre ID úlovku: " + idUlovku);
            } else {
                System.out.println("ID úlovku neexistuje: " + idUlovku);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (NumberFormatException e) {
            System.out.println("Neplatný formát ID úlovku!");
        }
    }
    }


