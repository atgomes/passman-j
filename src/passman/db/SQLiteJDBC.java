/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package passman.db;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import passman.Utils;
import passman.model.Model;
import passman.model.ErrorDialog;
import passman.model.User;

/**
 *
 * @author Andre Gomes
 */
public class SQLiteJDBC {
    private final List<Model> list = new ArrayList<>(); 
    private final String databaseName = "passman.s3db";
    
    public void createConnection(){        
        try{
            Class.forName("org.sqlite.JDBC");
            Connection c = DriverManager.getConnection("jdbc:sqlite:passman.s3db"); // creates connection to the DB
            
            ResultSet rs = c.getMetaData().getTables(null, null, "pmj_passwords", null);
            // Create table
            if(!rs.next()){
                try (Statement stmt = c.createStatement()) {
                    String sql = "CREATE TABLE pmj_passwords " +
                            "(ID INTEGER PRIMARY KEY, " +
                            "LABEL TEXT NOT NULL, " +
                            "USERNAME CHAR NOT NULL, " +
                            "PASSWORD BLOB NOT NULL, " +
                            "SALT BLOB NOT NULL, " +
                            "COMMENT CHAR)";
                    
                    String sql2 = "CREATE TABLE pmj_users " +
                            "(ID INTEGER PRIMARY KEY, " +
                            "USERNAME CHAR NOT NULL, " +
                            "PASSHASH BLOB NOT NULL, " +
                            "SALT BLOB NOT NULL)";
                    
                    String sql3 = "CREATE TABLE pmj_entries " +
                            "(ID INTEGER PRIMARY KEY, " +
                            "USER_ID INTEGER NOT NULL, " +
                            "PASSWORD_ID INTEGER NOT NULL, " +
                            "ENTRY_DATE TEXT NOT NULL)";
                    
                    stmt.executeUpdate(sql);
                    stmt.executeUpdate(sql2);
                    stmt.executeUpdate(sql3);
                }
                c.close();
                
                // Log action
                Logger.getLogger("").log(Level.INFO, "New database file created with the following name: {0}", databaseName);
            }
        } catch (ClassNotFoundException | SQLException e) {
            // Log exception
            Logger.getLogger("").log(Level.SEVERE, "Application stopped due to exception: {0}",e.getClass().getName());
            ErrorDialog errDlg = new ErrorDialog(new JFrame(), e.getClass().getName(), e.getMessage());
            System.exit(0);
        }
    }
    
    public List<Model> getItems2(){
        try{
            Class.forName("org.sqlite.JDBC");
            Connection c = DriverManager.getConnection("jdbc:sqlite:passman.s3db");
            
            ResultSet rs = c.getMetaData().getTables(null, null, "pmj_entries", null);
            // 
            if(rs.next()){
                Statement stmt = c.createStatement();
                String sql = "SELECT LABEL, USERNAME, PASSWORD, SALT, COMMENT FROM pmj_passwords"+
                        " WHERE ID IN (SELECT PASSWORD_ID FROM pmj_entries WHERE USER_ID=\""+ 
                        this.getUserID(Utils.getCurrentUser()) +"\");";
                

                ResultSet entries = stmt.executeQuery(sql);
                
                while(entries.next()){
                    Model model = new Model(entries.getString("LABEL"),entries.getString("USERNAME"),
                            entries.getBytes("PASSWORD"),
                            entries.getBytes("SALT"),entries.getString("COMMENT"));
                    
                    list.add(model);
                }                
                stmt.close();
                c.close();
            }
        } catch (ClassNotFoundException | SQLException e) {
            // Log exception
            Logger.getLogger("").log(Level.SEVERE, "Application stopped due to exception: {0}",e.getClass().getName());
            ErrorDialog errDlg = new ErrorDialog(new JFrame(), e.getClass().getName(), e.getMessage());
            System.exit(0);
        }
        
        return list;
    }
    
