package org.projekt;

import java.time.LocalDate;

// jdk.vm.ci.meta.Local;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor

public class Ulovok {
    private int idUlovok;
    private LocalDate datumUlovku;
    private String cisloReviru;
    private String druhRyby;
    private double dlzkaVcm;
    private double hmotnostVkg;
    private int kontrola;

    public Ulovok(LocalDate datumUlovku, String cisloReviru, String druhRyby, double dlzkaVcm, double hmotnostVkg, int kontrola) {
        this.datumUlovku = datumUlovku;
        this.cisloReviru = cisloReviru;
        this.druhRyby = druhRyby;
        this.dlzkaVcm = dlzkaVcm;
        this.hmotnostVkg = hmotnostVkg;
        this.kontrola = kontrola;
    }


    @Override
    public String toString() {
        return String.format(
                "Dátum: %s, Revír: %s, Druh ryby: %s, Dĺžka: %.2f cm, Hmotnosť: %.2f kg",
                datumUlovku, cisloReviru, druhRyby, dlzkaVcm, hmotnostVkg
        );
    }
}