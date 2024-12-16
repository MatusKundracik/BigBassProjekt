package Revir;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MemoryRevirDAO implements RevirDAO {


    public void insertRevir(Connection connection, Revir revir) throws SQLException {
        String insertQuery = "INSERT INTO revir (nazov, lokalita, popis, kaprove, lipnove, pstruhove) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(insertQuery)) {
            statement.setString(1, revir.getNazov());
            statement.setString(2, revir.getLokalita());
            statement.setString(3, revir.getPopis());

            statement.setInt(4, revir.isKaprove() ? 1 : 0);
            statement.setInt(5, revir.isLipnove() ? 1 : 0);
            statement.setInt(6, revir.isPstruhove() ? 1 : 0);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Chyba pri vkladaní revíru do databázy", e);
        }
    }




}
