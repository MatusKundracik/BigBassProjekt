package Rybar;

import org.junit.jupiter.api.*;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

import javax.sql.DataSource;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class SQLRybarDAOTest {

    private static DataSource dataSource;
    private static JdbcTemplate jdbcTemplate;
    private SQLRybarDAO sqlRybarDAO;

    @BeforeAll
    static void setUpDatabase() {
        dataSource = new SingleConnectionDataSource("jdbc:sqlite::memory:", true);
        jdbcTemplate = new JdbcTemplate(dataSource);

        String createTableQuery = "CREATE TABLE rybar (" +
                "id_rybara INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "meno TEXT, " +
                "priezvisko TEXT, " +
                "adresa TEXT, " +
                "cislo_obcianskeho_preukazu TEXT, " +
                "statna_prislusnost TEXT, " +
                "datum_narodenia TEXT, " +
                "pridany_do_evidencie TEXT, " +
                "odhlaseny_z_evidencie TEXT, " +
                "email TEXT UNIQUE, " +
                "heslo TEXT)";
        jdbcTemplate.execute(createTableQuery);
    }

    @BeforeEach
    void setUp() {
        sqlRybarDAO = new SQLRybarDAO(jdbcTemplate);
    }

    @AfterEach
    void cleanUp() {
        jdbcTemplate.update("DELETE FROM rybar");
    }

    @AfterAll
    static void tearDownDatabase() {
        ((SingleConnectionDataSource) dataSource).destroy();
    }

    private Rybar createTestRybar() {
        return new Rybar("Janko", "Hrasko", "Ulica 123", "123456789", "Slovensko",
                LocalDate.of(1990, 5, 1), LocalDate.now(), null, "janko@hrasko.sk", "heslo");
    }

    @Test
    void testInsertUser() {
        Rybar rybar = createTestRybar();
        sqlRybarDAO.insertUser(rybar);

        Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM rybar WHERE email = ?", Integer.class, "janko@hrasko.sk");
        assertNotNull(count);
        assertEquals(1, count);
    }

    @Test
    void testJeEmailPouzity() {
        sqlRybarDAO.insertUser(createTestRybar());
        assertTrue(sqlRybarDAO.jeEmailPouzity("janko@hrasko.sk"));
        assertFalse(sqlRybarDAO.jeEmailPouzity("neexistujuci@hrasko.sk"));
    }

    @Test
    void testGetUserIdByEmail() {
        sqlRybarDAO.insertUser(createTestRybar());
        int userId = sqlRybarDAO.getUserIdByEmail("janko@hrasko.sk");
        assertTrue(userId > 0);
    }

    @Test
    void testOveritPouzivatela() {
        String email = "janko@hrasko.sk";
        String heslo = "heslo";
        Rybar rybar = new Rybar("Janko", "Hrasko", "Ulica 123", "123456789", "Slovensko",
                LocalDate.of(1990, 5, 1), LocalDate.now(), null, email, heslo);
        sqlRybarDAO.insertUser(rybar);

        assertTrue(sqlRybarDAO.overitPouzivatela(email, heslo));
        assertFalse(sqlRybarDAO.overitPouzivatela(email, "nespravneHeslo"));
        assertFalse(sqlRybarDAO.overitPouzivatela("neexistujuci@hrasko.sk", "heslo"));
    }

    @Test
    void testGetRybarNameById() {
        sqlRybarDAO.insertUser(createTestRybar());
        int userId = sqlRybarDAO.getUserIdByEmail("janko@hrasko.sk");
        assertEquals("Janko Hrasko", sqlRybarDAO.getRybarNameById(userId));
    }

    @Test
    void testGetRybarAdresaById() {
        sqlRybarDAO.insertUser(createTestRybar());
        int userId = sqlRybarDAO.getUserIdByEmail("janko@hrasko.sk");
        assertEquals("Ulica 123", sqlRybarDAO.getRybarAdresaById(userId));
    }

    @Test
    void testGetRybarDatumNarById() {
        sqlRybarDAO.insertUser(createTestRybar());
        int userId = sqlRybarDAO.getUserIdByEmail("janko@hrasko.sk");
        assertEquals("1990-05-01", sqlRybarDAO.getRybarDatumNarById(userId));
    }

    @Test
    void testGetRybarEmailById() {
        sqlRybarDAO.insertUser(createTestRybar());
        int userId = sqlRybarDAO.getUserIdByEmail("janko@hrasko.sk");
        assertEquals("janko@hrasko.sk", sqlRybarDAO.getRybarEmailById(userId));
    }

    @Test
    void testGetRybarPridanyDoEvidencieById() {
        sqlRybarDAO.insertUser(createTestRybar());
        int userId = sqlRybarDAO.getUserIdByEmail("janko@hrasko.sk");
        assertNotNull(sqlRybarDAO.getRybarPridanyDoEvidencieById(userId));
    }

    @Test
    void testGetRybarMenoPriezviskoById() {
        sqlRybarDAO.insertUser(createTestRybar());
        int userId = sqlRybarDAO.getUserIdByEmail("janko@hrasko.sk");
        assertEquals("Janko Hrasko", sqlRybarDAO.getRybarMenoPriezviskoById(userId));
    }
}