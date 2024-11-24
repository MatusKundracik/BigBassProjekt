package org.example;

import lombok.var;

import java.time.LocalDate;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        var peto = new Rybar();
        var sp = new SpravaRybara();
        peto.setRybarId(1111);
        peto.setMeno("Martin");
        peto.setPriezvisko("Novák");
        peto.setDatumNarodenia(LocalDate.of(1990, 5, 15));
        peto.setAdresa("Bratislava");
        peto.setStatnaPrislusnost("SVK");
        peto.setCisloOP("ID123456");
        peto.setEvidentneCislo(1234);
        peto.setClenomOd(2015);
        peto.setPridanyDoEvidencie(LocalDate.of(2020,6,16));
        peto.setOdhlasenyZEvidencie(null); // Je stále člen
        sp.pridatRybara(peto);
        sp.vypisRybarov();
        sp.zmenitMeno(1111, "Matus");
        sp.zmenitPriezvisko(1111,"Kundracik");
        sp.vypisRybarov();
        sp.odobratRybara(1111);

        var kapor = new Ulovok();
        kapor.setIdUlovok(001);
        kapor.setDatumUlovku(LocalDate.of(2024,1,22));
        kapor.setCisloReviru(40102);
        kapor.setDruhRyby("kapor");
        kapor.setDlzkaVcm(40);
        kapor.setHmotnostVkg(20);

        System.out.println(kapor);

    }
}