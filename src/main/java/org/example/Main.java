package org.example;

import lombok.var;

import java.time.LocalDate;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        var peto = new Rybar();
        peto.setMeno("Martin");

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