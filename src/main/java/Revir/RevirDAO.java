package Revir;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface RevirDAO {

    void insertRevir(Revir revir) throws SQLException;

    public int getRevirIdByName(String nazovReviru) throws SQLException;

    Map<String, Integer> getReviryForRybar(int rybarId) throws SQLException;
}
