package Ulovok;

import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class MemoryUlovokDAOTest {

    private static Connection connection;
    private MemoryUlovokDAO memoryUlovokDAO;

    @BeforeAll
    static void setUpDatabase() throws SQLException {
        // Vytvorenie pamäťovej databázy
        connection = DriverManager.getConnection("jdbc:sqlite::memory:");

        // Vytvorenie tabuľky ulovok
        String createUlovokTable = " CREATE TABLE ulovok ( id_ulovok INTEGER PRIMARY KEY, datum DATE NOT NULL, cislo_reviru INTEGER NOT NULL, druh_ryby TEXT NOT NULL, dlzka_v_cm REAL NOT NULL, hmotnost_v_kg REAL NOT NULL, kontrola INTEGER NULL, povolenie_id_povolenie INTEGER NOT NULL, povolenie_rybar_id_rybara INTEGER NOT NULL, revir_id_revira INTEGER NOT NULL)";
        try (PreparedStatement statement = connection.prepareStatement(createUlovokTable)) {
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
        memoryUlovokDAO = new MemoryUlovokDAO();
    }

    @AfterEach
    void cleanUp() throws SQLException {
        // Vymazanie všetkých údajov z tabuľky ulovok
        try (PreparedStatement statement = connection.prepareStatement("DELETE FROM ulovok")) {
            statement.executeUpdate();
        }
    }

    @Test
    void testInsertUlovok() throws SQLException {
        // Kontrola, či je tabuľka 'revir' správne inicializovaná
        String checkRevirSQL = "SELECT * FROM revir WHERE nazov = ?";
        try (PreparedStatement statement = connection.prepareStatement(checkRevirSQL)) {
            statement.setString(1, "123");
            try (ResultSet resultSet = statement.executeQuery()) {
                assertTrue(resultSet.next(), "Tabuľka 'revir' neobsahuje očakávaný záznam.");
            }
        }

        // Vytvorenie a vloženie testovacieho objektu
        Ulovok ulovok = new Ulovok();
        ulovok.setDatumUlovku(LocalDate.parse("2024-06-01"));
        ulovok.setCisloReviru("123");
        ulovok.setDruhRyby("Kapor");
        ulovok.setDlzkaVcm(50.5);
        ulovok.setHmotnostVkg(3.2);
        ulovok.setKontrola(1);

        memoryUlovokDAO.insertUlovok(connection, ulovok, "123");

        // Kontrola vložených údajov
        String query = "SELECT * FROM ulovok WHERE cislo_reviru = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, "123");
            try (ResultSet resultSet = statement.executeQuery()) {
                assertTrue(resultSet.next(), "Záznam v tabuľke 'ulovok' nebol nájdený.");
                assertEquals("2024-06-01", resultSet.getString("datum"));
                assertEquals("123", resultSet.getString("cislo_reviru"));
                assertEquals("Kapor", resultSet.getString("druh_ryby"));
                assertEquals(50.5, resultSet.getDouble("dlzka_v_cm"), 0.01);
                assertEquals(3.2, resultSet.getDouble("hmotnost_v_kg"), 0.01);
                assertEquals(1, resultSet.getInt("kontrola"));
            }
        }
    }

}
