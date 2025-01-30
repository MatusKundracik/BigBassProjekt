package Rybar;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.dao.EmptyResultDataAccessException;
import java.time.LocalDate;

public class MemoryRybarDAO implements RybarDAO {
    private final JdbcTemplate jdbcTemplate;

    public MemoryRybarDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void save(Rybar rybar) {  // Chýbajúca implementácia metódy save
        insertUser(rybar);
    }

    @Override
    public void insertUser(Rybar rybar) {
        String insertQuery = "INSERT INTO rybar (meno, priezvisko, adresa, cislo_obcianskeho_preukazu, statna_prislusnost, datum_narodenia, pridany_do_evidencie, odhlaseny_z_evidencie, email, heslo) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try {
            jdbcTemplate.update(insertQuery,
                    rybar.getMeno(),
                    rybar.getPriezvisko(),
                    rybar.getAdresa(),
                    rybar.getCisloObcianskehoPreukazu(),  // Opravený getter
                    rybar.getStatnaPrislusnost(),
                    rybar.getDatumNarodenia().toString(),
                    rybar.getPridanyDoEvidencie().toString(),
                    rybar.getOdhlasenyZEvidencie() != null ? rybar.getOdhlasenyZEvidencie().toString() : null,
                    rybar.getEmail(),
                    BCrypt.hashpw(rybar.getHeslo(), BCrypt.gensalt()));  // Opravený getter
        } catch (Exception e) {
            throw new RuntimeException("Chyba pri vkladaní rybára do databázy.", e);
        }
    }

    @Override
    public boolean jeEmailPouzity(String email) {
        String sql = "SELECT COUNT(*) FROM rybar WHERE email = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, email) > 0;
    }

    @Override
    public int getUserIdByEmail(String email) {
        String sql = "SELECT id_rybara FROM rybar WHERE email = ?";
        try {
            return jdbcTemplate.queryForObject(sql, Integer.class, email);
        } catch (EmptyResultDataAccessException e) {
            return -1;
        }
    }

    @Override
    public boolean overitPouzivatela(String email, String heslo) {
        String sql = "SELECT heslo FROM rybar WHERE email = ?";
        try {
            String hashedPassword = jdbcTemplate.queryForObject(sql, String.class, email);
            return BCrypt.checkpw(heslo, hashedPassword);
        } catch (EmptyResultDataAccessException e) {
            return false;
        }
    }

    @Override
    public String getRybarNameById(int idRybara) {
        String sql = "SELECT meno, priezvisko FROM rybar WHERE id_rybara = ?";
        try {
            return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> rs.getString("meno") + " " + rs.getString("priezvisko"), idRybara);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
}
