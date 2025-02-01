package Controllers;

import Povolenie.PovolenieDAO;
import Rybar.RybarDAO;
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
    private final RybarDAO rybarDAO = Factory.INSTANCE.getRybarDAO();

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

            String menoPouzivatela = rybarDAO.getRybarMenoPriezviskoById(aktualnyRybarId);
            String adresaPouzivatela = rybarDAO.getRybarAdresaById(aktualnyRybarId);
            String datumNarodenia = rybarDAO.getRybarDatumNarById(aktualnyRybarId);
            String email = rybarDAO.getRybarEmailById(aktualnyRybarId);
            String pridanyDoEvidencie = rybarDAO.getRybarPridanyDoEvidencieById(aktualnyRybarId);
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

}