    public Model getItem(String label){
        Model model = null;
        try{
            Class.forName("org.sqlite.JDBC");
            Connection c = DriverManager.getConnection("jdbc:sqlite:passman.s3db");
            
            ResultSet rs = c.getMetaData().getTables(null, null, "pmj_passwords", null);
            // 
            if(rs.next()){
                Statement stmt = c.createStatement();
                String sql = "SELECT LABEL, USERNAME, PASSWORD, SALT, COMMENT FROM pmj_passwords WHERE LABEL=\""+label+"\";";

                ResultSet entries = stmt.executeQuery(sql);

                if(entries.next()){
                    model = new Model(entries.getString("LABEL"),entries.getString("USERNAME"),
                            entries.getBytes("PASSWORD"),entries.getBytes("SALT"),entries.getString("COMMENT"));
                    
                }
                
                stmt.close();
                
            }
            c.close();
        } catch (ClassNotFoundException | SQLException e) {            
            ErrorDialog errDlg = new ErrorDialog(new JFrame(), e.getClass().getName(), e.getMessage());
            System.exit(0);
        }
        
        return model;
    }
    
    public Model getItem2(String label){
        Model model = null;
        try{
            Class.forName("org.sqlite.JDBC");
            Connection c = DriverManager.getConnection("jdbc:sqlite:passman.s3db");
            
            ResultSet rs = c.getMetaData().getTables(null, null, "pmj_entries", null);
            // 
            if(rs.next()){
                Statement stmt = c.createStatement();
                String sql = "SELECT LABEL, USERNAME, PASSWORD, SALT, COMMENT FROM pmj_passwords WHERE "+
                        "ID IN (SELECT PASSWORD_ID FROM pmj_entries WHERE USER_ID "+
                        "IN (SELECT ID FROM pmj_users WHERE USERNAME=\""+Utils.getCurrentUser()+"\"))"+
                        " AND LABEL=\""+label+"\"";

                ResultSet entries = stmt.executeQuery(sql);

                if(entries.next()){
                    model = new Model(entries.getString("LABEL"),entries.getString("USERNAME"),
                            entries.getBytes("PASSWORD"),entries.getBytes("SALT"),entries.getString("COMMENT"));
                    
                }
                
                stmt.close();
                
            }
            c.close();
        } catch (ClassNotFoundException | SQLException e) { 
            // Log exception
            Logger.getLogger("").log(Level.SEVERE, "Application stopped due to exception: {0}",e.getClass().getName());
            ErrorDialog errDlg = new ErrorDialog(new JFrame(), e.getClass().getName(), e.getMessage());
            System.exit(0);
        }
        
        return model;
    }
    
    public int getItemID(String label){
        int itemID = -1;
        try{
            Class.forName("org.sqlite.JDBC");
            Connection c = DriverManager.getConnection("jdbc:sqlite:passman.s3db");
            
            ResultSet rs = c.getMetaData().getTables(null, null, "pmj_passwords", null);
            // 
            if(rs.next()){
                Statement stmt = c.createStatement();
                String sql = "SELECT ID FROM pmj_passwords WHERE LABEL=\""+label+"\";";
                /*String sql = "SELECT ID FROM pmj_passwords WHERE "+
                        "ID IN (SELECT PASSWORD_ID FROM pmj_entries WHERE USER_ID "+
                        "IN (SELECT ID FROM pmj_users WHERE USERNAME=\""+Utils.getCurrentUser()+"\"))"+
                        " AND LABEL=\""+label+"\"";*/

                ResultSet entries = stmt.executeQuery(sql);

                if(entries.next()){
                    itemID = entries.getInt("ID");                    
                }
                
                stmt.close();
                
            }
            c.close();
        } catch (ClassNotFoundException | SQLException e) {  
            // Log exception
            Logger.getLogger("").log(Level.SEVERE, "Application stopped due to exception: {0}",e.getClass().getName());
            ErrorDialog errDlg = new ErrorDialog(new JFrame(), e.getClass().getName(), e.getMessage());
            System.exit(0);
        }
        
        return itemID;
    }
    
