package org.projekt;

import java.util.ArrayList;
import java.util.List;

public class MemoryUlovokDAO implements UlovokDAO {
    private List<Ulovok> ulovky = new ArrayList<>();
    private int posledneID = 0;

    @Override
    public List<Ulovok> getAll() {
        return new ArrayList<>(this.ulovky);
    }

    @Override
    public void save(Ulovok ulovok) {
        if (ulovok == null) {
            throw new IllegalArgumentException("Ulovok nesmie byť null");
        }


        if (ulovok.getDatumUlovku() == null || ulovok.getDruhRyby() == null || ulovok.getCisloReviru() == null) {
            throw new IllegalArgumentException("Dátum úlovku, druh ryby a číslo revíru sú povinné");
        }

        if (ulovok.getIdUlovok() == 0) {

            ulovok.setIdUlovok(++this.posledneID);
            this.ulovky.add(ulovok);
        } else {

            boolean found = false;
            for (int i = 0; i < ulovky.size(); i++) {
                if (ulovky.get(i).getIdUlovok() == ulovok.getIdUlovok()) {
                    ulovky.set(i, ulovok);
                    found = true;
                    break;
                }
            }

            if (!found) {
                throw new IllegalArgumentException("Ulovok s ID " + ulovok.getIdUlovok() + " neexistuje");
            }
        }
    }

    @Override
    public void vymazPodlaID(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID musí byť väčšie ako 0");
        }

        boolean removed = ulovky.removeIf(ulovok -> ulovok.getIdUlovok() == id);

        if (!removed) {
            throw new IllegalArgumentException("Ulovok s ID " + id + " neexistuje");
        }
    }
}
