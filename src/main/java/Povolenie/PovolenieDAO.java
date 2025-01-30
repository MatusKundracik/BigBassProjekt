package Povolenie;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface PovolenieDAO {

    void insertPovolenie(Povolenie povolenie) throws SQLException;

    String generatePovolenieMessage(int rybarID, boolean kaprove, boolean lipnove, boolean pstruhove);

    public int getPovolenieIdByRybarId(int rybarId) throws SQLException;

    public boolean zobrazKaprovePovolenie(int idRybara);

    public boolean zobrazPstruhovePovolenie(int idRybara);

    public boolean zobrazLipnovePovolenie(int idRybara);

    public void idPovoleniaPodlaRybaraID();
}
