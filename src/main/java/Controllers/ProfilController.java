package Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.projekt.Factory;
import org.projekt.Rybar;
import org.projekt.RybarDAO;
import org.projekt.Session;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class ProfilController {

    private RybarDAO rybarDAO = Factory.INSTANCE.getRybarDAO();

    @FXML
    private Label adresaLabel;

    @FXML
    private Label celkovyPocetRybLabel;

    @FXML
    private Label najtazsiaRybaLabel;

    @FXML
    private Label najvacsiaRybaLabel;

    @FXML
    private Label datumNarodeniaLabel;

    @FXML
    private Label emailLabel;

    @FXML
    private Label menoPriezviskoLabel;

    @FXML
    private Label odobranzyZEvidencieLabel;

    @FXML
    private Label pridanyDoEvidencieLabel;

    @FXML
    public void initialize() {
        // Načítanie údajov o aktuálne prihlásenom používateľovi zo Session
        int aktualnyRybarId = Session.aktualnyRybarId;

        if (aktualnyRybarId > 0) {
            // Načítaj meno používateľa podľa ID
            String menoPouzivatela = getRybarMenoPriezviskoById(aktualnyRybarId);
            String adresaPouzivatela = getRybarAdresaById(aktualnyRybarId);
            String datumNarodenia = getRybarDatumNarById(aktualnyRybarId);
            String email = getRybarEmailById(aktualnyRybarId);
            String pridanyDoEvidencie = getRybarpridanyDoEvidencieById(aktualnyRybarId);
            LocalDate pridanyDatum = LocalDate.parse(pridanyDoEvidencie);
            LocalDate odobranieDatum = pridanyDatum.plusYears(1);
            if (menoPouzivatela != null) {
                menoPriezviskoLabel.setText("Prihlásený používateľ: " + menoPouzivatela);
                adresaLabel.setText("Adresa: " + adresaPouzivatela);
                datumNarodeniaLabel.setText("Datum narodenia: " + datumNarodenia);
                emailLabel.setText("Email: " + email);
                pridanyDoEvidencieLabel.setText("Pridaný do evidencie: " + pridanyDoEvidencie);
                odobranzyZEvidencieLabel.setText("Odobraný z evidencie: " + odobranieDatum);
            }
            if (aktualnyRybarId > 0) {
                // Nastavenie štatistík
                celkovyPocetRybLabel.setText("Celkový počet chytených rýb: " + vypocitajCelkovyPocetRyb(aktualnyRybarId));
                najvacsiaRybaLabel.setText("Najväčšia chytený ryba: " + vypocitajNajvacsiUlovok(aktualnyRybarId) + " cm");
                najtazsiaRybaLabel.setText("Najťažšia chytený ryba:  " + vypocitajNajtazsiUlovok(aktualnyRybarId) + " kg");
            } else {
                celkovyPocetRybLabel.setText("Nie sú dostupné údaje.");
                najvacsiaRybaLabel.setText("Nie sú dostupné údaje.");
                najtazsiaRybaLabel.setText("Nie sú dostupné údaje.");
            }
        }
        //nahradObsah("/Profil.fxml");
    }

    private String getRybarMenoPriezviskoById(int idRybara) {
        String sql = "SELECT meno, priezvisko FROM rybar WHERE id_rybara = ?";
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:bigbass.db");
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idRybara);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String meno = rs.getString("meno");
                    String priezvisko = rs.getString("priezvisko");
                    return meno + " " + priezvisko; // Skombinuj meno a priezvisko
                }
            }
        } catch (Exception e) {
            e.printStackTrace(); // Vypíše chybu pri práci s databázou
        }
        return null; // Ak sa meno nepodarilo načítať
    }

    private String getRybarAdresaById(int idRybara) {
        String sql = "SELECT adresa FROM rybar WHERE id_rybara = ?";
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:bigbass.db");
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idRybara);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String adresa = rs.getString("adresa");
                    return adresa;
                }
            }
        } catch (Exception e) {
            e.printStackTrace(); // Vypíše chybu pri práci s databázou
        }
        return null; // Ak sa meno nepodarilo načítať
    }

    private String getRybarDatumNarById(int idRybara) {
        String sql = "SELECT datum_narodenia FROM rybar WHERE id_rybara = ?";
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:bigbass.db");
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idRybara);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String datumNar = rs.getString("datum_narodenia");
                    return datumNar;
                }
            }
        } catch (Exception e) {
            e.printStackTrace(); // Vypíše chybu pri práci s databázou
        }
        return null; // Ak sa meno nepodarilo načítať
    }

    private String getRybarEmailById(int idRybara) {
        String sql = "SELECT email FROM rybar WHERE id_rybara = ?";
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:bigbass.db");
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idRybara);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String email = rs.getString("email");
                    return email;
                }
            }
        } catch (Exception e) {
            e.printStackTrace(); // Vypíše chybu pri práci s databázou
        }
        return null; // Ak sa meno nepodarilo načítať
    }

    private String getRybarpridanyDoEvidencieById(int idRybara) {
        String sql = "SELECT pridany_do_evidencie FROM rybar WHERE id_rybara = ?";
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:bigbass.db");
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idRybara);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String pridanyDoEvi = rs.getString("pridany_do_evidencie");
                    return pridanyDoEvi;
                }
            }
        } catch (Exception e) {
            e.printStackTrace(); // Vypíše chybu pri práci s databázou
        }
        return null; // Ak sa meno nepodarilo načítať
    }

    // Metóda na výpočet celkového počtu chytených rýb
    private int vypocitajCelkovyPocetRyb(int rybarId) {
        String sql = "SELECT SUM(druh_ryby) AS celkovy_pocet FROM ulovok WHERE povolenie_rybar_id_rybara = ?";
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:bigbass.db");
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, rybarId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("celkovy_pocet");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    // Metóda na výpočet najväčšej chytenej ryby
    private int vypocitajNajvacsiUlovok(int rybarId) {
        String sql = "SELECT MAX(dlzka_v_cm) AS najvacsia_ryba FROM ulovok WHERE povolenie_rybar_id_rybara = ?";
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:bigbass.db");
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, rybarId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("najvacsia_ryba");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    // Metóda na výpočet najťažšej chytenej ryby
    private double vypocitajNajtazsiUlovok(int rybarId) {
        String sql = "SELECT MAX(hmotnost_v_kg) AS najtazsia_ryba FROM ulovok WHERE povolenie_rybar_id_rybara = ?";
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:bigbass.db");
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, rybarId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("najtazsia_ryba");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0.0;
    }

}
