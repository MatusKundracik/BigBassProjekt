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

public class SQLUlovokDAO implements UlovokDAO {

    private final JdbcTemplate jdbcTemplate;
    private final PovolenieDAO povolenieDAO;
    private final RevirDAO revirDAO;

    public SQLUlovokDAO(JdbcTemplate jdbcTemplate) {
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
        System.out.println(rybarId);
        String sql = "SELECT MAX(dlzka_v_cm) FROM ulovok WHERE povolenie_rybar_id_rybara = ?";
        Integer result = jdbcTemplate.queryForObject(sql, Integer.class, rybarId);
        return result != null ? result : 0;  // Ak je null, vráť 0
    }

    @Override
    public Double vypocitajNajtazsiUlovok(int rybarId) {
        String sql = "SELECT MAX(hmotnost_v_kg) FROM ulovok WHERE povolenie_rybar_id_rybara = ?";
        Double najtazsiUlovok = jdbcTemplate.queryForObject(sql, Double.class, rybarId);
        return najtazsiUlovok != null ? najtazsiUlovok : 0.0;
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

    // Získa všetky registrované e-maily rybárov
    public List<String> getRegisteredEmails() {
        String query = "SELECT DISTINCT email FROM rybar";
        return jdbcTemplate.queryForList(query, String.class);
    }

    // Získa všetky úlovky pre vybraného používateľa na základe e-mailu
    public List<String> getUlovkyByEmail(String email) {
        String query = "SELECT id_ulovok, datum, druh_ryby, dlzka_v_cm, hmotnost_v_kg FROM ulovok WHERE povolenie_rybar_id_rybara = (SELECT id_rybara FROM rybar WHERE email = ?)";
        RowMapper<String> rowMapper = (rs, rowNum) ->
                rs.getInt("id_ulovok") + " - " +
                        rs.getString("datum") + " - " +
                        rs.getString("druh_ryby") + " (" +
                        rs.getDouble("dlzka_v_cm") + " cm, " +
                        rs.getDouble("hmotnost_v_kg") + " kg)";

        return jdbcTemplate.query(query, rowMapper, email);
    }

    // Aktualizuje kontrolu úlovku
    public boolean aktualizujKontrolu(int idUlovku) {
        String query = "UPDATE ulovok SET kontrola = 1 WHERE id_ulovok = ?";
        return jdbcTemplate.update(query, idUlovku) > 0;
    }




}