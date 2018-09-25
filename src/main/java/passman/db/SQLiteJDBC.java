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
    private final String prefix = "jdbc:sqlite:";
    private final String databaseName = "passman.s3db";
    
    /**
     * Checks if application's required tables exist. If they don't then creates them.
     * @return integer value equal to 0 (zero) if it created the required tables, 
     * 1 (one) if the tables already exist or a negative value otherwise.
     */
    public int createConnection(){
        int result = -1;
        try{
            Class.forName("org.sqlite.JDBC");
            String connectionString = "jdbc:sqlite:".concat(databaseName);
            Connection c = DriverManager.getConnection(connectionString); // creates connection to the DB
            
            ResultSet rs = c.getMetaData().getTables(null, null, "pmj_%", null);
            
           // Checks number os results
            int count = 0;
            while(rs.next()){
                ++count;
            }
            // Checks if all three tables exist, if not creates them
            if(count<3){
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
                
                // Log action
                Logger.getLogger("").log(Level.INFO, "New database file created with the following name: {0}", databaseName);
                result = 0;
            } else{ // Tables already exist
                result = 1;
            }
            // Close connection
            c.close();
        } catch (ClassNotFoundException | SQLException e) {
            // Log exception
            Logger.getLogger("").log(Level.SEVERE, "Application stopped due to exception: {0}",e.getClass().getName());
            ErrorDialog errDlg = new ErrorDialog(new JFrame(), e.getClass().getName(), e.getMessage());
            System.exit(0);
        }
        
        return result;
    }
    
    // *****************
    // ADD TO DB METHODS
    // *****************
    /**
     * 
     * @param model 
     */
    public void addItem2(Model model){
        // Checks table existance and creates them if needed
        if(this.createConnection() == 1){
            try{
                Class.forName("org.sqlite.JDBC");
                Connection c = DriverManager.getConnection(prefix.concat(databaseName));
                
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
            } catch (ClassNotFoundException | SQLException e) {
                // Log exception
                Logger.getLogger("").log(Level.SEVERE, "Application stopped due to exception: {0}",e.getClass().getName());
                ErrorDialog errDlg = new ErrorDialog(new JFrame(), e.getClass().getName(), e.getMessage());
                System.exit(0);
            }
        }
    }
    
    /**
     * 
     * @param user 
     */
    public void addUser(User user){
        // Checks table existance and creates them if needed
        if(this.createConnection() == 1){
            try{
                Class.forName("org.sqlite.JDBC");
                Connection c = DriverManager.getConnection(prefix.concat(databaseName));
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
                c.close();
            } catch (ClassNotFoundException | SQLException e) {
                // Log exception
                Logger.getLogger("").log(Level.SEVERE, "Application stopped due to exception: {0}",e.getClass().getName());
                ErrorDialog errDlg = new ErrorDialog(new JFrame(), e.getClass().getName(), e.getMessage());
                System.exit(0);
            }
        }
    }
    
    /**
     * 
     * @param model
     * @deprecated Use {@link #addItem2(passman.model.Model) addItem2} instead.
     */
    @Deprecated
    public void addItem(Model model){
        /*try{
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
            // Log exception
            Logger.getLogger("").log(Level.SEVERE, "Application stopped due to exception: {0}",e.getClass().getName());
            ErrorDialog errDlg = new ErrorDialog(new JFrame(), e.getClass().getName(), e.getMessage());
            System.exit(0);
        }*/
    }
    
    // *******************
    // GET FROM DB METHODS
    // *******************
    /**
     * 
     * @return 
     */
    public List<Model> getItems2(){
        // Checks table existance and creates them if needed
        if(this.createConnection() == 1){
            try{
                Class.forName("org.sqlite.JDBC");
                Connection c = DriverManager.getConnection(prefix.concat(databaseName));

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
            } catch (ClassNotFoundException | SQLException e) {
                // Log exception
                Logger.getLogger("").log(Level.SEVERE, "Application stopped due to exception: {0}",e.getClass().getName());
                ErrorDialog errDlg = new ErrorDialog(new JFrame(), e.getClass().getName(), e.getMessage());
                System.exit(0);
            }
        }        
        return list;
    }    
    
    /**
     * 
     * @param label
     * @return 
     */
    public Model getItem2(String label){
        Model model = null;
        // Checks table existance and creates them if needed
        if(this.createConnection() == 1){
            try{
                Class.forName("org.sqlite.JDBC");
                Connection c = DriverManager.getConnection(prefix.concat(databaseName));
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
                c.close();
            } catch (ClassNotFoundException | SQLException e) { 
                // Log exception
                Logger.getLogger("").log(Level.SEVERE, "Application stopped due to exception: {0}",e.getClass().getName());
                ErrorDialog errDlg = new ErrorDialog(new JFrame(), e.getClass().getName(), e.getMessage());
                System.exit(0);
            }
        }
        return model;
    }
    
    /**
     * 
     * @param label
     * @return 
     */
    public int getItemID(String label){
        int itemID = -1;
        // Checks table existance and creates them if needed
        if(this.createConnection() == 1){
            try{
                Class.forName("org.sqlite.JDBC");
                Connection c = DriverManager.getConnection(prefix.concat(databaseName));
                
                PreparedStatement stmt = null;
                String sql = "SELECT ID FROM pmj_passwords WHERE "+
                        "ID IN (SELECT PASSWORD_ID FROM pmj_entries WHERE "+
                        "USER_ID IN (SELECT ID FROM pmj_users WHERE "+
                        "USERNAME=?)) AND LABEL=?;";

                stmt = c.prepareStatement(sql);

                stmt.setString(1, Utils.getCurrentUser());
                stmt.setString(2, label);

                ResultSet entries = stmt.executeQuery();

                if(entries.next()){
                    itemID = entries.getInt("ID");                    
                }                
                stmt.close();
                c.close();
            } catch (ClassNotFoundException | SQLException e) {  
                // Log exception
                Logger.getLogger("").log(Level.SEVERE, "Application stopped due to exception: {0}",e.getClass().getName());
                ErrorDialog errDlg = new ErrorDialog(new JFrame(), e.getClass().getName(), e.getMessage());
                System.exit(0);
            }
        }
        return itemID;
    }
    
    /**
     * 
     * @param username
     * @return 
     */
    public User getUser(String username){
        User user = null;
        // Checks table existance and creates them if needed
        if(this.createConnection() == 1){
            try{
                Class.forName("org.sqlite.JDBC");
                Connection c = DriverManager.getConnection(prefix.concat(databaseName));
                Statement stmt = c.createStatement();
                String sql = "SELECT USERNAME, PASSHASH, SALT FROM pmj_users WHERE USERNAME=\""+username+"\";";

                ResultSet entries = stmt.executeQuery(sql);

                if(entries.next()){
                    user = new User(entries.getString("USERNAME"),entries.getBytes("PASSHASH"),entries.getBytes("SALT"));
                }

                stmt.close();
                c.close();
            } catch (ClassNotFoundException | SQLException e) { 
                // Log exception
                Logger.getLogger("").log(Level.SEVERE, "Application stopped due to exception: {0}",e.getClass().getName());
                ErrorDialog errDlg = new ErrorDialog(new JFrame(), e.getClass().getName(), e.getMessage());
                System.exit(0);
            }
        }
        return user;
    }
    
    /**
     * Searches the database using an username and returns the associated ID
     * @param username the string used to search the database
     * @return -1 if user not found, user ID otherwise
     */
    public int getUserID(String username){
        int userID = -1;
        // Checks table existance and creates them if needed
        if(this.createConnection() == 1){
            try{
                Class.forName("org.sqlite.JDBC");
                Connection c = DriverManager.getConnection(prefix.concat(databaseName));
                Statement stmt = c.createStatement();
                String sql = "SELECT ID FROM pmj_users WHERE USERNAME=\""+username+"\";";

                ResultSet entries = stmt.executeQuery(sql);

                if(entries.next()){
                    userID = entries.getInt("ID");
                }
                else{
                    // Log event
                    //Logger.getLogger("").log(Level.SEVERE, "Failed to retrieve USER ID from database");
                }
                stmt.close();
                // Log action
                //Logger.getLogger("").log(Level.SEVERE, "Failed to find table pmj_users.");
                c.close();
            } catch (ClassNotFoundException | SQLException e) {     
                // Log exception
                Logger.getLogger("").log(Level.SEVERE, "Application stopped due to exception: {0}",e.getClass().getName());
                ErrorDialog errDlg = new ErrorDialog(new JFrame(), e.getClass().getName(), e.getMessage());
                System.exit(0);
            }
        }
        return userID;
    }
    
    /**
     * 
     * @param label
     * @return
     * @deprecated Use {@link #getItem2(java.lang.String) getItem2} instead.
     */
    @Deprecated
    public Model getItem(String label){
        /*Model model = null;
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
            // Log exception
            Logger.getLogger("").log(Level.SEVERE, "Application stopped due to exception: {0}",e.getClass().getName());
            ErrorDialog errDlg = new ErrorDialog(new JFrame(), e.getClass().getName(), e.getMessage());
            System.exit(0);
        }
        
        return model;*/
        return null; //temp return
    }
    
    // *****************
    // UPDATE DB METHODS
    // *****************
    /**
     * 
     * @param oldUsername
     * @param newUsername 
     */
    public void updateUsername(String oldUsername, String newUsername){
        // Checks table existance and creates them if needed
        if(this.createConnection() == 1){
            try{
                Class.forName("org.sqlite.JDBC");
                Connection c = DriverManager.getConnection(prefix.concat(databaseName));
                int oldUserID = this.getUserID(oldUsername);
                if(oldUserID > -1){
                    PreparedStatement stmt = null;
                    String sql = "UPDATE pmj_users SET USERNAME=\""+ newUsername +"\" WHERE ID="+oldUserID+";";
                    stmt = c.prepareStatement(sql);

                    stmt.executeUpdate();
                    stmt.close();
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
    
    /**
     * 
     * @param model 
     */
    public void updateItemPassword(Model model){
        // Checks table existance and creates them if needed
        if(this.createConnection() == 1){
            try{
                Class.forName("org.sqlite.JDBC");
                Connection c = DriverManager.getConnection(prefix.concat(databaseName));
                // Updates passwords table
                PreparedStatement stmt = null;
                String sql = "UPDATE pmj_passwords SET PASSWORD=?, SALT=? WHERE ID=?;";

                stmt = c.prepareStatement(sql);

                stmt.setBytes(1, model.getPassword());
                stmt.setBytes(2, model.getSalt());
                stmt.setInt(3, this.getItemID(model.getLabel()));

                stmt.executeUpdate();

                stmt.close();
                c.close();                
            } catch (ClassNotFoundException | SQLException e) {
                // Log exception
                Logger.getLogger("").log(Level.SEVERE, "Application stopped due to exception: {0}",e.getClass().getName());
                ErrorDialog errDlg = new ErrorDialog(new JFrame(), e.getClass().getName(), e.getMessage());
                System.exit(0);
            }
        }
    }
    
    /**
     * 
     * @param user 
     */
    public void updatePassword(User user){
        // Checks table existance and creates them if needed
        if(this.createConnection() == 1){
            try{
                Class.forName("org.sqlite.JDBC");
                Connection c = DriverManager.getConnection(prefix.concat(databaseName));
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
                c.close();
            } catch (ClassNotFoundException | SQLException e) {
                // Log exception
                Logger.getLogger("").log(Level.SEVERE, "Application stopped due to exception: {0}",e.getClass().getName());
                ErrorDialog errDlg = new ErrorDialog(new JFrame(), e.getClass().getName(), e.getMessage());
                System.exit(0);
            }
        }
    }
    
    // **********************
    // REMOVE FROM DB METHODS
    // **********************
    /**
     * 
     * @param model 
     */
    public void removeItem2(Model model){
        // Checks table existance and creates them if needed
        if(this.createConnection() == 1){
            try{
                Class.forName("org.sqlite.JDBC");
                Connection c = DriverManager.getConnection(prefix.concat(databaseName));
                try (Statement stmt = c.createStatement()) {
                    String sql = "DELETE FROM pmj_entries WHERE PASSWORD_ID IN ("+
                            "SELECT ID FROM pmj_passwords WHERE LABEL=\""+model.getLabel()+"\");";

                    stmt.executeUpdate(sql);
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
    
    /**
     * 
     * @param user 
     */
    public void removeUser(User user){
        // Checks table existance and creates them if needed
        if(this.createConnection() == 1){
            try{
                Class.forName("org.sqlite.JDBC");
                Connection c = DriverManager.getConnection(prefix.concat(databaseName));
                if(this.getUser(user.getUsername()) != null){
                    PreparedStatement stmt = null;
                    String sql = "DELETE FROM pmj_users WHERE USERNAME=?";
                    stmt = c.prepareStatement(sql);

                    stmt.setString(1, user.getUsername());

                    stmt.executeUpdate();
                    stmt.close();
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
    
    /**
     * 
     * @param model
     * @deprecated Use {@link #removeItem2(passman.model.Model) removeItem2} instead. 
     */
    @Deprecated
    public void removeItem(Model model){
        /*try{
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
            // Log exception
            Logger.getLogger("").log(Level.SEVERE, "Application stopped due to exception: {0}",e.getClass().getName());
            ErrorDialog errDlg = new ErrorDialog(new JFrame(), e.getClass().getName(), e.getMessage());
            System.exit(0);
        }*/
    }    
}
