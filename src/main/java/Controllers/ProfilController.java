package Controllers;

import Povolenie.PovolenieDAO;
import Ulovok.UlovokDAO;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import org.projekt.Factory;
import org.projekt.Session;

import java.sql.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class ProfilController {
    private PovolenieDAO povolenieDAO= Factory.INSTANCE.getPovolenieDAO();
    private final UlovokDAO ulovokDAO = Factory.INSTANCE.getUlovokDAO();
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
    private Label povolenieKLabel;

    @FXML
    private Label povolenieLLabel;

    @FXML
    private Label povoleniePLabel;

    @FXML
    private BarChart<String, Number> ulovkyBarChart;

    @FXML
    public void initialize() {
        int aktualnyRybarId = Session.aktualnyRybarId;
            int rybarId = aktualnyRybarId;


        if (aktualnyRybarId > 0) {

            String menoPouzivatela = getRybarMenoPriezviskoById(aktualnyRybarId);
            String adresaPouzivatela = getRybarAdresaById(aktualnyRybarId);
            String datumNarodenia = getRybarDatumNarById(aktualnyRybarId);
            String email = getRybarEmailById(aktualnyRybarId);
            String pridanyDoEvidencie = getRybarpridanyDoEvidencieById(aktualnyRybarId);
            LocalDate pridanyDatum = LocalDate.parse(pridanyDoEvidencie);
            LocalDate odobranieDatum = pridanyDatum.plusYears(1);
            boolean maKaprove = povolenieDAO.zobrazKaprovePovolenie(aktualnyRybarId);
            boolean maLipnove = povolenieDAO.zobrazPstruhovePovolenie(aktualnyRybarId);
            boolean maPstruhove = povolenieDAO.zobrazPstruhovePovolenie(aktualnyRybarId);

            povolenieKLabel.setText("Kaprové: " + (maKaprove ? "Áno" : "Nie"));
            povolenieLLabel.setText("Lipňové: " + (maLipnove ? "Áno" : "Nie"));
            povoleniePLabel.setText("Pstruhové: " + (maPstruhove ? "Áno" : "Nie"));

            if (menoPouzivatela != null) {
                menoPriezviskoLabel.setText("Prihlásený používateľ: " + menoPouzivatela);
                adresaLabel.setText("Adresa: " + adresaPouzivatela);
                datumNarodeniaLabel.setText("Datum narodenia: " + datumNarodenia);
                emailLabel.setText("Email: " + email);
                pridanyDoEvidencieLabel.setText("Pridaný do evidencie: " + pridanyDoEvidencie);
                odobranzyZEvidencieLabel.setText("Odobraný z evidencie: " + odobranieDatum);
            }
            if (aktualnyRybarId > 0) {
                celkovyPocetRybLabel.setText("Celkový počet chytených rýb: " + ulovokDAO.vypocitajCelkovyPocetRyb(rybarId));
                najvacsiaRybaLabel.setText("Najväčšia chytená ryba: " + ulovokDAO.vypocitajNajvacsiUlovok(rybarId) + " cm");
                najtazsiaRybaLabel.setText("Najťažšia chytená ryba: " + ulovokDAO.vypocitajNajtazsiUlovok(rybarId) + " kg");

                Map<String, Integer> ulovkyZaMesiac = ulovokDAO.nacitajPoctyUlovkovZaMesiac(rybarId);
                naplnBarChart(ulovkyZaMesiac);
            } else {
                celkovyPocetRybLabel.setText("Nie sú dostupné údaje.");
                najvacsiaRybaLabel.setText("Nie sú dostupné údaje.");
                najtazsiaRybaLabel.setText("Nie sú dostupné údaje.");
            }
        }

    }

    private void naplnBarChart(Map<String, Integer> ulovkyZaMesiac) {
        ulovkyBarChart.getData().clear();

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Počet úlovkov");

        for (Map.Entry<String, Integer> entry : ulovkyZaMesiac.entrySet()) {
            series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
        }

        ulovkyBarChart.getData().add(series);
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
                    return meno + " " + priezvisko;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
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
            e.printStackTrace();
        }
        return null;
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
            e.printStackTrace();
        }
        return null;
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
            e.printStackTrace();
        }
        return null;
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
            e.printStackTrace();
        }
        return null;
    }









}
