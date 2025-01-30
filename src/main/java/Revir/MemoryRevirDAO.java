package Revir;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SingleColumnRowMapper;
import org.springframework.dao.EmptyResultDataAccessException;

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
}
