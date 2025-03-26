package Povolenie;

import org.junit.jupiter.api.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SQLPovolenieDAOTest {

    private Connection connection;
    private JdbcTemplate jdbcTemplate;
    private SQLPovolenieDAO sqlPovolenieDAO;

    @BeforeAll
    void setUpDatabase() throws SQLException {
        connection = DriverManager.getConnection("jdbc:sqlite::memory:");
        jdbcTemplate = new JdbcTemplate(new SingleConnectionDataSource(connection, true));
        sqlPovolenieDAO = new SQLPovolenieDAO(jdbcTemplate);

        jdbcTemplate.execute("CREATE TABLE povolenie (" +
                "id_povolenie INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "platnost_od TEXT, " +
                "platnost_do TEXT, " +
                "pstruhove INTEGER, " +
                "lipňove INTEGER, " +
                "kaprové INTEGER, " +
                "rybar_id_rybara INTEGER)");

        jdbcTemplate.execute("CREATE TABLE rybar (id_rybara INTEGER PRIMARY KEY, email TEXT)");
    }

    @AfterAll
    void tearDownDatabase() throws SQLException {
        if (connection != null) {
            connection.close();
        }
    }

    @BeforeEach
    void cleanUp() {
        jdbcTemplate.update("DELETE FROM povolenie");
        jdbcTemplate.update("DELETE FROM rybar");
    }

    @Test
    void testInsertPovolenie() {
        Povolenie povolenie = new Povolenie(LocalDate.of(2024, 1, 1), LocalDate.of(2024, 12, 31), true, false, true, 1);
        sqlPovolenieDAO.insertPovolenie(povolenie);
        Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM povolenie WHERE rybar_id_rybara = ?", Integer.class, 1);
        assertNotNull(count);
        assertEquals(1, count);
    }

    @Test
    void testGeneratePovolenieMessage() {
        assertEquals("Rybárovi 1 bolo pridané kaprové, pstruhové povolenie.", sqlPovolenieDAO.generatePovolenieMessage(1, true, false, true));
        assertEquals("Rybárovi 2 bolo pridané žiadne povolenie.", sqlPovolenieDAO.generatePovolenieMessage(2, false, false, false));
        assertEquals("Rybárovi 3 bolo pridané lipňové povolenie.", sqlPovolenieDAO.generatePovolenieMessage(3, false, true, false));
    }

    @Test
    void testGetPovolenieIdByRybarId() {
        jdbcTemplate.update("INSERT INTO povolenie (platnost_od, platnost_do, pstruhove, lipňove, kaprové, rybar_id_rybara) VALUES (?, ?, ?, ?, ?, ?)",
                "2024-01-01", "2024-12-31", 1, 0, 1, 1);
        int id = sqlPovolenieDAO.getPovolenieIdByRybarId(1);
        assertTrue(id > 0);
    }

    @Test
    void testZobrazKaprovePovolenie() {
        jdbcTemplate.update("INSERT INTO povolenie (platnost_od, platnost_do, pstruhove, lipňove, kaprové, rybar_id_rybara) VALUES (?, ?, ?, ?, ?, ?)",
                "2024-01-01", "2024-12-31", 0, 0, 1, 1);
        assertTrue(sqlPovolenieDAO.zobrazKaprovePovolenie(1));
        assertFalse(sqlPovolenieDAO.zobrazKaprovePovolenie(2));
    }

    @Test
    void testGetAllEmails() {
        jdbcTemplate.update("INSERT INTO rybar (id_rybara, email) VALUES (?, ?)", 1, "test@example.com");
        jdbcTemplate.update("INSERT INTO rybar (id_rybara, email) VALUES (?, ?)", 2, "test2@example.com");
        List<String> emails = sqlPovolenieDAO.getAllEmails();
        assertEquals(2, emails.size());
        assertTrue(emails.contains("test@example.com"));
        assertTrue(emails.contains("test2@example.com"));
    }

    @Test
    void testGetIdByEmail() {
        jdbcTemplate.update("INSERT INTO rybar (id_rybara, email) VALUES (?, ?)", 1, "test@example.com");
        int id = sqlPovolenieDAO.getIdByEmail("test@example.com");
        assertEquals(1, id);
    }

    @Test
    void testZobrazLipnovePovolenie() {
        jdbcTemplate.update("INSERT INTO povolenie (platnost_od, platnost_do, pstruhove, lipňove, kaprové, rybar_id_rybara) VALUES (?, ?, ?, ?, ?, ?)",
                "2024-01-01", "2024-12-31", 0, 1, 0, 1);
        assertTrue(sqlPovolenieDAO.zobrazLipnovePovolenie(1));
        assertFalse(sqlPovolenieDAO.zobrazLipnovePovolenie(2));
    }

    @Test
    void testZobrazPstruhovePovolenie() {
        jdbcTemplate.update("INSERT INTO povolenie (platnost_od, platnost_do, pstruhove, lipňove, kaprové, rybar_id_rybara) VALUES (?, ?, ?, ?, ?, ?)",
                "2024-01-01", "2024-12-31", 1, 0, 0, 1);
        assertTrue(sqlPovolenieDAO.zobrazPstruhovePovolenie(1));
        assertFalse(sqlPovolenieDAO.zobrazPstruhovePovolenie(2));
    }
}
