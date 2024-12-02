package org.example;

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
}

