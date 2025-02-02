package Povolenie;


import org.projekt.Session;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.*;
import java.util.List;

public class MemoryPovolenieDAO implements PovolenieDAO {

    private int idPovolenie;
    private final JdbcTemplate jdbcTemplate;


    public MemoryPovolenieDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void insertPovolenie(Povolenie povolenie) {
        String insertQuery = "INSERT INTO povolenie (platnost_od, platnost_do, pstruhove, lipňove, kaprové, rybar_id_rybara) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(insertQuery,
                povolenie.getPlatnostOd().toString(),
                povolenie.getPlatnostDo().toString(),
                povolenie.isPstruhove() ? 1 : 0,
                povolenie.isLipnove() ? 1 : 0,
                povolenie.isKaprove() ? 1 : 0,
                povolenie.getRybarIdRybara());
    }

    @Override
    public String generatePovolenieMessage(int rybarID, boolean kaprove, boolean lipnove, boolean pstruhove) {
        StringBuilder message = new StringBuilder("Rybárovi " + rybarID + " bolo pridané ");
        List<String> typyPovolenia = new java.util.ArrayList<>();

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

    @Override
    public int getPovolenieIdByRybarId(int rybarId) {
        String sql = "SELECT id_povolenie FROM povolenie WHERE rybar_id_rybara = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, rybarId);
    }

    public boolean zobrazKaprovePovolenie(int idRybara) {
        String query = "SELECT kaprové FROM povolenie WHERE rybar_id_rybara = ? " +
                "ORDER BY platnost_od DESC LIMIT 1";
        try {
            return Boolean.TRUE.equals(jdbcTemplate.queryForObject(query, Boolean.class, idRybara));
        } catch (EmptyResultDataAccessException e) {
            return false;
        }
    }

    public boolean zobrazPstruhovePovolenie(int idRybara) {
        String query = "SELECT pstruhove FROM povolenie WHERE rybar_id_rybara = ? " +
                "ORDER BY platnost_od DESC LIMIT 1";
        try {
            return Boolean.TRUE.equals(jdbcTemplate.queryForObject(query, Boolean.class, idRybara));
        } catch (EmptyResultDataAccessException e) {
            return false;
        }
    }

    public boolean zobrazLipnovePovolenie(int idRybara) {
        String query = "SELECT lipňove FROM povolenie WHERE rybar_id_rybara = ? " +
                "ORDER BY platnost_od DESC LIMIT 1";
        try {
            return Boolean.TRUE.equals(jdbcTemplate.queryForObject(query, Boolean.class, idRybara));
        } catch (EmptyResultDataAccessException e) {
            return false;
        }
    }


    public void idPovoleniaPodlaRybaraID() {
        String selectQuery = "SELECT id_povolenie FROM povolenie WHERE rybar_id_rybara = ? " +
                "ORDER BY platnost_od DESC LIMIT 1";

    }

}