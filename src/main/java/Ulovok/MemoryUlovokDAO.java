package Ulovok;

import Povolenie.PovolenieDAO;
import Revir.RevirDAO;
import org.projekt.Factory;
import org.projekt.Session;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MemoryUlovokDAO implements UlovokDAO {

    private final JdbcTemplate jdbcTemplate;
    private final PovolenieDAO povolenieDAO;
    private final RevirDAO revirDAO;

    public MemoryUlovokDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.povolenieDAO = Factory.INSTANCE.getPovolenieDAO();
        this.revirDAO = Factory.INSTANCE.getRevirDAO();
    }

    @Override
    public void insertUlovok(Ulovok ulovok) {
        String insertQuery = "INSERT INTO ulovok (datum, cislo_reviru, druh_ryby, dlzka_v_cm, hmotnost_v_kg, " +
                "povolenie_id_povolenie, povolenie_rybar_id_rybara, revir_id_revira, kontrola) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, NULL)";

        try {
            int povolenieId = povolenieDAO.getPovolenieIdByRybarId(Session.aktualnyRybarId);
            int revirId = revirDAO.getRevirIdByName(ulovok.getCisloReviru());

            jdbcTemplate.update(insertQuery,
                    ulovok.getDatumUlovku().toString(),
                    ulovok.getCisloReviru(),
                    ulovok.getDruhRyby(),
                    ulovok.getDlzkaVcm(),
                    ulovok.getHmotnostVkg(),
                    povolenieId,
                    Session.aktualnyRybarId,
                    revirId);
        } catch (EmptyResultDataAccessException e) {
            throw new RuntimeException("Povolenie alebo revír nebol nájdený.", e);
        } catch (Exception e) {
            throw new RuntimeException("Chyba pri vkladaní úlovku do databázy.", e);
        }
    }

    @Override
    public int vypocitajCelkovyPocetRyb(int rybarId) {
        String sql = "SELECT COUNT(*) FROM ulovok WHERE povolenie_rybar_id_rybara = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, rybarId);
    }

    @Override
    public int vypocitajNajvacsiUlovok(int rybarId) {
        String sql = "SELECT MAX(dlzka_v_cm) FROM ulovok WHERE povolenie_rybar_id_rybara = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, rybarId);
    }

    @Override
    public double vypocitajNajtazsiUlovok(int rybarId) {
        String sql = "SELECT MAX(hmotnost_v_kg) FROM ulovok WHERE povolenie_rybar_id_rybara = ?";
        return jdbcTemplate.queryForObject(sql, Double.class, rybarId);
    }

    @Override
    public Map<String, Integer> nacitajPoctyUlovkovZaMesiac(int rybarId) {
        Map<String, Integer> ulovkyZaMesiac = new HashMap<>();

        String query = "SELECT strftime('%Y-%m', datum) AS mesiac, COUNT(*) AS pocet_ulovkov " +
                "FROM ulovok " +
                "WHERE povolenie_rybar_id_rybara = ? " +
                "GROUP BY strftime('%Y-%m', datum) " +
                "ORDER BY pocet_ulovkov DESC " +
                "LIMIT 3";

        jdbcTemplate.query(query, new Object[]{rybarId}, (rs) -> {
            ulovkyZaMesiac.put(rs.getString("mesiac"), rs.getInt("pocet_ulovkov"));
        });

        return ulovkyZaMesiac;
    }

    @Override
    public List<Ulovok> nacitajUlovkyPreRybara(int rybarId) {
        String query = "SELECT * FROM ulovok WHERE povolenie_rybar_id_rybara = ?";
        return jdbcTemplate.query(query, new Object[]{rybarId}, new UlovokRowMapper());
    }

    private static class UlovokRowMapper implements RowMapper<Ulovok> {
        @Override
        public Ulovok mapRow(ResultSet rs, int rowNum) throws SQLException {
            LocalDate datum = LocalDate.parse(rs.getString("datum"));
            String cisloReviru = rs.getString("cislo_reviru");
            String druhRyby = rs.getString("druh_ryby");
            double dlzkaVcm = rs.getDouble("dlzka_v_cm");
            double hmotnostVkg = rs.getDouble("hmotnost_v_kg");
            int kontrola = rs.getInt("kontrola");

            return new Ulovok(datum, cisloReviru, druhRyby, dlzkaVcm, hmotnostVkg, kontrola);
        }
    }

    @Override
    public boolean aktualizujKontrolu(int idUlovku) {
        String sql = "UPDATE ulovok SET kontrola = 1 WHERE id_ulovok = ?";
        return jdbcTemplate.update(sql, idUlovku) > 0;
    }

}