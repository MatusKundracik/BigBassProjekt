package Ulovok;

import org.junit.jupiter.api.*;
import org.projekt.Session;

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
        connection = DriverManager.getConnection("jdbc:sqlite::memory:");

        String createPovolenieTableQuery = "CREATE TABLE povolenie (" +
                "id_povolenie INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "platnost_od TEXT, " +
                "platnost_do TEXT, " +
                "pstruhove INTEGER, " +
                "lipňove INTEGER, " +
                "kaprové INTEGER, " +
                "rybar_id_rybara INTEGER)";

        String createRevirTableQuery = "CREATE TABLE revir (" +
                "id_revira INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "nazov TEXT)";

        String createUlovokTableQuery = "CREATE TABLE ulovok (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "datum TEXT, " +
                "cislo_reviru TEXT, " +
                "druh_ryby TEXT, " +
                "dlzka_v_cm REAL, " +
                "hmotnost_v_kg REAL, " +
                "povolenie_id_povolenie INTEGER, " +
                "povolenie_rybar_id_rybara INTEGER, " +
                "revir_id_revira INTEGER, " +
                "kontrola TEXT)";

        try (PreparedStatement createPovolenieTable = connection.prepareStatement(createPovolenieTableQuery);
             PreparedStatement createRevirTable = connection.prepareStatement(createRevirTableQuery);
             PreparedStatement createUlovokTable = connection.prepareStatement(createUlovokTableQuery)) {

            createPovolenieTable.execute();
            createRevirTable.execute();
            createUlovokTable.execute();
        }
    }

    @AfterAll
    static void tearDownDatabase() throws SQLException {
        if (connection != null) {
            connection.close();
        }
    }


    @AfterEach
    void cleanUp() throws SQLException {
        try (PreparedStatement deleteUlovok = connection.prepareStatement("DELETE FROM ulovok");
             PreparedStatement deletePovolenie = connection.prepareStatement("DELETE FROM povolenie");
             PreparedStatement deleteRevir = connection.prepareStatement("DELETE FROM revir")) {

            deleteUlovok.executeUpdate();
            deletePovolenie.executeUpdate();
            deleteRevir.executeUpdate();
        }
    }

    @Test
    void testInsertUlovok() throws SQLException {
        // Insert test data for Povolenie and Revir
        try (PreparedStatement insertPovolenie = connection.prepareStatement("INSERT INTO povolenie (id_povolenie, platnost_od, platnost_do, pstruhove, lipňove, kaprové, rybar_id_rybara) VALUES (1, '2024-01-01', '2024-12-31', 1, 0, 1, 1)");
             PreparedStatement insertRevir = connection.prepareStatement("INSERT INTO revir (id_revira, nazov) VALUES (1, '1234')")) {

            insertPovolenie.executeUpdate();
            insertRevir.executeUpdate();
        }

        Ulovok ulovok = new Ulovok(LocalDate.of(2024, 6, 15), "1234", "Pstruh", 45.0, 1.2, 1);
        Session.aktualnyRybarId = 1;

        memoryUlovokDAO.insertUlovok(ulovok);

        String query = "SELECT * FROM ulovok WHERE cislo_reviru = '1234'";
        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            assertTrue(resultSet.next());
            assertEquals("2024-06-15", resultSet.getString("datum"));
            assertEquals("1234", resultSet.getString("cislo_reviru"));
            assertEquals("Pstruh", resultSet.getString("druh_ryby"));
            assertEquals(45.0, resultSet.getDouble("dlzka_v_cm"));
            assertEquals(1.2, resultSet.getDouble("hmotnost_v_kg"));
            assertEquals(1, resultSet.getInt("povolenie_id_povolenie"));
            assertEquals(1, resultSet.getInt("povolenie_rybar_id_rybara"));
            assertEquals(1, resultSet.getInt("revir_id_revira"));
        }
    }
}