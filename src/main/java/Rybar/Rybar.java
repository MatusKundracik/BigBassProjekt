package Rybar;

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
    private String cisloObcianskehoPreukazu;  // Premenované z cisloOP
    private LocalDate pridanyDoEvidencie;
    private LocalDate odhlasenyZEvidencie;
    private String email;
    private String heslo;  // Pridané heslo (ak je potrebné uložiť)

    public Rybar(String meno, String priezvisko, String adresa, String cisloObcianskehoPreukazu, String statnaPrislusnost, LocalDate datumNarodenia,
                 LocalDate pridanyDoEvidencie, LocalDate odhlasenyZEvidencie, String email, String heslo) { // Pridané heslo
        this.meno = meno;
        this.priezvisko = priezvisko;
        this.datumNarodenia = datumNarodenia;
        this.adresa = adresa;
        this.statnaPrislusnost = statnaPrislusnost;
        this.cisloObcianskehoPreukazu = cisloObcianskehoPreukazu;
        this.pridanyDoEvidencie = pridanyDoEvidencie;
        this.odhlasenyZEvidencie = odhlasenyZEvidencie;
        this.email = email;
        this.heslo = heslo;
    }
}
