package Ulovok;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface UlovokDAO {

    void insertUlovok(Ulovok ulovok) throws SQLException;

}