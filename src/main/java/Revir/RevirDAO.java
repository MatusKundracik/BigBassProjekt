package Revir;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface RevirDAO {

    void insertRevir(Connection connection, Revir revir) throws SQLException;

    public int getRevirIdByName(Connection connection, String nazovReviru) throws SQLException;


}
