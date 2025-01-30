package Ulovok;

import Povolenie.PovolenieDAO;
import Revir.RevirDAO;
import org.projekt.Factory;
import org.projekt.Session;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

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
}