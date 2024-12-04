package org.projekt;

import lombok.var;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class MemoryRybarDAOTest {

    private MemoryRybarDAO rybarDAO;
    private Rybar rybar1;
    private Rybar rybar2;

    @BeforeEach
    void setUp() {
        rybarDAO = new MemoryRybarDAO();
        rybar1 = new Rybar(0, "Jozef", "Novak", LocalDate.of(1990, 1, 1), "Adresa 1", "Slovensko", "123456", 123, 1, LocalDate.now(), null);
        rybar2 = new Rybar(0, "Martin", "Kovač", LocalDate.of(1985, 5, 15), "Adresa 2", "Slovensko", "654321", 456, 1, LocalDate.now(), null);
    }

    @Test
    void testSave_NewRybar() {
        rybarDAO.save(rybar1);
        assertNotNull(rybar1.getRybarId(), "Rybár by mal mať priradené ID po uložení");
    }

    @Test
    void testSave_ExistingRybar() {
        rybarDAO.save(rybar1);
        rybar1.setMeno("Jozef Updated");
        rybarDAO.save(rybar1);

        Rybar updatedRybar = rybarDAO.getAll().stream()
                .filter(rybar -> rybar.getRybarId() == rybar1.getRybarId())
                .findFirst().orElse(null);

        assertNotNull(updatedRybar, "Rybár by mal byť aktualizovaný");
        assertEquals("Jozef Updated", updatedRybar.getMeno(), "Meno by malo byť aktualizované");
    }

    @Test
    void testSave_InvalidRybar() {
        Rybar invalidRybar = new Rybar(0, null, null, null, null, null, null, 0, 0, null, null);
        assertThrows(IllegalArgumentException.class, () -> rybarDAO.save(invalidRybar), "Mal by sa hodiť výnimka pri uložení neplatného rybára");
    }


    @Test
    void testFindByName() {
        rybarDAO.save(rybar1);
        rybarDAO.save(rybar2);

        var foundRybari = rybarDAO.najdiPodlaMena("Martin");

        assertEquals(1, foundRybari.size(), "Mal by sa nájsť 1 rybár podľa mena");
        assertEquals("Martin", foundRybari.get(0).getMeno(), "Meno nájdeného rybára by malo byť Martin");
    }

    @Test
    void testFindByName_NoResults() {
        rybarDAO.save(rybar1);

        var foundRybari = rybarDAO.najdiPodlaMena("Peter");

        assertTrue(foundRybari.isEmpty(), "Výsledok hľadania by mal byť prázdny pre neexistujúce meno");
    }

    @Test
    void testDeleteRybar() {
        rybarDAO.save(rybar1);
        rybarDAO.save(rybar2);

        rybarDAO.vymazPodlaID(rybar1.getRybarId());

        var foundRybari = rybarDAO.getAll();
        assertEquals(1, foundRybari.size(), "Po vymazaní by mal zostať len jeden rybár");
    }

    @Test
    void testDeleteRybar_NotFound() {
        rybarDAO.save(rybar1);
        assertThrows(IllegalArgumentException.class, () -> rybarDAO.vymazPodlaID(999), "Mal by sa hodiť výnimka pri pokuse o vymazanie neexistujúceho rybára");
    }
}
