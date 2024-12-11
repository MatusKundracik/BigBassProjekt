package org.projekt;

import java.util.ArrayList;
import java.util.List;

public class MemoryRybarDAO implements RybarDAO {
    private List<Rybar> rybari = new ArrayList<>();
    private int posledneID = 0;

    @Override
    public List<Rybar> getAll() {
        return new ArrayList<>(this.rybari);
    }

    @Override
    public void save(Rybar rybar) {
        if (rybar == null) {
            throw new IllegalArgumentException("Rybar nesmie byť null");
        }


        if (rybar.getMeno() == null || rybar.getPriezvisko() == null || rybar.getDatumNarodenia() == null) {
            throw new IllegalArgumentException("Meno, priezvisko a dátum narodenia sú povinné");
        }

        if (rybar.getRybarId() == 0) {

            rybar.setRybarId(++this.posledneID);
            this.rybari.add(rybar);
        } else {

            boolean found = false;
            for (int i = 0; i < rybari.size(); i++) {
                if (rybari.get(i).getRybarId() == rybar.getRybarId()) {
                    rybari.set(i, rybar);
                    found = true;
                    break;
                }
            }

            if (!found) {
                throw new IllegalArgumentException("Rybar s ID " + rybar.getRybarId() + " neexistuje");
            }
        }
    }



    @Override
    public List<Rybar> najdiPodlaMena(String meno) {
        if (meno == null || meno.isEmpty()) {
            throw new IllegalArgumentException("Meno nemoze byt null alebo prazdne");
        }

        List<Rybar> vysledky = new ArrayList<>();
        for (Rybar rybar : this.rybari) {
            if (rybar.getMeno() != null && rybar.getMeno().contains(meno)) {
                vysledky.add(rybar);
            }
        }
        return vysledky;
    }

    @Override
    public void vymazPodlaID(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID musí byť väčšie ako 0");
        }

        boolean removed = rybari.removeIf(rybar -> rybar.getRybarId() == id);

        if (!removed) {
            throw new IllegalArgumentException("Rybar s ID " + id + " neexistuje");
        }
    }

    @Override
    public List<Rybar> najdiPodlaID(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID musí byť väčšie ako 0");
        }

        List<Rybar> vysledky = new ArrayList<>();
        for (Rybar rybar : rybari) {
            if (rybar.getRybarId() == id) {
                vysledky.add(rybar);
            }
        }
        return vysledky;
    }

}
