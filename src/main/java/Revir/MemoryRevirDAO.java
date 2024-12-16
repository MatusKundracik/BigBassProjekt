package Revir;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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

    public int getRevirIdByName(Connection connection, String nazovReviru) throws SQLException {
        String sql = "SELECT id_revira FROM revir WHERE nazov = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, nazovReviru);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("id_revira");
                } else {
                    throw new RuntimeException("Revír nenájdený s názvom: " + nazovReviru + " nebolo nájdené.");
                }
            }
        }
    }


}
