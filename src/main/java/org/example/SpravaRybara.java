package org.example;

import java.util.ArrayList;
import java.util.List;

public class SpravaRybara {
    private List<Rybar> zoznamRybarov;

    public void vypisRybarov() {
        System.out.println(zoznamRybarov);
    }

    public SpravaRybara() {
        // Inicializovanie zoznamu rybárov
        this.zoznamRybarov = new ArrayList<>();
    }

    public void pridatRybara(Rybar rybar) {
        zoznamRybarov.add(rybar);
        System.out.println(zoznamRybarov);
    }

    public void odobratRybara(int rybarID) {
        zoznamRybarov.removeIf(rybar -> rybar.getRybarId() == rybarID);
        System.out.println(zoznamRybarov);
    }

    public void zmenitMeno(int rybarId, String noveMeno) {
        for (Rybar rybar : zoznamRybarov) {
            if (rybar.getRybarId() == rybarId) {
                rybar.setMeno(noveMeno);
                break;
            }
        }
    }


    public void zmenitPriezvisko(int rybarId, String novePriezvisko) {
        for (Rybar rybar : zoznamRybarov) {
            if (rybar.getRybarId() == rybarId) {
                rybar.setPriezvisko(novePriezvisko);
                break;
            }
        }
    }

    public void zmenaAdresy(int rybarId, String novaAdresa) {
        for (Rybar rybar : zoznamRybarov) {
            if (rybar.getRybarId() == rybarId) {
                rybar.setAdresa(novaAdresa);
                break;
            }
        }
    }

}
