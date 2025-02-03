package Rybar;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.dao.EmptyResultDataAccessException;

public class SQLRybarDAO implements RybarDAO {
    private final JdbcTemplate jdbcTemplate;

    public SQLRybarDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void save(Rybar rybar) {
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
                    rybar.getCisloObcianskehoPreukazu(),
                    rybar.getStatnaPrislusnost(),
                    rybar.getDatumNarodenia().toString(),
                    rybar.getPridanyDoEvidencie().toString(),
                    rybar.getOdhlasenyZEvidencie() != null ? rybar.getOdhlasenyZEvidencie().toString() : null,
                    rybar.getEmail(),
                    BCrypt.hashpw(rybar.getHeslo(), BCrypt.gensalt()));
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

    @Override
    public String getRybarAdresaById(int idRybara) {
        String sql = "SELECT adresa FROM rybar WHERE id_rybara = ?";
        try {
            return jdbcTemplate.queryForObject(sql, String.class, idRybara);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public String getRybarDatumNarById(int idRybara) {
        String sql = "SELECT datum_narodenia FROM rybar WHERE id_rybara = ?";
        try {
            return jdbcTemplate.queryForObject(sql, String.class, idRybara);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public String getRybarEmailById(int idRybara) {
        String sql = "SELECT email FROM rybar WHERE id_rybara = ?";
        try {
            return jdbcTemplate.queryForObject(sql, String.class, idRybara);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public String getRybarPridanyDoEvidencieById(int idRybara) {
        String sql = "SELECT pridany_do_evidencie FROM rybar WHERE id_rybara = ?";
        try {
            return jdbcTemplate.queryForObject(sql, String.class, idRybara);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public String getRybarMenoPriezviskoById(int idRybara) {
        String sql = "SELECT meno, priezvisko FROM rybar WHERE id_rybara = ?";
        try {
            return jdbcTemplate.queryForObject(sql, (rs, rowNum) ->
                    rs.getString("meno") + " " + rs.getString("priezvisko"), idRybara);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
}
