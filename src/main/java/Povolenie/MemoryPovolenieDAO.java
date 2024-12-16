package Povolenie;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MemoryPovolenieDAO implements PovolenieDAO {

    public void insertPovolenie(Connection connection, Povolenie povolenie) throws SQLException {
        String insertQuery = "INSERT INTO povolenie (platnost_od, platnost_do, pstruhove, lipňove, kaprové, rybar_id_rybara) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(insertQuery)) {
            statement.setString(1, povolenie.getPlatnostOd().toString());
            statement.setString(2, povolenie.getPlatnostDo().toString());
            statement.setInt(3, povolenie.isPstruhove() ? 1 : 0);
            statement.setInt(4, povolenie.isLipnove() ? 1 : 0);
            statement.setInt(5, povolenie.isKaprove() ? 1 : 0);
            statement.setInt(6, (int) povolenie.getRybarIdRybara());

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Chyba pri vkladaní povolenia do databázy", e);
        }
    }

    public String generatePovolenieMessage(int rybarID, boolean kaprove, boolean lipnove, boolean pstruhove) {
        StringBuilder message = new StringBuilder("Rybárovi " + rybarID + " bolo pridané ");
        List<String> typyPovolenia = new ArrayList<>();

        if (kaprove) typyPovolenia.add("kaprové");
        if (lipnove) typyPovolenia.add("lipňové");
        if (pstruhove) typyPovolenia.add("pstruhové");

        if (typyPovolenia.isEmpty()) {
            message.append("žiadne povolenie.");
        } else {
            message.append(String.join(", ", typyPovolenia)).append(" povolenie.");
        }

        return message.toString();

    }

    public int getPovolenieIdByRybarId(Connection connection, int rybarId) throws SQLException {
        String query = "SELECT id_povolenie FROM povolenie WHERE rybar_id_rybara = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, rybarId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("id_povolenie");
                } else {
                    throw new RuntimeException("Povolenie pre rybára s ID " + rybarId + " nebolo nájdené.");
                }
            }
        }
    }




}
