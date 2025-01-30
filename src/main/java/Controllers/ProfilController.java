package Controllers;

import Povolenie.PovolenieDAO;
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
        zobrazStatistikyUlovkov();
        int aktualnyRybarId = Session.aktualnyRybarId;

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
                // Nastavenie štatistík
                celkovyPocetRybLabel.setText("Celkový počet chytených rýb: " + vypocitajCelkovyPocetRyb(aktualnyRybarId));
                najvacsiaRybaLabel.setText("Najväčšia chytená ryba: " + vypocitajNajvacsiUlovok(aktualnyRybarId) + " cm");
                najtazsiaRybaLabel.setText("Najťažšia chytená ryba:  " + vypocitajNajtazsiUlovok(aktualnyRybarId) + " kg");
            } else {
                celkovyPocetRybLabel.setText("Nie sú dostupné údaje.");
                najvacsiaRybaLabel.setText("Nie sú dostupné údaje.");
                najtazsiaRybaLabel.setText("Nie sú dostupné údaje.");
            }
        }

    }



    private void naplnBarChart(Map<String, Integer> ulovkyZaMesiac) {
        if (ulovkyBarChart == null) {
            System.err.println("Chyba: BarChart nebol inicializovaný.");
            return;
        }

        if (ulovkyZaMesiac == null || ulovkyZaMesiac.isEmpty()) {
            System.err.println("Chyba: Údaje pre BarChart nie sú dostupné.");
            return;
        }

        ulovkyBarChart.getData().clear();

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Počet úlovkov");

        for (Map.Entry<String, Integer> entry : ulovkyZaMesiac.entrySet()) {
            XYChart.Data<String, Number> data = new XYChart.Data<>(entry.getKey(), entry.getValue());
            series.getData().add(data);
        }

        ulovkyBarChart.getData().add(series);
    }


    private Map<String, Integer> nacitajPoctyUlovkovZaMesiac() {
        Map<String, Integer> ulovkyZaMesiac = new HashMap<>();

            String query = "SELECT strftime('%Y-%m', datum) AS mesiac, COUNT(*) AS pocet_ulovkov " +
                    "FROM ulovok " +
                    "WHERE povolenie_rybar_id_rybara = ? " +
                    "GROUP BY strftime('%Y-%m', datum) " +
                    "ORDER BY pocet_ulovkov DESC " +
                    "LIMIT 3";

        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:bigbass.db");
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, Session.aktualnyRybarId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    String mesiac = resultSet.getString("mesiac");
                    int pocet = resultSet.getInt("pocet_ulovkov");
                    ulovkyZaMesiac.put(mesiac, pocet);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Chyba pri načítaní počtu úlovkov za jednotlivé mesiace.", e);
        }

        return ulovkyZaMesiac;
    }



    @FXML
    private void zobrazStatistikyUlovkov() {
        Map<String, Integer> ulovkyZaMesiac = nacitajPoctyUlovkovZaMesiac();
        if (ulovkyZaMesiac == null || ulovkyZaMesiac.isEmpty()) {
            System.err.println("Chyba: Žiadne údaje o úlovkoch.");
            return;
        }
        naplnBarChart(ulovkyZaMesiac);
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

    // Metóda na výpočet celkového počtu chytených rýb
    private int vypocitajCelkovyPocetRyb(int rybarId) {
        String sql = "SELECT count(druh_ryby) AS celkovy_pocet FROM ulovok WHERE povolenie_rybar_id_rybara = ?";
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