    public void addItem(Model model){
        try{
            Class.forName("org.sqlite.JDBC");
            Connection c = DriverManager.getConnection("jdbc:sqlite:passman.s3db");
            
            ResultSet rs = c.getMetaData().getTables(null, null, "pmj_passwords", null);
            // 
            if(rs.next()){
                if(this.getItem(model.getLabel()) == null){
                    PreparedStatement stmt = null;
                    String sql = "INSERT INTO pmj_passwords (LABEL, USERNAME, PASSWORD, SALT, COMMENT)"+
                             " VALUES (\""+model.getLabel()+"\", \""+model.getUsername()+"\", ?, ?, \""+model.getComment()+"\")"; 
                    stmt = c.prepareStatement(sql);

                    stmt.setBytes(1, model.getPassword());
                    stmt.setBytes(2, model.getSalt());

                    stmt.executeUpdate();
                    stmt.close();
                }
                c.close();
            }
            else{
                try (Statement stmt = c.createStatement()) {
                    String sql = "CREATE TABLE pmj_passwords " +
                            "(ID INTEGER PRIMARY KEY," +
                            "LABEL TEXT NOT NULL, " +
                            "USERNAME CHAR(50) NOT NULL, " +
                            "PASSWORD CHAR(50) NOT NULL, " +
                            "COMMENT CHAR(250))";
                    
                    stmt.executeUpdate(sql);
                }
                PreparedStatement stmt = null;
                String sql = "INSERT INTO pmj_passwords (LABEL, USERNAME, PASSWORD, SALT, COMMENT)"+
                         " VALUES (\""+model.getLabel()+"\", \""+model.getUsername()+"\", ?, ?, \""+model.getComment()+"\")"; 
                stmt = c.prepareStatement(sql);
                
                stmt.setBytes(1, model.getPassword());
                stmt.setBytes(2, model.getSalt());

                stmt.executeUpdate();
                c.close();
            }
        } catch (ClassNotFoundException | SQLException e) {
            ErrorDialog errDlg = new ErrorDialog(new JFrame(), e.getClass().getName(), e.getMessage());
            System.exit(0);
        }
    }
    
    public void addItem2(Model model){
        try{
            Class.forName("org.sqlite.JDBC");
            Connection c = DriverManager.getConnection("jdbc:sqlite:passman.s3db");
            
            ResultSet rs = c.getMetaData().getTables(null, null, "pmj_%", null);
            
           // Checks number os results
            int count = 0;
            while(rs.next()){
                ++count;
            }
            if(count>=3){
                // Updates passwords table
                PreparedStatement stmt = null;
                String sql = "INSERT INTO pmj_passwords (LABEL, USERNAME, PASSWORD, SALT, COMMENT)"+
                         " VALUES (\""+model.getLabel()+"\", "+
                                  "\""+model.getUsername()+"\", "+
                                    "?, "+
                                    "?, "+
                                  "\""+model.getComment()+"\");";


                stmt = c.prepareStatement(sql);

                stmt.setBytes(1, model.getPassword());
                stmt.setBytes(2, model.getSalt());

                stmt.executeUpdate();

                // Get last entry ID
                int itemID = stmt.getGeneratedKeys().getInt(1);
                // Get current date
                SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date now = new Date();
                String strDate = sdfDate.format(now);                 

                // Get current user ID
                int userID = this.getUserID(Utils.getCurrentUser());

                // Update entries table
                sql = "INSERT INTO pmj_entries (USER_ID, PASSWORD_ID, ENTRY_DATE)"+
                        "VALUES (" + userID +
                                ", " + itemID +
                                ", \"" + strDate + "\");";

                stmt = c.prepareStatement(sql);

                stmt.executeUpdate();

                stmt.close();
                c.close();
                // Log action
                Logger.getLogger("").log(Level.INFO, "Entry with label {0} added to database.", model.getLabel());
            }
            else{
                try (Statement stmt = c.createStatement()) {
                    String sql = "CREATE TABLE pmj_passwords " +
                            "(ID INTEGER PRIMARY KEY," +
                            "LABEL TEXT NOT NULL, " +
                            "USERNAME CHAR(50) NOT NULL, " +
                            "PASSWORD CHAR(50) NOT NULL, " +
                            "COMMENT CHAR(250))";
                    
                    stmt.executeUpdate(sql);
                }
                PreparedStatement stmt = null;
                String sql = "INSERT INTO pmj_passwords (LABEL, USERNAME, PASSWORD, SALT, COMMENT)"+
                         " VALUES (\""+model.getLabel()+"\", \""+model.getUsername()+"\", ?, ?, \""+model.getComment()+"\")"; 
                stmt = c.prepareStatement(sql);
                
                stmt.setBytes(1, model.getPassword());
                stmt.setBytes(2, model.getSalt());

                stmt.executeUpdate();
                c.close();
            }
        } catch (ClassNotFoundException | SQLException e) {
            // Log exception
            Logger.getLogger("").log(Level.SEVERE, "Application stopped due to exception: {0}",e.getClass().getName());
            ErrorDialog errDlg = new ErrorDialog(new JFrame(), e.getClass().getName(), e.getMessage());
            System.exit(0);
        }
    }
    
