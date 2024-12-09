package org.projekt;

import java.util.ArrayList;
import java.util.List;

public class MemoryRevirDAO implements RevirDAO {
    private List<Revir> reviry = new ArrayList<>();
    private long posledneID = 0;

    @Override
    public List<Revir> getAll() {
        return new ArrayList<>(this.reviry); // Vráti kópiu zoznamu pre imunitu voči úpravám
    }

    @Override
    public void save(Revir revir) {
        if (revir == null) {
            throw new IllegalArgumentException("Revír nesmie byť null");
        }

        // Overenie povinných atribútov
        if (revir.getNazov() == null || revir.getLokalita() == null || revir.getCisloReviru() <= 0) {
            throw new IllegalArgumentException("Názov, lokalita a číslo revíru sú povinné");
        }

        if (revir.getIdRevir() == 0) {
            // Ak revír nemá ID, pridáme nový záznam
            revir.setIdRevir(++this.posledneID);
            this.reviry.add(revir);
        } else {
            // Ak revír má ID, pokúsime sa aktualizovať existujúci záznam
            boolean found = false;
            for (int i = 0; i < reviry.size(); i++) {
                if (reviry.get(i).getIdRevir() == revir.getIdRevir()) {
                    reviry.set(i, revir); // Aktualizujeme existujúci záznam
                    found = true;
                    break;
                }
            }

            if (!found) {
                throw new IllegalArgumentException("Revír s ID " + revir.getIdRevir() + " neexistuje");
            }
        }
    }

    @Override
    public void vymazPodlaID(long id) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID musí byť väčšie ako 0");
        }

        boolean removed = reviry.removeIf(revir -> revir.getIdRevir() == id);

        if (!removed) {
            throw new IllegalArgumentException("Revír s ID " + id + " neexistuje");
        }
    }

    @Override
    public List<Revir> najdiPodlaLokalita(String lokalita) {
        if (lokalita == null || lokalita.isEmpty()) {
            throw new IllegalArgumentException("Lokalita nesmie byť null alebo prázdna");
        }

        List<Revir> vysledky = new ArrayList<>();
        for (Revir revir : this.reviry) {
            if (revir.getLokalita() != null && revir.getLokalita().contains(lokalita)) {
                vysledky.add(revir);
            }
        }
        return vysledky;
    }
}
