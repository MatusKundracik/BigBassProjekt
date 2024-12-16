package Ulovok;

import org.projekt.Session;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class MemoryUlovokDAO implements UlovokDAO {

    private int idRevir;
    private int idPovolenie;

    public void insertUlovok(Connection connection, Ulovok ulovok) throws SQLException {
        // SQL dotaz pre vloženie úlovku
        String insertQuery = "INSERT INTO ulovok (datum, cislo_reviru, druh_ryby, dlzka_v_cm, hmotnost_v_kg," +
                "povolenie_id_povolenie, povolenie_rybar_id_rybara, revir_id_revira, kontrola) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(insertQuery)) {

            statement.setString(1, ulovok.getDatumUlovku().toString());
            statement.setString(2, ulovok.getCisloReviru());
            statement.setString(3, ulovok.getDruhRyby());
            statement.setDouble(4, ulovok.getDlzkaVcm());
            statement.setDouble(5, ulovok.getHmotnostVkg());
            statement.setInt(6, ulovok.getKontrola());
            statement.setInt(6, idPovolenie);
            statement.setInt(7, Session.aktualnyRybarId);
            statement.setInt(8, idRevir);

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Chyba pri vkladaní úlovku do databázy", e);
        }
    }








}
