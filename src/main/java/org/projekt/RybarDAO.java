package org.projekt;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RybarDAO {
    private static final String URL = "jdbc:mysql://localhost:3306/rybarska_aplikacia";
    private static final String USER = "root"; // Zmeň podľa tvojho nastavenia
    private static final String PASSWORD = "root"; // Zmeň podľa tvojho nastavenia

    // Pridanie nového rybára
    public void pridatRybara(Rybar rybar) throws SQLException {
        String sql = "INSERT INTO rybar (meno, priezvisko, datum_narodenia, adresa, statna_prislusnost, " +
                "cislo_obcianskeho_preukazu, evidentne_cislo, clenom_od, pridany_do_evidencie, odhlaseny_z_evidencie) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, rybar.getMeno());
            stmt.setString(2, rybar.getPriezvisko());
            stmt.setDate(3, Date.valueOf(rybar.getDatumNarodenia()));
            stmt.setString(4, rybar.getAdresa());
            stmt.setString(5, rybar.getStatnaPrislusnost());
            stmt.setString(6, rybar.getCisloOP());
            stmt.setLong(7, rybar.getEvidentneCislo());
            stmt.setInt(8, rybar.getClenomOd());
            stmt.setDate(9, Date.valueOf(rybar.getPridanyDoEvidencie()));
            stmt.setDate(10, (rybar.getOdhlasenyZEvidencie() != null) ?
                    Date.valueOf(rybar.getOdhlasenyZEvidencie()) : null);

            stmt.executeUpdate();
        }
    }

    // Načítanie všetkých rybárov
    public List<Rybar> nacitatVsetkychRybarov() throws SQLException {
        List<Rybar> rybari = new ArrayList<>();
        String sql = "SELECT * FROM rybar";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Rybar rybar = new Rybar();
                rybar.setRybarId(rs.getInt("id_rybara"));
                rybar.setMeno(rs.getString("meno"));
                rybar.setPriezvisko(rs.getString("priezvisko"));
                rybar.setDatumNarodenia(rs.getDate("datum_narodenia").toLocalDate());
                rybar.setAdresa(rs.getString("adresa"));
                rybar.setStatnaPrislusnost(rs.getString("statna_prislusnost"));
                rybar.setCisloOP(rs.getString("cislo_obcianskeho_preukazu"));
                rybar.setEvidentneCislo(rs.getInt("evidentne_cislo"));
                rybar.setClenomOd(rs.getInt("clenom_od"));
                rybar.setPridanyDoEvidencie(rs.getDate("pridany_do_evidencie").toLocalDate());
                if (rs.getDate("odhlaseny_z_evidencie") != null) {
                    rybar.setOdhlasenyZEvidencie(rs.getDate("odhlaseny_z_evidencie").toLocalDate());
                }
                rybari.add(rybar);
            }
        }
        return rybari;
    }

    // Odstránenie rybára podľa ID
    public void odstranitRybara(long idRybara) throws SQLException {
        String sql = "DELETE FROM rybar WHERE id_rybara = ?";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, idRybara);
            stmt.executeUpdate();
        }
    }
}

