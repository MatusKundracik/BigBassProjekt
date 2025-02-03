package Revir;

import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class SQLRevirDAOTest {

    private static Connection connection;
    private SQLRevirDAO SQLRevirDAO;

    @BeforeAll
    static void setUpDatabase() throws SQLException {
        connection = DriverManager.getConnection("jdbc:sqlite::memory:");

        String createRevirTableQuery = " CREATE TABLE IF NOT EXISTS revir ( id_revira INTEGER PRIMARY KEY AUTOINCREMENT, nazov TEXT NOT NULL, lokalita TEXT NOT NULL, popis TEXT NOT NULL, kaprove INTEGER NOT NULL, lipnove INTEGER NOT NULL, pstruhove INTEGER NOT NULL) ";

        try (PreparedStatement statement = connection.prepareStatement(createRevirTableQuery)) {
            statement.execute();
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
        try (PreparedStatement statement = connection.prepareStatement("DELETE FROM revir")) {
            statement.executeUpdate();
        }
    }

    @Test
    void testInsertRevir() throws SQLException {
        Revir revir = new Revir();
        revir.setNazov("Revír Test");
        revir.setLokalita("Lokalita A");
        revir.setPopis("Popis pre revír.");
        revir.setKaprove(true);
        revir.setLipnove(false);
        revir.setPstruhove(true);

        SQLRevirDAO.insertRevir(revir);

        String query = "SELECT * FROM revir WHERE nazov = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, "Revír Test");
            try (ResultSet resultSet = statement.executeQuery()) {
                assertTrue(resultSet.next());
                assertEquals("Revír Test", resultSet.getString("nazov"));
                assertEquals("Lokalita A", resultSet.getString("lokalita"));
                assertEquals("Popis pre revír.", resultSet.getString("popis"));
                assertEquals(1, resultSet.getInt("kaprove"));
                assertEquals(0, resultSet.getInt("lipnove"));
                assertEquals(1, resultSet.getInt("pstruhove"));
            }
        }
    }

    @Test
    void testGetRevirIdByName() throws SQLException {
        // Vloženie testovacích údajov do tabuľky revir
        String insertQuery = " INSERT INTO revir (nazov, lokalita, popis, kaprove, lipnove, pstruhove) VALUES ('Revír 1', 'Lokalita 1', 'Popis 1', 1, 0, 1) ";
        try (PreparedStatement statement = connection.prepareStatement(insertQuery)) {
            statement.executeUpdate();
        }

        int id = SQLRevirDAO.getRevirIdByName("Revír 1");
        assertTrue(id > 0); // Overenie, že id je platné

        // Pokus o získanie ID pre neexistujúci názov
        Exception exception = assertThrows(RuntimeException.class, () -> {
            SQLRevirDAO.getRevirIdByName("Neexistujúci Revír");
        });

        assertEquals("Revír nenájdený s názvom: Neexistujúci Revír", exception.getMessage());
    }
}
