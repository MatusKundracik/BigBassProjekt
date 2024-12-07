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
    private LocalDate pridanyDoEvidencie;
    private LocalDate odhlasenyZEvidencie;
    private String email;

    public Rybar(String meno, String priezvisko, LocalDate datumNarodenia, String email,
                 String adresa, String statnaPrislusnost, String cisloOP,
                 LocalDate pridanyDoEvidencie, LocalDate odhlasenyZEvidencie) {
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
