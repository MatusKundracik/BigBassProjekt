package org.projekt;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SpravaRybaraTest {
    private SpravaRybara zoznamRybarov;

    @BeforeEach
    void setUp() {
        zoznamRybarov = new SpravaRybara();
    }

    @Test
    void vypisRybarov() {
        Rybar rybar = new Rybar();
        rybar.setRybarId(1);
        rybar.setMeno("Jozef");
        rybar.setPriezvisko("Novák");
        rybar.setAdresa("Bratislava");
        zoznamRybarov.pridatRybara(rybar);

        assertDoesNotThrow(zoznamRybarov::vypisRybarov);
    }

    @Test
    void pridatRybaraTest() {
        int count = zoznamRybarov.pocetRybarov();

        Rybar rybar = new Rybar();
        rybar.setRybarId(1);
        rybar.setMeno("Jozef");
        rybar.setPriezvisko("Novák");
        rybar.setAdresa("Bratislava");

        zoznamRybarov.pridatRybara(rybar);
        assertEquals(count + 1, zoznamRybarov.pocetRybarov());
    }

    @Test
    void odobratRybara() {
        Rybar rybar = new Rybar();
        rybar.setRybarId(1);
        zoznamRybarov.pridatRybara(rybar);

        int count = zoznamRybarov.pocetRybarov();
        zoznamRybarov.odobratRybara(1);

        assertEquals(count - 1, zoznamRybarov.pocetRybarov());
    }

    @Test
    void zmenitMeno() {
        Rybar rybar = new Rybar();
        rybar.setRybarId(1);
        rybar.setMeno("Jozef");
        zoznamRybarov.pridatRybara(rybar);

        zoznamRybarov.zmenitMeno(1, "Marek");

        assertEquals("Marek", rybar.getMeno());
    }

    @Test
    void zmenitPriezvisko() {
        Rybar rybar = new Rybar();
        rybar.setRybarId(1);
        rybar.setPriezvisko("Novák");
        zoznamRybarov.pridatRybara(rybar);

        zoznamRybarov.zmenitPriezvisko(1, "Horváth");

        assertEquals("Horváth", rybar.getPriezvisko());
    }

    @Test
    void zmenaAdresy() {
        Rybar rybar = new Rybar();
        rybar.setRybarId(1);
        rybar.setAdresa("Bratislava");
        zoznamRybarov.pridatRybara(rybar);

        zoznamRybarov.zmenaAdresy(1, "Košice");

        assertEquals("Košice", rybar.getAdresa());
    }
}
