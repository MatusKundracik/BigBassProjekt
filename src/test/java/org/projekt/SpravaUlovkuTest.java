package org.projekt;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class SpravaUlovkuTest {

    private SpravaUlovku zoznamUlovku;

    @BeforeEach
    void setUp() {
        zoznamUlovku = new SpravaUlovku();
        zoznamUlovku.setZoznamUlovkov(new ArrayList<>());
    }

    @Test
    void pridajUlovok() {
        Ulovok ulovok = new Ulovok();
        ulovok.setIdUlovok(1);
        ulovok.setDruhRyby("Kapor");
        ulovok.setHmotnostVkg(5);

        zoznamUlovku.pridajUlovok(ulovok);

        assertEquals(1, zoznamUlovku.getZoznamUlovkov().size());
        assertTrue(zoznamUlovku.getZoznamUlovkov().contains(ulovok));
    }

    @Test
    void vypisUlovkov() {
        assertTrue(zoznamUlovku.vypisUlovkov().isEmpty());

        Ulovok ulovok = new Ulovok();
        ulovok.setIdUlovok(1);
        ulovok.setDruhRyby("Sumec");

        zoznamUlovku.pridajUlovok(ulovok);

        assertEquals(1, zoznamUlovku.vypisUlovkov().size());
        assertEquals("Sumec", zoznamUlovku.vypisUlovkov().get(0).getDruhRyby());
    }

    @Test
    void getUlovok() {
        Ulovok ulovok1 = new Ulovok();
        ulovok1.setIdUlovok(1);
        ulovok1.setDruhRyby("Pstruh");

        Ulovok ulovok2 = new Ulovok();
        ulovok2.setIdUlovok(2);
        ulovok2.setDruhRyby("Šťuka");

        zoznamUlovku.pridajUlovok(ulovok1);
        zoznamUlovku.pridajUlovok(ulovok2);

        Ulovok result = zoznamUlovku.getUlovok(2);

        assertNotNull(result);
        assertEquals("Šťuka", result.getDruhRyby());
    }

    @Test
    void getZoznamUlovkov() {
        assertNotNull(zoznamUlovku.getZoznamUlovkov());
        assertTrue(zoznamUlovku.getZoznamUlovkov().isEmpty());

        Ulovok ulovok = new Ulovok();
        ulovok.setIdUlovok(1);

        zoznamUlovku.pridajUlovok(ulovok);

        assertEquals(1, zoznamUlovku.getZoznamUlovkov().size());
    }

    @Test
    void setZoznamUlovkov() {
        ArrayList<Ulovok> novyZoznam = new ArrayList<>();
        Ulovok ulovok = new Ulovok();
        ulovok.setIdUlovok(1);
        novyZoznam.add(ulovok);

        zoznamUlovku.setZoznamUlovkov(novyZoznam);

        assertEquals(novyZoznam, zoznamUlovku.getZoznamUlovkov());
    }

    @Test
    void testEquals() {
        SpravaUlovku sprava1 = new SpravaUlovku();
        SpravaUlovku sprava2 = new SpravaUlovku();

        assertEquals(sprava1, sprava2);
    }

    @Test
    void canEqual() {
        SpravaUlovku sprava1 = new SpravaUlovku();
        SpravaUlovku sprava2 = new SpravaUlovku();

        assertTrue(sprava1.canEqual(sprava2));
    }

    @Test
    void testHashCode() {
        SpravaUlovku sprava1 = new SpravaUlovku();
        SpravaUlovku sprava2 = new SpravaUlovku();

        assertEquals(sprava1.hashCode(), sprava2.hashCode());
    }

    @Test
    void testToString() {
        SpravaUlovku sprava = new SpravaUlovku();
        String result = sprava.toString();

        assertNotNull(result);
        assertTrue(result.contains("zoznamUlovkov"));
    }
}
