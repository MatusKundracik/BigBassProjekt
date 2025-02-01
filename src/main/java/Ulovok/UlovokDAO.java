package Ulovok;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface UlovokDAO {

    void insertUlovok(Ulovok ulovok) throws SQLException;

    int vypocitajCelkovyPocetRyb(int rybarId);

    int vypocitajNajvacsiUlovok(int rybarId);

    double vypocitajNajtazsiUlovok(int rybarId);

    Map<String, Integer> nacitajPoctyUlovkovZaMesiac(int rybarId);

    List<Ulovok> nacitajUlovkyPreRybara(int rybarId);

    boolean aktualizujKontrolu(int idUlovku);
}