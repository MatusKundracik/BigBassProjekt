package Rybar;

import java.sql.Connection;
import java.time.LocalDate;
import java.util.List;

public interface RybarDAO {

    void save(Rybar rybar);

    boolean jeEmailPouzity(String email);

    void insertUser(Connection connection, String meno, String priezvisko, String adresa,
                    String obcianskyPreukaz, String statnaPrislusnost, LocalDate datumNarodenia,
                    LocalDate pridanyDoEvidencie, LocalDate odhlasenyZEvidencie, String email, String heslo);

    int getUserIdByEmail(String email);

    boolean overitPouzivatela(String email, String heslo);

    String getRybarNameById(int idRybara);
}