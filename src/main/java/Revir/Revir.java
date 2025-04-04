package Revir;

import lombok.Data;

@Data
public class Revir {
    private long idRevir;
    private String nazov;
    private String lokalita;
    private String popis;
    private Boolean kaprove;
    private Boolean lipnove;
    private Boolean pstruhove;

    public Revir(String nazov, String lokalita, String popis, boolean kaprove, boolean lipnove, boolean pstruhove) {
        this.nazov = nazov;
        this.lokalita = lokalita;
        this.popis = popis;
        this.kaprove = kaprove;
        this.lipnove = lipnove;
        this.pstruhove = pstruhove;
    }

    public Revir() {

    }

    public boolean isKaprove() {
        return kaprove;
    }

    public boolean isLipnove() {
        return lipnove;
    }

    public boolean isPstruhove() {
        return pstruhove;
    }

    @Override
    public String toString() {
        return nazov + " (" + lokalita + ")";
    }

}
