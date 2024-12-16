package Rybar;

import java.sql.Connection;
import java.time.LocalDate;
import java.util.List;

public interface RybarDAO {

    void save(Rybar rybar);

    boolean jeEmailPouzity(Connection connection, String email);

    void insertUser(Connection connection, String meno, String priezvisko, String adresa,
                    String obcianskyPreukaz, String statnaPrislusnost, LocalDate datumNarodenia,
                    LocalDate pridanyDoEvidencie, LocalDate odhlasenyZEvidencie, String email, String heslo);

    int getUserIdByEmail(Connection connection, String email);

    boolean overitPouzivatela(Connection connection, String email, String heslo);

    String getRybarNameById(Connection connection, int idRybara);
}