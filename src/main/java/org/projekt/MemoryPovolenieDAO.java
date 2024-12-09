package org.projekt;

import java.util.ArrayList;
import java.util.List;

public class MemoryPovolenieDAO implements PovolenieDAO {
    private List<Povolenie> povolenia = new ArrayList<>();
    private long posledneID = 0;

    @Override
    public List<Povolenie> getAll() {
        return new ArrayList<>(this.povolenia); // Vráti kópiu zoznamu pre imunitu voči úpravám
    }

    @Override
    public void save(Povolenie povolenie) {
        if (povolenie == null) {
            throw new IllegalArgumentException("Povolenie nesmie byť null");
        }

        // Overenie povinných atribútov
        if (povolenie.getPlatnostOd() == null || povolenie.getPlatnostDo() == null || povolenie.getRybarIdRybara() <= 0) {
            throw new IllegalArgumentException("Platnosť od, platnosť do a ID rybára sú povinné");
        }

        if (povolenie.getIdPovolenie() == 0) {
            // Ak povolenie nemá ID, pridáme nový záznam
            povolenie.setIdPovolenie(++this.posledneID);
            this.povolenia.add(povolenie);
        } else {
            // Ak povolenie má ID, pokúsime sa aktualizovať existujúci záznam
            boolean found = false;
            for (int i = 0; i < povolenia.size(); i++) {
                if (povolenia.get(i).getIdPovolenie() == povolenie.getIdPovolenie()) {
                    povolenia.set(i, povolenie); // Aktualizujeme existujúce povolenie
                    found = true;
                    break;
                }
            }

            if (!found) {
                throw new IllegalArgumentException("Povolenie s ID " + povolenie.getIdPovolenie() + " neexistuje");
            }
        }
    }

    @Override
    public void vymazPodlaID(long id) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID musí byť väčšie ako 0");
        }

        boolean removed = povolenia.removeIf(povolenie -> povolenie.getIdPovolenie() == id);

        if (!removed) {
            throw new IllegalArgumentException("Povolenie s ID " + id + " neexistuje");
        }
    }

    @Override
    public List<Povolenie> najdiPodlaRybarID(long rybarId) {
        if (rybarId <= 0) {
            throw new IllegalArgumentException("ID rybára musí byť väčšie ako 0");
        }

        List<Povolenie> vysledky = new ArrayList<>();
        for (Povolenie povolenie : this.povolenia) {
            if (povolenie.getRybarIdRybara() == rybarId) {
                vysledky.add(povolenie);
            }
        }
        return vysledky;
    }
}
