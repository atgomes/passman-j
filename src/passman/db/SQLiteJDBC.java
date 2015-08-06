/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package passman.db;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import passman.model.Model;
import passman.model.ErrorDialog;

/**
 *
 * @author Andre Gomes
 */
public class SQLiteJDBC {
    private final List<Model> list = new ArrayList<>(); 
    
    public void createConnection(){        
        try{
            Class.forName("org.sqlite.JDBC");
            Connection c = DriverManager.getConnection("jdbc:sqlite:passman.s3db"); // creates connection to the DB
            
            ResultSet rs = c.getMetaData().getTables(null, null, "passwords", null);
            // Create table
            if(!rs.next()){
                try (Statement stmt = c.createStatement()) {
                    String sql = "CREATE TABLE passwords " +
                            "(ID INTEGER PRIMARY KEY," +
                            "LABEL TEXT NOT NULL, " +
                            "USERNAME CHAR NOT NULL, " +
                            "PASSWORD BLOB NOT NULL, " +
                            "SALT BLOB NOT NULL, " +
                            "COMMENT CHAR)";
                    
                    stmt.executeUpdate(sql);
                }
                c.close();
            }
        } catch (ClassNotFoundException | SQLException e) {
            ErrorDialog errDlg = new ErrorDialog(new JFrame(), e.getClass().getName(), e.getMessage());
            System.exit(0);
        }
    }
    
    public List<Model> getItems(){
        try{
            Class.forName("org.sqlite.JDBC");
            Connection c = DriverManager.getConnection("jdbc:sqlite:passman.s3db");
            
            ResultSet rs = c.getMetaData().getTables(null, null, "passwords", null);
            // 
            if(rs.next()){
                Statement stmt = c.createStatement();
                String sql = "SELECT LABEL, USERNAME, PASSWORD, SALT, COMMENT FROM passwords;";

                ResultSet entries = stmt.executeQuery(sql);

                while(entries.next()){
                    Model model = new Model(entries.getString("LABEL"),entries.getString("USERNAME"),
                            entries.getBytes("PASSWORD"),entries.getBytes("SALT"),entries.getString("COMMENT"));
                    
                    list.add(model);
                }
                
                stmt.close();
                c.close();
            }
        } catch (ClassNotFoundException | SQLException e) {            
            ErrorDialog errDlg = new ErrorDialog(new JFrame(), e.getClass().getName(), e.getMessage());
            System.exit(0);
        }
        
        return list;
    }
    
    public void addItem(Model model){
        try{
            Class.forName("org.sqlite.JDBC");
            Connection c = DriverManager.getConnection("jdbc:sqlite:passman.s3db");
            
            ResultSet rs = c.getMetaData().getTables(null, null, "passwords", null);
            // 
            if(rs.next()){
                PreparedStatement stmt = null;
                String sql = "INSERT INTO passwords (LABEL, USERNAME, PASSWORD, SALT, COMMENT)"+
                         " VALUES (\""+model.getLabel()+"\", \""+model.getUsername()+"\", ?, ?, \""+model.getComment()+"\")"; 
                stmt = c.prepareStatement(sql);
                
                stmt.setBytes(1, model.getPassword());
                stmt.setBytes(2, model.getSalt());

                stmt.executeUpdate();
                c.close();
            }
            else{
                try (Statement stmt = c.createStatement()) {
                    String sql = "CREATE TABLE passwords " +
                            "(ID INTEGER PRIMARY KEY," +
                            "LABEL TEXT NOT NULL, " +
                            "USERNAME CHAR(50) NOT NULL, " +
                            "PASSWORD CHAR(50) NOT NULL, " +
                            "COMMENT CHAR(250))";
                    
                    stmt.executeUpdate(sql);
                }
                try (Statement stmt = c.createStatement()) {
                    String sql = "INSERT INTO passwords (LABEL, USERNAME, PASSWORD, COMMENT) " + 
                            "VALUES (\""+model.getLabel()+"\", \""+
                            model.getUsername()+"\", \""+ model.getPassword() + "\", \""+ model.getComment()+"\")";
                    
                    stmt.executeUpdate(sql);
                }
                c.close();
            }
        } catch (ClassNotFoundException | SQLException e) {
            ErrorDialog errDlg = new ErrorDialog(new JFrame(), e.getClass().getName(), e.getMessage());
            System.exit(0);
        }
    }
    
    public void removeItem(Model model){
        try{
            Class.forName("org.sqlite.JDBC");
            Connection c = DriverManager.getConnection("jdbc:sqlite:passman.s3db");
            
            ResultSet rs = c.getMetaData().getTables(null, null, "passwords", null);
            // 
            if(rs.next()){
                try (Statement stmt = c.createStatement()) {
                    String sql = "DELETE FROM passwords WHERE LABEL = \"" + model.getLabel() + "\";";
                    
                    stmt.executeUpdate(sql);
                }
                c.close();
            }
        } catch (ClassNotFoundException | SQLException e) {
            ErrorDialog errDlg = new ErrorDialog(new JFrame(), e.getClass().getName(), e.getMessage());
            System.exit(0);
        }
    }
}
