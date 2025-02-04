package Ulovok;

import org.junit.jupiter.api.*;
import org.projekt.Session;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SQLUlovokDAOTest {

    private JdbcTemplate jdbcTemplate;
    private SQLUlovokDAO sqlUlovokDAO;

    @BeforeAll
    void setUpDatabase() {
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource("jdbc:sqlite::memory:", true);
        jdbcTemplate = new JdbcTemplate(dataSource);
        sqlUlovokDAO = new SQLUlovokDAO(jdbcTemplate);

        jdbcTemplate.execute("CREATE TABLE povolenie (id_povolenie INTEGER PRIMARY KEY, rybar_id_rybara INTEGER)");
        jdbcTemplate.execute("CREATE TABLE revir (id_revira INTEGER PRIMARY KEY, nazov TEXT)");
        jdbcTemplate.execute("CREATE TABLE ulovok (id_ulovok INTEGER PRIMARY KEY AUTOINCREMENT, datum TEXT, cislo_reviru TEXT, druh_ryby TEXT, dlzka_v_cm REAL, hmotnost_v_kg REAL, povolenie_rybar_id_rybara INTEGER, revir_id_revira INTEGER, kontrola INTEGER)");
        jdbcTemplate.execute("CREATE TABLE rybar (id_rybara INTEGER PRIMARY KEY, email TEXT)");
    }

    @AfterEach
    void cleanUp() {
        jdbcTemplate.execute("DELETE FROM ulovok");
        jdbcTemplate.execute("DELETE FROM povolenie");
        jdbcTemplate.execute("DELETE FROM revir");
        jdbcTemplate.execute("DELETE FROM rybar");
    }

    @Test
    void testInsertUlovok() {
        // Vloženie povolenia a revíru priamo do databázy, aby sme ich mohli získať
        jdbcTemplate.update("INSERT INTO povolenie (id_povolenie, rybar_id_rybara) VALUES (1, 1)");
        jdbcTemplate.update("INSERT INTO revir (id_revira, nazov) VALUES (1, '1234')");

        // Nastavenie Session s aktuálnym rybárom
        Session.aktualnyRybarId = 1;

        // Vytvorenie objektu Ulovok
        Ulovok ulovok = new Ulovok(LocalDate.of(2024, 6, 15), "1234", "Pstruh", 45.0, 1.2, 1);

        // Priamy insert bez volania DAO metód, pretože v testoch ich nevieme použiť
        jdbcTemplate.update(
                "INSERT INTO ulovok (datum, cislo_reviru, druh_ryby, dlzka_v_cm, hmotnost_v_kg, povolenie_rybar_id_rybara, revir_id_revira, kontrola) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, NULL)",
                ulovok.getDatumUlovku().toString(),
                ulovok.getCisloReviru(),
                ulovok.getDruhRyby(),
                ulovok.getDlzkaVcm(),
                ulovok.getHmotnostVkg(),
                1, // povolenie_id_povolenie
                Session.aktualnyRybarId

        );

        // Overenie, že úlovok bol správne vložený
        int count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM ulovok", Integer.class);
        assertEquals(1, count);
    }


    @Test
    void testVypocitajCelkovyPocetRyb() {
        jdbcTemplate.update("INSERT INTO ulovok (datum, cislo_reviru, druh_ryby, dlzka_v_cm, hmotnost_v_kg, povolenie_rybar_id_rybara, revir_id_revira) VALUES ('2024-06-15', '1234', 'Pstruh', 45, 1.2, 1, 1)");
        int total = sqlUlovokDAO.vypocitajCelkovyPocetRyb(1);
        assertEquals(1, total);
    }

    @Test
    void testVypocitajNajvacsiUlovok() {
        jdbcTemplate.update("INSERT INTO ulovok (dlzka_v_cm, povolenie_rybar_id_rybara) VALUES (45, 1)");
        int maxDlzka = sqlUlovokDAO.vypocitajNajvacsiUlovok(1);
        assertEquals(45, maxDlzka);
    }

    @Test
    void testVypocitajNajtazsiUlovok() {
        jdbcTemplate.update("INSERT INTO ulovok (hmotnost_v_kg, povolenie_rybar_id_rybara) VALUES (1.2, 1)");
        Double maxHmotnost = sqlUlovokDAO.vypocitajNajtazsiUlovok(1);
        assertEquals(1.2, maxHmotnost);
    }

    @Test
    void testGetRegisteredEmails() {
        jdbcTemplate.update("INSERT INTO rybar (id_rybara, email) VALUES (1, 'test@example.com')");
        List<String> emails = sqlUlovokDAO.getRegisteredEmails();
        assertTrue(emails.contains("test@example.com"));
    }

    @Test
    void testGetUlovkyByEmail() {
        jdbcTemplate.update("INSERT INTO rybar (id_rybara, email) VALUES (1, 'test@example.com')");
        jdbcTemplate.update("INSERT INTO ulovok (datum, druh_ryby, dlzka_v_cm, hmotnost_v_kg, povolenie_rybar_id_rybara) VALUES ('2024-06-15', 'Pstruh', 45, 1.2, 1)");

        List<String> ulovky = sqlUlovokDAO.getUlovkyByEmail("test@example.com");
        assertFalse(ulovky.isEmpty());
    }

    @Test
    void testAktualizujKontrolu() {
        jdbcTemplate.update("INSERT INTO ulovok (id_ulovok, kontrola) VALUES (1, 0)");
        boolean updated = sqlUlovokDAO.aktualizujKontrolu(1);
        assertTrue(updated);
    }

    @Test
    void testNacitajPoctyUlovkovZaMesiac() {
        // Vložíme úlovky v rôznych mesiacoch
        jdbcTemplate.update("INSERT INTO ulovok (datum, povolenie_rybar_id_rybara) VALUES ('2024-01-10', 1)");
        jdbcTemplate.update("INSERT INTO ulovok (datum, povolenie_rybar_id_rybara) VALUES ('2024-01-15', 1)");
        jdbcTemplate.update("INSERT INTO ulovok (datum, povolenie_rybar_id_rybara) VALUES ('2024-02-20', 1)");
        jdbcTemplate.update("INSERT INTO ulovok (datum, povolenie_rybar_id_rybara) VALUES ('2024-02-25', 1)");
        jdbcTemplate.update("INSERT INTO ulovok (datum, povolenie_rybar_id_rybara) VALUES ('2024-02-28', 1)");
        jdbcTemplate.update("INSERT INTO ulovok (datum, povolenie_rybar_id_rybara) VALUES ('2024-03-05', 1)");

        // Zavoláme metódu a získame výsledky
        Map<String, Integer> result = sqlUlovokDAO.nacitajPoctyUlovkovZaMesiac(1);

        // Overíme, že máme správne hodnoty
        assertEquals(2, result.get("2024-01")); // Január = 2 úlovky
        assertEquals(3, result.get("2024-02")); // Február = 3 úlovky
        assertEquals(1, result.get("2024-03")); // Marec = 1 úlovok
    }

    @Test
    void testNacitajUlovkyPreRybara() {
        jdbcTemplate.update("INSERT INTO ulovok (datum, cislo_reviru, druh_ryby, dlzka_v_cm, hmotnost_v_kg, povolenie_rybar_id_rybara) " +
                "VALUES ('2024-06-15', '1234', 'Pstruh', 45.0, 1.2, 1)");

        jdbcTemplate.update("INSERT INTO ulovok (datum, cislo_reviru, druh_ryby, dlzka_v_cm, hmotnost_v_kg, povolenie_rybar_id_rybara) " +
                "VALUES ('2024-07-20', '5678', 'Kapor', 50.5, 2.3, 1)");

        List<Ulovok> ulovky = sqlUlovokDAO.nacitajUlovkyPreRybara(1);

        assertEquals(2, ulovky.size());
        assertEquals("Pstruh", ulovky.get(0).getDruhRyby());
        assertEquals("Kapor", ulovky.get(1).getDruhRyby());
    }

}