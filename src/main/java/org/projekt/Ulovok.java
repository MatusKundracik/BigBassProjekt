package org.projekt;

import java.time.LocalDate;

// jdk.vm.ci.meta.Local;
import lombok.Data;

@Data
public class Ulovok {
    private int idUlovok;
    private LocalDate datumUlovku;
    private int cisloReviru;
    private String druhRyby;
    //private int pocetKusov;
    private int dlzkaVcm;
    private int hmotnostVkg;
    private int kontrola;

//    public Ulovok(int idUlovok, LocalDate datumUlovku, int cisloReviru, String druhRyby, int dlzkaVcm, int hmotnostVkg, int kontrola) {
//        this.idUlovok = idUlovok;
//        this.datumUlovku = datumUlovku;
//        this.cisloReviru = cisloReviru;
//        this.druhRyby = druhRyby;
//        this.dlzkaVcm = dlzkaVcm;
//        this.hmotnostVkg = hmotnostVkg;
//        this.kontrola = kontrola;
//    }
}
