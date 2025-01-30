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
        // Pripojenie k databáze v pamäti
        connection = DriverManager.getConnection("jdbc:sqlite::memory:");

        // Vytvorenie testovacej tabuľky
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
                "email TEXT, " +
                "heslo TEXT)";
        try (PreparedStatement statement = connection.prepareStatement(createTableQuery)) {
            statement.execute();
        }
    }

    @AfterAll
    static void tearDownDatabase() throws SQLException {
        // Uzavretie pripojenia po všetkých testoch
        if (connection != null) {
            connection.close();
        }
    }

    @AfterEach
    void cleanUp() throws SQLException {
        // Vymazanie všetkých údajov v tabuľke po každom teste
        try (PreparedStatement statement = connection.prepareStatement("DELETE FROM rybar")) {
            statement.executeUpdate();
        }
    }

    @Test
    void testInsertUser() throws SQLException {
        // Vytvorenie nového rybára
        memoryRybarDAO.insertUser(connection, "Janko", "Hrasko", "Ulica 123", "123456789", "Slovenská republika",
                LocalDate.of(1990, 5, 1), LocalDate.now(), null, "janko@hrasko.sk", "heslo");

        // Overenie, že bol záznam vložený
        String sql = "SELECT * FROM rybar WHERE email = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, "janko@hrasko.sk");
            try (ResultSet rs = stmt.executeQuery()) {
                assertTrue(rs.next());
                assertEquals("Janko", rs.getString("meno"));
                assertEquals("Hrasko", rs.getString("priezvisko"));
                assertEquals("janko@hrasko.sk", rs.getString("email"));
            }
        }
    }

    @Test
    void testJeEmailPouzity() throws SQLException {
        // Vytvorenie nového rybára
        memoryRybarDAO.insertUser(connection, "Janko", "Hrasko", "Ulica 123", "123456789", "Slovenská republika",
                LocalDate.of(1990, 5, 1), LocalDate.now(), null, "janko@hrasko.sk", "heslo");

        // Testovanie, že email je použitý
        boolean emailExistuje = memoryRybarDAO.jeEmailPouzity(connection, "janko@hrasko.sk");
        assertTrue(emailExistuje);

        // Testovanie, že iný email nie je použitý
        boolean emailNeexistuje = memoryRybarDAO.jeEmailPouzity(connection, "neexistujuci@hrasko.sk");
        assertFalse(emailNeexistuje);
    }

    @Test
    void testGetUserIdByEmail() throws SQLException {
        // Vytvorenie nového rybára
        memoryRybarDAO.insertUser(connection, "Janko", "Hrasko", "Ulica 123", "123456789", "Slovenská republika",
                LocalDate.of(1990, 5, 1), LocalDate.now(), null, "janko@hrasko.sk", "heslo");

        // Testovanie získania ID podľa emailu
        int userId = memoryRybarDAO.getUserIdByEmail(connection, "janko@hrasko.sk");
        assertTrue(userId > 0);
    }

    @Test
    void testOveritPouzivatela() throws SQLException {
        // Vytvorenie nového rybára s heslom
        String email = "janko@hrasko.sk";
        String heslo = BCrypt.hashpw("heslo", BCrypt.gensalt());
        memoryRybarDAO.insertUser(connection, "Janko", "Hrasko", "Ulica 123", "123456789", "Slovenská republika",
                LocalDate.of(1990, 5, 1), LocalDate.now(), null, email, heslo);

        // Testovanie správneho overenia používateľa
        boolean overeny = memoryRybarDAO.overitPouzivatela(connection, email, "heslo");
        assertTrue(overeny);

        // Testovanie nesprávneho overenia používateľa
        boolean neovereny = memoryRybarDAO.overitPouzivatela(connection, email, "nespravneHeslo");
        assertFalse(neovereny);
    }

    @Test
    void testGetRybarNameById() throws SQLException {
        // Vytvorenie nového rybára
        memoryRybarDAO.insertUser(connection, "Janko", "Hrasko", "Ulica 123", "123456789", "Slovenská republika",
                LocalDate.of(1990, 5, 1), LocalDate.now(), null, "janko@hrasko.sk", "heslo");

        // Získanie ID používateľa
        int userId = memoryRybarDAO.getUserIdByEmail(connection, "janko@hrasko.sk");

        // Testovanie získania mena používateľa
        String meno = memoryRybarDAO.getRybarNameById(connection, userId);
        assertEquals("Janko Hrasko", meno);
    }
}