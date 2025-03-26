package Revir;

import org.junit.jupiter.api.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SQLRevirDAOTest {

    private JdbcTemplate jdbcTemplate;
    private SQLRevirDAO sqlRevirDAO;
    private SingleConnectionDataSource dataSource;

    @BeforeAll
    void setUpDatabase() {
        dataSource = new SingleConnectionDataSource("jdbc:sqlite::memory:", false); // Vypnutý auto-commit
        jdbcTemplate = new JdbcTemplate(dataSource);
        sqlRevirDAO = new SQLRevirDAO(jdbcTemplate);

        String createRevirTable = "CREATE TABLE revir (" +
                "id_revira INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "nazov TEXT NOT NULL UNIQUE, " +
                "lokalita TEXT NOT NULL, " +
                "popis TEXT NOT NULL, " +
                "kaprove INTEGER NOT NULL, " +
                "lipnove INTEGER NOT NULL, " +
                "pstruhove INTEGER NOT NULL);";

        String createPovolenieTable = "CREATE TABLE povolenie (" +
                "id_povolenia INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "rybar_id_rybara INTEGER NOT NULL, " +
                "kaprové INTEGER NOT NULL, " +
                "pstruhove INTEGER NOT NULL, " +
                "lipňove INTEGER NOT NULL);";

        jdbcTemplate.execute(createRevirTable);
        jdbcTemplate.execute(createPovolenieTable);
    }

    @BeforeEach
    void cleanUp() {
        jdbcTemplate.update("DELETE FROM povolenie");
        jdbcTemplate.update("DELETE FROM revir");
    }

    @AfterAll
    void tearDown() {
        dataSource.destroy();
    }

    @Test
    void testInsertRevir() {
        Revir revir = new Revir("Revír Test", "Lokalita A", "Popis pre revír.", true, false, true);
        sqlRevirDAO.insertRevir(revir);

        int count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM revir WHERE nazov = ?", Integer.class, "Revír Test");
        assertEquals(1, count);
    }

    @Test
    void testInsertDuplicateRevir() {
        Revir revir = new Revir("Revír Test", "Lokalita A", "Popis pre revír.", true, false, true);
        sqlRevirDAO.insertRevir(revir);

        assertThrows(IllegalArgumentException.class, () -> sqlRevirDAO.insertRevir(revir));
    }

    @Test
    void testGetRevirIdByName() {
        jdbcTemplate.update("INSERT INTO revir (nazov, lokalita, popis, kaprove, lipnove, pstruhove) VALUES (?, ?, ?, ?, ?, ?)",
                "Revír 1", "Lokalita 1", "Popis 1", 1, 0, 1);

        int id = sqlRevirDAO.getRevirIdByName("Revír 1");
        assertTrue(id > 0);

        Exception exception = assertThrows(RuntimeException.class, () -> sqlRevirDAO.getRevirIdByName("Neexistujúci Revír"));
        assertEquals("Revír nenájdený s názvom: Neexistujúci Revír", exception.getMessage());
    }

    @Test
    void testGetAllReviry() {
        // Test prázdnej tabuľky
        List<Revir> emptyReviry = sqlRevirDAO.getAllReviry();
        assertTrue(emptyReviry.isEmpty());

        // Pridanie záznamov
        jdbcTemplate.update("INSERT INTO revir (nazov, lokalita, popis, kaprove, lipnove, pstruhove) VALUES (?, ?, ?, ?, ?, ?)",
                "Revír A", "Lokalita X", "Popis A", 1, 0, 1);
        jdbcTemplate.update("INSERT INTO revir (nazov, lokalita, popis, kaprove, lipnove, pstruhove) VALUES (?, ?, ?, ?, ?, ?)",
                "Revír B", "Lokalita Y", "Popis B", 0, 1, 0);

        List<Revir> reviry = sqlRevirDAO.getAllReviry();
        assertEquals(2, reviry.size());

        assertEquals("Revír A", reviry.get(0).getNazov());
        assertEquals("Revír B", reviry.get(1).getNazov());
    }

    @Test
    void testGetReviryForRybar() {
        jdbcTemplate.update("INSERT INTO revir (nazov, lokalita, popis, kaprove, lipnove, pstruhove) VALUES ('Revír K', 'Lok X', 'Popis K', 1, 0, 0)");
        jdbcTemplate.update("INSERT INTO revir (nazov, lokalita, popis, kaprove, lipnove, pstruhove) VALUES ('Revír L', 'Lok Y', 'Popis L', 0, 1, 0)");

        jdbcTemplate.update("INSERT INTO povolenie (rybar_id_rybara, kaprové, pstruhove, lipňove) VALUES (1, 1, 0, 0)");

        Map<String, Integer> reviry = sqlRevirDAO.getReviryForRybar(1);
        assertEquals(1, reviry.size());
        assertTrue(reviry.containsKey("Revír K"));
    }

    @Test
    void testGetReviryForRybarNoPermission() {
        jdbcTemplate.update("INSERT INTO revir (nazov, lokalita, popis, kaprove, lipnove, pstruhove) VALUES ('Revír M', 'Lok Z', 'Popis M', 1, 0, 0)");

        // Nepridáme rybára do povolení
        Map<String, Integer> reviry = sqlRevirDAO.getReviryForRybar(2);
        assertTrue(reviry.isEmpty());
    }
}