    public void removeItem(Model model){
        try{
            Class.forName("org.sqlite.JDBC");
            Connection c = DriverManager.getConnection("jdbc:sqlite:passman.s3db");
            
            ResultSet rs = c.getMetaData().getTables(null, null, "pmj_passwords", null);
            // 
            if(rs.next()){
                try (Statement stmt = c.createStatement()) {
                    String sql = "DELETE FROM pmj_passwords WHERE LABEL = \"" + model.getLabel() + "\";";
                    
                    stmt.executeUpdate(sql);
                }
                c.close();
            }
        } catch (ClassNotFoundException | SQLException e) {
            ErrorDialog errDlg = new ErrorDialog(new JFrame(), e.getClass().getName(), e.getMessage());
            System.exit(0);
        }
    }
    
    public void removeItem2(Model model){
        try{
            Class.forName("org.sqlite.JDBC");
            Connection c = DriverManager.getConnection("jdbc:sqlite:passman.s3db");
            
            ResultSet rs = c.getMetaData().getTables(null, null, "pmj_entries", null);
            // 
            if(rs.next()){
                try (Statement stmt = c.createStatement()) {
                    String sql = "DELETE FROM pmj_entries WHERE PASSWORD_ID IN ("+
                            "SELECT ID FROM pmj_passwords WHERE LABEL=\""+model.getLabel()+"\");";
                    
                    stmt.executeUpdate(sql);
                }
                c.close();
                // Log action
                Logger.getLogger("").log(Level.INFO, "Entry with label {0} removed from database.", model.getLabel());
            }
        } catch (ClassNotFoundException | SQLException e) {
            // Log exception
            Logger.getLogger("").log(Level.SEVERE, "Application stopped due to exception: {0}",e.getClass().getName());
            ErrorDialog errDlg = new ErrorDialog(new JFrame(), e.getClass().getName(), e.getMessage());
            System.exit(0);
        }
    }
    
    public void addUser(User user){
        try{
            Class.forName("org.sqlite.JDBC");
            Connection c = DriverManager.getConnection("jdbc:sqlite:passman.s3db");
            
            ResultSet rs = c.getMetaData().getTables(null, null, "pmj_users", null);
            // 
            if(rs.next()){
                if(this.getUser(user.getUsername()) == null){
                    PreparedStatement stmt = null;
                    String sql = "INSERT INTO pmj_users (USERNAME, PASSHASH, SALT)"+
                             " VALUES (\""+user.getUsername()+"\", ?, ?)";
                    stmt = c.prepareStatement(sql);

                    stmt.setBytes(1, user.getSecurePassword());
                    stmt.setBytes(2, user.getSaltArray());

                    stmt.executeUpdate();
                    stmt.close();
                }
            }
            else{
                try (Statement stmt = c.createStatement()) {
                    String sql = "CREATE TABLE pmj_users " +
                            "(ID INTEGER PRIMARY KEY," +
                            "USERNAME CHAR NOT NULL, " +
                            "PASSHASH BLOB NOT NULL, " +
                            "SALT BLOB NOT NULL)";
                    
                    stmt.executeUpdate(sql);
                }
                PreparedStatement stmt = null;
                String sql = "INSERT INTO pmj_users (USERNAME, PASSWORD, SALT)"+
                         " VALUES (\""+user.getUsername()+"\", ?, ?)";
                stmt = c.prepareStatement(sql);
                
                stmt.setBytes(1, user.getSecurePassword());
                stmt.setBytes(2, user.getSaltArray());

                stmt.executeUpdate();
                stmt.close();
                
                // Log action
                Logger.getLogger("").log(Level.INFO, "User {0} added to database.", user.getUsername());
            }
            c.close();
        } catch (ClassNotFoundException | SQLException e) {
            // Log exception
            Logger.getLogger("").log(Level.SEVERE, "Application stopped due to exception: {0}",e.getClass().getName());
            ErrorDialog errDlg = new ErrorDialog(new JFrame(), e.getClass().getName(), e.getMessage());
            System.exit(0);
        }
    }
    
