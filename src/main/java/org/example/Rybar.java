package org.example;


import java.time.LocalDate;
import lombok.Data;

@Data
public class Rybar {

    private int rybarId;
    private String meno;
    private String priezvisko;
    private LocalDate datumNarodenia;
    private String adresa;
    private String statnaPrislusnost;
    private String cisloOP;
    private int evidentneCislo;
    private int clenomOd;
    private LocalDate pridanyDoEvidencie;
    private LocalDate odhlasenyZEvidencie;
}
