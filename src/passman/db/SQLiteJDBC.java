/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package passman.db;

import java.sql.*;

/**
 *
 * @author P057736
 */
public class SQLiteJDBC {
    public void createConnection(){        
        try{
            Class.forName("org.sqlite.JDBC");
            Connection c = DriverManager.getConnection("jdbc:sqlite:passman.db");
            
            ResultSet rs = c.getMetaData().getTables(null, null, "passwords", null);
            // Create table
            if(!rs.next()){
                try (Statement stmt = c.createStatement()) {
                    String sql = "CREATE TABLE passwords2 " +
                            "(ID INT PRIMARY KEY NOT NULL," +
                            "LABEL TEXT NOT NULL, " +
                            "USERNAME CHAR(50) NOT NULL, " +
                            "PASSWORD CHAR(50) NOT NULL, " +
                            "COMMENT CHAR(250))";
                    
                    stmt.executeUpdate(sql);
                }
                c.close();
            }
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }
}
