package org.projekt;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class pripojenieNaDB {
    public static Connection connect() {
        Connection con = null;
        try {
            Class.forName("org.sqlite.JDBC");
            con = DriverManager.getConnection("jdbc:sqlite:C:/Users/matus/IdeaProjects/BigBassProjekt/bigbass.db");
            System.out.println("Connected to the database successfully");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    return con;
    }

}
