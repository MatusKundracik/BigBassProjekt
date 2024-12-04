package org.projekt;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class SpravaUlovku {

    private List<Ulovok> zoznamUlovkov;

    public void SpravaUlovku() {
        this.zoznamUlovkov = new ArrayList<>();
    }

    public void pridajUlovok(Ulovok ulovok) {
        zoznamUlovkov.add(ulovok);
    }

    public List<Ulovok> vypisUlovkov() {
        return zoznamUlovkov;
    }

    public Ulovok getUlovok(int idUlovok) {
        for (Ulovok ulovok : zoznamUlovkov) {
            if (ulovok.getIdUlovok() == idUlovok) {
                return ulovok;
            }
        }
        return null;
    }

}
