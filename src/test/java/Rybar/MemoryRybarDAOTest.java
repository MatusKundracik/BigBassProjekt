package Rybar;

import org.junit.jupiter.api.*;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class MemoryRybarDAOTest {

    private static Connection connection;
    private MemoryRybarDAO memoryRybarDAO;

    @BeforeAll
    static void setUpDatabase() throws SQLException {
        connection = DriverManager.getConnection("jdbc:sqlite::memory:");
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

        try (PreparedStatement statement = connection.prepareStatement(createTableQuery)) {
            statement.execute();
        }
    }

    @AfterAll
    static void tearDownDatabase() throws SQLException {
        if (connection != null) {
            connection.close();
        }
    }

    @BeforeEach
    void setUp() {
        memoryRybarDAO = new MemoryRybarDAO();
    }

    @AfterEach
    void cleanUp() throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement("DELETE FROM rybar")) {
            statement.executeUpdate();
        }
    }

    @Test
    void testSaveAndRetrieveRybar() {
        Rybar rybar = new Rybar("Jozef", "Novak", LocalDate.of(1990, 5, 15),
                "jozef.novak@example.com", "Bratislava", "Slovak", "AB123456",
                LocalDate.of(2023, 1, 1), null);
        memoryRybarDAO.save(rybar);

        assertEquals(1, rybar.getRybarId());
        assertThrows(IllegalArgumentException.class, () -> memoryRybarDAO.save(null));
        assertThrows(IllegalArgumentException.class, () -> memoryRybarDAO.save(new Rybar(null, "Novak", LocalDate.of(1990, 5, 15))));
    }

    @Test
    void testInsertUser() throws SQLException {
        LocalDate datumNarodenia = LocalDate.of(1990, 5, 15);
        LocalDate pridanyDoEvidencie = LocalDate.of(2023, 1, 1);

        memoryRybarDAO.insertUser(connection, "Jozef", "Novak", "Bratislava", "AB123456",
                "Slovak", datumNarodenia, pridanyDoEvidencie, null, "jozef.novak@example.com",
                BCrypt.hashpw("password123", BCrypt.gensalt()));

        String query = "SELECT * FROM rybar WHERE email = 'jozef.novak@example.com'";
        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            assertTrue(resultSet.next());
            assertEquals("Jozef", resultSet.getString("meno"));
            assertEquals("Novak", resultSet.getString("priezvisko"));
            assertEquals("Bratislava", resultSet.getString("adresa"));
        }
    }

    @Test
    void testJeEmailPouzity() {
        LocalDate datumNarodenia = LocalDate.of(1990, 5, 15);
        LocalDate pridanyDoEvidencie = LocalDate.of(2023, 1, 1);

        memoryRybarDAO.insertUser(connection, "Jozef", "Novak", "Bratislava", "AB123456",
                "Slovak", datumNarodenia, pridanyDoEvidencie, null, "jozef.novak@example.com",
                BCrypt.hashpw("password123", BCrypt.gensalt()));

        assertTrue(memoryRybarDAO.jeEmailPouzity("jozef.novak@example.com"));
        assertFalse(memoryRybarDAO.jeEmailPouzity("nonexistent@example.com"));
    }

    @Test
    void testOveritPouzivatela() {
        // Príprava údajov
        LocalDate datumNarodenia = LocalDate.of(1990, 5, 15);
        LocalDate pridanyDoEvidencie = LocalDate.of(2023, 1, 1);
        String spravneHeslo = "password123";
        String nespravneHeslo = "wrongpassword";
        String hashovaneHeslo = BCrypt.hashpw(spravneHeslo, BCrypt.gensalt());

        // Vloženie používateľa do databázy
        memoryRybarDAO.insertUser(connection, "Jozef", "Novak", "Bratislava", "AB123456",
                "Slovak", datumNarodenia, pridanyDoEvidencie, null, "jozef.novak@example.com",
                hashovaneHeslo);

        // Overenie správnych údajov
        assertTrue(memoryRybarDAO.overitPouzivatela("jozef.novak@example.com", spravneHeslo),
                "Overenie správnych údajov by malo vrátiť true");

        // Overenie nesprávneho hesla
        assertFalse(memoryRybarDAO.overitPouzivatela("jozef.novak@example.com", nespravneHeslo),
                "Overenie nesprávneho hesla by malo vrátiť false");

        // Overenie neexistujúceho e-mailu
        assertFalse(memoryRybarDAO.overitPouzivatela("neexistujuci.email@example.com", spravneHeslo),
                "Overenie neexistujúceho e-mailu by malo vrátiť false");

        // Overenie neexistujúceho e-mailu s nesprávnym heslom
        assertFalse(memoryRybarDAO.overitPouzivatela("neexistujuci.email@example.com", nespravneHeslo),
                "Overenie neexistujúceho e-mailu s nesprávnym heslom by malo vrátiť false");
    }


    @Test
    void testGetRybarNameById() {
        LocalDate datumNarodenia = LocalDate.of(1990, 5, 15);
        LocalDate pridanyDoEvidencie = LocalDate.of(2023, 1, 1);

        memoryRybarDAO.insertUser(connection, "Jozef", "Novak", "Bratislava", "AB123456",
                "Slovak", datumNarodenia, pridanyDoEvidencie, null, "jozef.novak@example.com",
                BCrypt.hashpw("password123", BCrypt.gensalt()));

        int id = memoryRybarDAO.getUserIdByEmail("jozef.novak@example.com");
        assertEquals("Jozef Novak", memoryRybarDAO.getRybarNameById(id));
        assertNull(memoryRybarDAO.getRybarNameById(999));
    }
}
