package Povolenie;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface PovolenieDAO {

    void insertPovolenie(Povolenie povolenie) throws SQLException;

    String generatePovolenieMessage(int rybarID, boolean kaprove, boolean lipnove, boolean pstruhove);

    public int getPovolenieIdByRybarId(int rybarId) throws SQLException;
}
