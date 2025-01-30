package Revir;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface RevirDAO {

    void insertRevir(Revir revir) throws SQLException;

    public int getRevirIdByName(String nazovReviru) throws SQLException;


}
