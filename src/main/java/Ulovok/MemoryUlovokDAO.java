package Ulovok;

import Povolenie.MemoryPovolenieDAO;
import Povolenie.PovolenieDAO;
import Revir.MemoryRevirDAO;
import Revir.RevirDAO;
import org.projekt.Session;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MemoryUlovokDAO implements UlovokDAO {

    public void insertUlovok(Connection connection, Ulovok ulovok, String nazovReviru) {
        try {
            // Získanie ID aktuálneho rybára zo Session
            int aktualnyRybarId = Session.aktualnyRybarId;

            // Získanie ID povolenia a ID revíru
            PovolenieDAO povolenieDAO = new MemoryPovolenieDAO();
            int povolenieId = povolenieDAO.getPovolenieIdByRybarId(connection, aktualnyRybarId);

            RevirDAO revirDAO = new MemoryRevirDAO();
            int revirId = revirDAO.getRevirIdByName(connection, nazovReviru);

            // Príprava SQL dotazu
            String sql = "INSERT INTO ulovok (datum, cislo_reviru, druh_ryby, dlzka_v_cm, hmotnost_v_kg, kontrola, povolenie_rybar_id_rybara, revir_id_revira, povolenie_id_povolenie) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, ulovok.getDatumUlovku().toString());
                preparedStatement.setString(2, ulovok.getCisloReviru());
                preparedStatement.setString(3, ulovok.getDruhRyby());
                preparedStatement.setDouble(4, ulovok.getDlzkaVcm());
                preparedStatement.setDouble(5, ulovok.getHmotnostVkg());
                preparedStatement.setInt(6, ulovok.getKontrola());
                preparedStatement.setInt(7, aktualnyRybarId);
                preparedStatement.setInt(8, revirId);
                preparedStatement.setInt(9, povolenieId);

                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Chyba pri vkladaní úlovku do databázy", e);
        }
    }



}
