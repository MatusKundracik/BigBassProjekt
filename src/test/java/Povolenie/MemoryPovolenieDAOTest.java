package Povolenie;

import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class MemoryPovolenieDAOTest {

    private static Connection connection;
    private MemoryPovolenieDAO memoryPovolenieDAO;

    @BeforeAll
    static void setUpDatabase() throws SQLException {
        connection = DriverManager.getConnection("jdbc:sqlite::memory:");
        String createTableQuery = "CREATE TABLE povolenie (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "platnost_od TEXT, " +
                "platnost_do TEXT, " +
                "pstruhove INTEGER, " +
                "lipňove INTEGER, " +
                "kaprové INTEGER, " +
                "rybar_id_rybara INTEGER)";
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
        memoryPovolenieDAO = new MemoryPovolenieDAO();
    }

    @AfterEach
    void cleanUp() throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement("DELETE FROM povolenie")) {
            statement.executeUpdate();
        }
    }

    @Test
    void testInsertPovolenie() throws SQLException {
        Povolenie povolenie = new Povolenie();
        povolenie.setPlatnostOd(LocalDate.parse("2024-01-01"));
        povolenie.setPlatnostDo(LocalDate.parse("2024-12-31"));
        povolenie.setPstruhove(true);
        povolenie.setLipnove(false);
        povolenie.setKaprove(true);
        povolenie.setRybarIdRybara(1);

        memoryPovolenieDAO.insertPovolenie(connection, povolenie);

        String query = "SELECT * FROM povolenie WHERE rybar_id_rybara = 1";
        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            assertTrue(resultSet.next());
            assertEquals("2024-01-01", resultSet.getString("platnost_od"));
            assertEquals("2024-12-31", resultSet.getString("platnost_do"));
            assertEquals(1, resultSet.getInt("pstruhove"));
            assertEquals(0, resultSet.getInt("lipňove"));
            assertEquals(1, resultSet.getInt("kaprové"));
            assertEquals(1, resultSet.getInt("rybar_id_rybara"));
        }
    }

    @Test
    void testGeneratePovolenieMessage() {
        String message = memoryPovolenieDAO.generatePovolenieMessage(1, true, false, true);
        assertEquals("Rybárovi 1 bolo pridané kaprové, pstruhové povolenie.", message);

        message = memoryPovolenieDAO.generatePovolenieMessage(2, false, false, false);
        assertEquals("Rybárovi 2 bolo pridané žiadne povolenie.", message);

        message = memoryPovolenieDAO.generatePovolenieMessage(3, false, true, false);
        assertEquals("Rybárovi 3 bolo pridané lipňové povolenie.", message);
    }

    @Test
    void insertPovolenie() {
    }

    @Test
    void generatePovolenieMessage() {
    }
}
