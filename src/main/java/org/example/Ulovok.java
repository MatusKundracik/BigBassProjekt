package org.example;

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
}
