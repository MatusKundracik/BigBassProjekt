package Rybar;

import java.time.LocalDate;

public interface RybarDAO {

    void save(Rybar rybar);

    boolean jeEmailPouzity(String email);

    void insertUser(Rybar rybar);

    int getUserIdByEmail(String email);

    boolean overitPouzivatela(String email, String heslo);

    String getRybarNameById(int idRybara);

    String getRybarAdresaById(int idRybara);

    String getRybarDatumNarById(int idRybara);

    String getRybarEmailById(int idRybara);

    String getRybarPridanyDoEvidencieById(int idRybara);

    String getRybarMenoPriezviskoById(int idRybara);

}