package org.projekt;

import java.time.LocalDate;
import lombok.Data;

@Data
public class Povolenie {
    private long idPovolenie;
    private LocalDate platnostOd;
    private LocalDate platnostDo;
    private boolean pstruhove;
    private boolean lipnove;
    private boolean kaprove;
    private long rybarIdRybara;

    public Povolenie(LocalDate platnostOd, LocalDate platnostDo, boolean kaprove, boolean lipnove, boolean pstruhove, int rybarID) {
        this.platnostOd = platnostOd;
        this.platnostDo = platnostDo;
        this.kaprove = kaprove;
        this.lipnove = lipnove;
        this.pstruhove = pstruhove;
        this.rybarIdRybara = rybarID;
    }
}

