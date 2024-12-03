package org.projekt;


import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
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

    public Rybar(int rybarId, String meno, String priezvisko, LocalDate datumNarodenia,
                 String adresa, String statnaPrislusnost, String cisloOP,
                 LocalDate pridanyDoEvidencie, LocalDate odhlasenyZEvidencie) {
        this.rybarId = rybarId;
        this.meno = meno;
        this.priezvisko = priezvisko;
        this.datumNarodenia = datumNarodenia;
        this.adresa = adresa;
        this.statnaPrislusnost = statnaPrislusnost;
        this.cisloOP = cisloOP;
        this.pridanyDoEvidencie = pridanyDoEvidencie;
        this.odhlasenyZEvidencie = odhlasenyZEvidencie;
    }
}
