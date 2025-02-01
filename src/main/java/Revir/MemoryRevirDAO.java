package Revir;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SingleColumnRowMapper;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MemoryRevirDAO implements RevirDAO {

    private final JdbcTemplate jdbcTemplate;

    public MemoryRevirDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void insertRevir(Revir revir) {
        String insertQuery = "INSERT INTO revir (nazov, lokalita, popis, kaprove, lipnove, pstruhove) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(insertQuery,
                revir.getNazov(),
                revir.getLokalita(),
                revir.getPopis(),
                revir.isKaprove() ? 1 : 0,
                revir.isLipnove() ? 1 : 0,
                revir.isPstruhove() ? 1 : 0);
    }

    @Override
    public int getRevirIdByName(String nazovReviru) {
        String sql = "SELECT id_revira FROM revir WHERE nazov = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new SingleColumnRowMapper<>(Integer.class), nazovReviru);
        } catch (EmptyResultDataAccessException e) {
            throw new RuntimeException("Revír nenájdený s názvom: " + nazovReviru);
        }
    }

    @Override
    public Map<String, Integer> getReviryForRybar(int rybarId) {
        String sql = "SELECT r.nazov, r.id_revira FROM revir r " +
                "JOIN povolenie p ON (" +
                "    (p.kaprové = 1 AND r.kaprove = 1) OR " +
                "    (p.pstruhove = 1 AND r.pstruhove = 1) OR " +
                "    (p.lipňove = 1 AND r.lipnove = 1)" +
                ") " +
                "WHERE p.rybar_id_rybara = ?";

        List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql, rybarId);
        Map<String, Integer> revirMap = new HashMap<>();

        for (Map<String, Object> row : rows) {
            String nazov = (String) row.get("nazov");
            Integer idRevir = (Integer) row.get("id_revira");
            revirMap.put(nazov, idRevir);
        }

        return revirMap;
    }

}