    public User getUser(String username){
        User user = null;
        try{
            Class.forName("org.sqlite.JDBC");
            Connection c = DriverManager.getConnection("jdbc:sqlite:passman.s3db");
            
            ResultSet rs = c.getMetaData().getTables(null, null, "pmj_users", null);
            // 
            if(rs.next()){
                Statement stmt = c.createStatement();
                String sql = "SELECT USERNAME, PASSHASH, SALT FROM pmj_users WHERE USERNAME=\""+username+"\";";

                ResultSet entries = stmt.executeQuery(sql);

                if(entries.next()){
                    user = new User(entries.getString("USERNAME"),entries.getBytes("PASSHASH"),entries.getBytes("SALT"));
                }
                
                stmt.close();
                
            }
            c.close();
        } catch (ClassNotFoundException | SQLException e) { 
            // Log exception
            Logger.getLogger("").log(Level.SEVERE, "Application stopped due to exception: {0}",e.getClass().getName());
            ErrorDialog errDlg = new ErrorDialog(new JFrame(), e.getClass().getName(), e.getMessage());
            System.exit(0);
        }
        
        return user;
    }
    
    public int getUserID(String username){
        int userID = -1;
        try{
            Class.forName("org.sqlite.JDBC");
            Connection c = DriverManager.getConnection("jdbc:sqlite:passman.s3db");
            
            ResultSet rs = c.getMetaData().getTables(null, null, "pmj_users", null);
            // 
            if(rs.next()){
                Statement stmt = c.createStatement();
                String sql = "SELECT ID FROM pmj_users WHERE USERNAME=\""+username+"\";";

                ResultSet entries = stmt.executeQuery(sql);

                if(entries.next()){
                    userID = entries.getInt("ID");
                }
                else{
                    // Log event
                    Logger.getLogger("").log(Level.SEVERE, "Failed to retrieve USER ID from database");
                }
                stmt.close();           
            } else{
                // Log action
                Logger.getLogger("").log(Level.SEVERE, "Failed to find table pmj_users.");
            }
            c.close();
        } catch (ClassNotFoundException | SQLException e) {     
            // Log exception
            Logger.getLogger("").log(Level.SEVERE, "Application stopped due to exception: {0}",e.getClass().getName());
            ErrorDialog errDlg = new ErrorDialog(new JFrame(), e.getClass().getName(), e.getMessage());
            System.exit(0);
        }
        
        return userID;
    }
    
    public void updateUsername(String oldUsername, String newUsername){
        try{
            Class.forName("org.sqlite.JDBC");
            Connection c = DriverManager.getConnection("jdbc:sqlite:passman.s3db");
            
            ResultSet rs = c.getMetaData().getTables(null, null, "pmj_users", null);
            int oldUserID = this.getUserID(oldUsername);
            if(rs.next()){
                if(oldUserID > -1){
                    PreparedStatement stmt = null;
                    String sql = "UPDATE pmj_users SET USERNAME=\""+ newUsername +"\" WHERE ID="+oldUserID+";";
                    stmt = c.prepareStatement(sql);

                    stmt.executeUpdate();
                    stmt.close();
                }
            }
            c.close();
        } catch (ClassNotFoundException | SQLException e) {
            // Log exception
            Logger.getLogger("").log(Level.SEVERE, "Application stopped due to exception: {0}",e.getClass().getName());
            ErrorDialog errDlg = new ErrorDialog(new JFrame(), e.getClass().getName(), e.getMessage());
            System.exit(0);
        }
    }
    
    public void updatePassword(User user){
        try{
            Class.forName("org.sqlite.JDBC");
            Connection c = DriverManager.getConnection("jdbc:sqlite:passman.s3db");
            
            ResultSet rs = c.getMetaData().getTables(null, null, "pmj_users", null);
            
            if(rs.next()){
                int userID = this.getUserID(user.getUsername());
                if(userID >- 1){
                    PreparedStatement stmt = null;
                    String sql = "UPDATE pmj_users SET PASSHASH=?, SALT=? WHERE ID=\""+ userID +"\";";
                    stmt = c.prepareStatement(sql);

                    stmt.setBytes(1, user.getSecurePassword());
                    stmt.setBytes(2, user.getSaltArray());

                    stmt.executeUpdate();
                    stmt.close();
                }
            }
            c.close();
        } catch (ClassNotFoundException | SQLException e) {
            // Log exception
            Logger.getLogger("").log(Level.SEVERE, "Application stopped due to exception: {0}",e.getClass().getName());
            ErrorDialog errDlg = new ErrorDialog(new JFrame(), e.getClass().getName(), e.getMessage());
            System.exit(0);
        }
    }
}
