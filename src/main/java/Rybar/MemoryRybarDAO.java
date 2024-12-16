package Rybar;

import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MemoryRybarDAO implements RybarDAO {
    private List<Rybar> rybari = new ArrayList<>();
    private int posledneID = 0;


    @Override
    public void save(Rybar rybar) {
        if (rybar == null) {
            throw new IllegalArgumentException("Rybar nesmie byť null");
        }


        if (rybar.getMeno() == null || rybar.getPriezvisko() == null || rybar.getDatumNarodenia() == null) {
            throw new IllegalArgumentException("Meno, priezvisko a dátum narodenia sú povinné");
        }

        if (rybar.getRybarId() == 0) {

            rybar.setRybarId(++this.posledneID);
            this.rybari.add(rybar);
        } else {

            boolean found = false;
            for (int i = 0; i < rybari.size(); i++) {
                if (rybari.get(i).getRybarId() == rybar.getRybarId()) {
                    rybari.set(i, rybar);
                    found = true;
                    break;
                }
            }

            if (!found) {
                throw new IllegalArgumentException("Rybar s ID " + rybar.getRybarId() + " neexistuje");
            }
        }
    }


    public void insertUser(Connection connection, String meno, String priezvisko, String adresa,
                           String obcianskyPreukaz, String statnaPrislusnost, LocalDate datumNarodenia,
                           LocalDate pridanyDoEvidencie, LocalDate odhlasenyZEvidencie, String email, String heslo) {
        String insertQuery = "INSERT INTO rybar (meno, priezvisko, adresa, cislo_obcianskeho_preukazu, statna_prislusnost, datum_narodenia, pridany_do_evidencie, odhlaseny_z_evidencie, email, heslo) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(insertQuery)) {
            statement.setString(1, meno);
            statement.setString(2, priezvisko);
            statement.setString(3, adresa);
            statement.setString(4, obcianskyPreukaz);
            statement.setString(5, statnaPrislusnost);
            statement.setString(6, datumNarodenia.toString());
            statement.setString(7, pridanyDoEvidencie.toString());
            statement.setString(8, odhlasenyZEvidencie != null ? odhlasenyZEvidencie.toString() : null);
            statement.setString(9, email);
            statement.setString(10, heslo);

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean jeEmailPouzity(Connection connection, String email) {
        String sql = "SELECT COUNT(*) AS pocet FROM rybar WHERE email = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() && rs.getInt("pocet") > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public int getUserIdByEmail(Connection connection, String email) {
        String sql = "SELECT id_rybara FROM rybar WHERE email = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id_rybara");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public boolean overitPouzivatela(Connection connection, String email, String heslo) {
        String sql = "SELECT heslo FROM rybar WHERE email = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return BCrypt.checkpw(heslo, rs.getString("heslo"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public String getRybarNameById(Connection conn, int idRybara) {
        String sql = "SELECT meno, priezvisko FROM rybar WHERE id_rybara = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
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


}