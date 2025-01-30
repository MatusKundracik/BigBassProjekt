package Ulovok;


import Povolenie.PovolenieDAO;
import Revir.RevirDAO;
import org.projekt.Factory;
import org.projekt.Session;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class MemoryUlovokDAO implements UlovokDAO {

    private JdbcTemplate jdbcTemplate;

    public MemoryUlovokDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private PovolenieDAO povolenieDAO = Factory.INSTANCE.getPovolenieDAO();
    private RevirDAO revirDAO = Factory.INSTANCE.getRevirDAO();

    public void insertUlovok(Connection connection, Ulovok ulovok) throws SQLException {
        // ačanie ID povolenia a ID revíru z databázy
        String insertQuery = "INSERT INTO ulovok (datum, cislo_reviru, druh_ryby, dlzka_v_cm, hmotnost_v_kg," +
                "povolenie_id_povolenie, povolenie_rybar_id_rybara, revir_id_revira, kontrola) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, NULL)";

        try (PreparedStatement statement = connection.prepareStatement(insertQuery)) {
            statement.setString(1, ulovok.getDatumUlovku().toString());
            statement.setString(2, ulovok.getCisloReviru());
            statement.setString(3, ulovok.getDruhRyby());
            statement.setDouble(4, ulovok.getDlzkaVcm());
            statement.setDouble(5, ulovok.getHmotnostVkg());
            statement.setInt(6, povolenieDAO.getPovolenieIdByRybarId(Session.aktualnyRybarId));
            statement.setInt(7, Session.aktualnyRybarId);
            statement.setInt(8, revirDAO.getRevirIdByName(connection, ulovok.getCisloReviru()));


            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Chyba pri vkladaní úlovku do databázy", e);
        }
    }
}