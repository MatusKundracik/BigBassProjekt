package org.projekt;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UlovokDAO {
    private static final String URL = "jdbc:mysql://localhost:3306/rybarska_aplikacia";
    private static final String USER = "root"; // Zmeň podľa tvojho nastavenia
    private static final String PASSWORD = "root"; // Zmeň podľa tvojho nastavenia

    // Pridanie nového úlovku
    public void pridatUlovok(Ulovok ulovok) throws SQLException {
        String sql = "INSERT INTO ulovok (datum_ulovku, cislo_reviru, druh_ryby, dlzka_v_cm, hmotnost_v_kg, kontrola) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDate(1, Date.valueOf(ulovok.getDatumUlovku()));
            stmt.setInt(2, ulovok.getCisloReviru());
            stmt.setString(3, ulovok.getDruhRyby());
            stmt.setInt(4, ulovok.getDlzkaVcm());
            stmt.setInt(5, ulovok.getHmotnostVkg());
            stmt.setInt(6, ulovok.getKontrola());

            stmt.executeUpdate();
        }
    }

    // Načítanie všetkých úlovkov
    public List<Ulovok> nacitatVsetkyUlovky() throws SQLException {
        List<Ulovok> ulovky = new ArrayList<>();
        String sql = "SELECT * FROM ulovok";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Ulovok ulovok = new Ulovok();
                ulovok.setIdUlovok(rs.getInt("id_ulovok"));
                ulovok.setDatumUlovku(rs.getDate("datum_ulovku").toLocalDate());
                ulovok.setCisloReviru(rs.getInt("cislo_reviru"));
                ulovok.setDruhRyby(rs.getString("druh_ryby"));
                ulovok.setDlzkaVcm(rs.getInt("dlzka_v_cm"));
                ulovok.setHmotnostVkg(rs.getInt("hmotnost_v_kg"));
                ulovok.setKontrola(rs.getInt("kontrola"));
                ulovky.add(ulovok);
            }
        }
        return ulovky;
    }

    // Načítanie úlovku podľa ID
    public Ulovok getUlovokById(int idUlovok) throws SQLException {
        Ulovok ulovok = null;
        String sql = "SELECT * FROM ulovok WHERE id_ulovok = ?";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idUlovok);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    ulovok = new Ulovok();
                    ulovok.setIdUlovok(rs.getInt("id_ulovok"));
                    ulovok.setDatumUlovku(rs.getDate("datum_ulovku").toLocalDate());
                    ulovok.setCisloReviru(rs.getInt("cislo_reviru"));
                    ulovok.setDruhRyby(rs.getString("druh_ryby"));
                    ulovok.setDlzkaVcm(rs.getInt("dlzka_v_cm"));
                    ulovok.setHmotnostVkg(rs.getInt("hmotnost_v_kg"));
                    ulovok.setKontrola(rs.getInt("kontrola"));
                }
            }
        }
        return ulovok;
    }

}
