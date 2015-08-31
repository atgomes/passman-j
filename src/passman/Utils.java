/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package passman;

import java.awt.CardLayout;
import java.awt.Component;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JProgressBar;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import passman.db.Crypt;
import passman.db.SQLiteJDBC;
import passman.model.CryptModel;
import passman.model.ErrorDialog;
import passman.model.Model;
import passman.model.PassGenerator;
import passman.model.PasswordOptions;
import passman.model.User;

/**
 * Utils is a class that contains and groups relevant utilities commonly used by
 * this application.
 * 
 * @author Andre Gomes
 */
public class Utils {
    private static String CURRENT_USER = ""; //NOI18N
    
    /**
     * Reloads entries from database.
     * This method retrieves all entries from database and adds them to the 
     * model of the Swing list passed as parameter.
     * 
     * @param jList1 swing list to update 
     */
    public void refreshView(JList jList1){
        SQLiteJDBC sqlite = new SQLiteJDBC();
        List<Model> list = new ArrayList<>(sqlite.getItems2());

        DefaultListModel<Model> listModel = new DefaultListModel();
        list.stream().filter((obj) -> (!obj.getLabel().isEmpty() && !obj.getUsername().isEmpty() && obj.getPassword().length > 0)).forEach((obj) -> {
            listModel.addElement(obj);
        });
        jList1.setModel(listModel);
    }
    
    /**
     * Reloads entries from database.
     * Similar to {@link #refreshView(javax.swing.JList) refreshView} but instead
     * of reloading items from database it uses the passed list as source to 
     * populate the swing list.
     * 
     * @param jList1 swing list to update
     * @param list source list used as source 
     */
    public void refreshView(JList jList1, List<Model> list){
        DefaultListModel<Model> listModel = new DefaultListModel();
        list.stream().
                forEach((obj) -> {
                    listModel.addElement(obj);
        });
        jList1.setModel(listModel);
    }
    
    /**
     * Save the password options passed as argument to the default properties
     * file.
     * This method calls {@link #openOrCreatePropertiesFile()} retrieving
     * the properties present in the file and then updates them using the object
     * passed as source.
     * 
     * @param pOpts PasswordOptions source object containing password options
     */
    public static void savePasswordOptions(PasswordOptions pOpts){
        try{
            // Load current parameters
            Properties props = openOrCreatePropertiesFile();
            // Set desired parameters
            props.setProperty("Password.length", String.valueOf(pOpts.getpLength())); //NOI18N
            props.setProperty("Password.symbols", String.valueOf(pOpts.isSymbols())); //NOI18N
            props.setProperty("Password.safesymbols", String.valueOf(pOpts.isSymbolsSafe())); //NOI18N
            props.setProperty("Password.digits", String.valueOf(pOpts.isDigits())); //NOI18N
            props.setProperty("Password.uppercase", String.valueOf(pOpts.isUpperCase())); //NOI18N
            
            // Save properties to file
            File f = new File("passman.properties"); //NOI18N
            OutputStream out = new FileOutputStream(f);
            props.store(out,"Language properties"); //NOI18N
            out.close();
            
        } catch (Exception e){
            // Log exception
            Logger.getLogger("").log(Level.SEVERE, "Application stopped due to exception: {0}",e.getClass().getName()); //NOI18N
            ErrorDialog errDlg = new ErrorDialog(new JFrame(), e.getClass().getName(), e.getMessage());
            System.exit(0);
        }
    }
    
    /**
     * Loads password options from file.
     * This method calls {@link #openOrCreatePropertiesFile()} that retrieves
     * all properties associated with the application and then populates a 
     * PasswordOptions object with the password related properties.
     * 
     * @return PasswordOptions object containing password related properties
     */
    public static PasswordOptions loadPasswordOptions(){
        PasswordOptions pOpts = null;
        Properties props = openOrCreatePropertiesFile();
        InputStream is = null;

        // First try loading from the current directory
        if(props.size()<=0){
            // Log event
            Logger.getLogger("").log(Level.SEVERE, "Application stopped due to internal error: properties operation error"); //NOI18N
            String title = "Properties operation error"; //NOI18N
            String message = "It was not possible to create a properties file, please try again later. "+ //NOI18N
                    "If this problem persists please report this error at https://bitbucket.org/atgomes/publicfiles/issues"; //NOI18N
            ErrorDialog errorDlg = new ErrorDialog(new JFrame(), title, message);
            System.exit(0);
        } else{
            try{
                pOpts = new PasswordOptions(Integer.valueOf(props.getProperty("Password.length","12")), //NOI18N
                        Boolean.valueOf(props.getProperty("Password.symbols","true")),  //NOI18N
                        Boolean.valueOf(props.getProperty("Password.safesymbols","true")),  //NOI18N
                        Boolean.valueOf(props.getProperty("Password.digits","true")),  //NOI18N
                        Boolean.valueOf(props.getProperty("Password.uppercase","true"))); //NOI18N

            } catch(Exception e){
                // Log exception
                Logger.getLogger("").log(Level.SEVERE, "Application stopped due to exception: {0}",e.getClass().getName()); //NOI18N
                ErrorDialog errorDlg = new ErrorDialog(new JFrame(), e.getClass().getName(), e.getMessage());
                System.exit(0);
            }
        }
        
        return pOpts;
    }
    
    /**
     * Saves language and location properties to file.
     * This method calls {@link #openOrCreatePropertiesFile() } that retrieves
     * all properties associated with the application and then updates the fields
     * Language and Country using the strings passed as parameters.
     * 
     * @param language string indicating the language represented using ISO-639
     * @param country string indicating the country represented using ISO-3166
     */
    public void saveParamChanges(String language, String country){
        try{
            // Load current parameters
            Properties props = openOrCreatePropertiesFile();
            // Set desired parameters
            props.setProperty("Country", country); //NOI18N
            props.setProperty("Language", language); //NOI18N
            
            // Save properties to file
            File f = new File("passman.properties"); //NOI18N
            OutputStream out = new FileOutputStream(f);
            props.store(out,"Language properties"); //NOI18N
            out.close();
            
            // Log action
            Logger.getLogger("").log(Level.INFO, "Language properties saved. Language: {0}",language); //NOI18N
            
        } catch (Exception e){
            // Log exception
            Logger.getLogger("").log(Level.SEVERE, "Application stopped due to exception: {0}",e.getClass().getName()); //NOI18N
            ErrorDialog errDlg = new ErrorDialog(new JFrame(), e.getClass().getName(), e.getMessage());
            System.exit(0);
        }
    }
    
    /**
     * Loads language and location properties from file.
     * This method calls {@link #openOrCreatePropertiesFile() } that retrieves
     * all properties associated with the application and then populates an 
     * ArrayList object with the language and country properties.
     * 
     * @return ArrayList of strings indicating the language and country as
     * strings encoded with ISO-639 and ISO-3166, respectively.
     */
    public static ArrayList<String> loadParams(){
        Properties props = openOrCreatePropertiesFile();
        InputStream is = null;

        // First try loading from the current directory
        if(props.size()<=0){
            // Log event
            Logger.getLogger("").log(Level.SEVERE, "Application stopped due to internal error: properties operation error"); //NOI18N
            String title = "Properties operation error"; //NOI18N
            String message = "It was not possible to create a properties file, please try again later. "+ //NOI18N
                    "If this problem persists please report this error at https://bitbucket.org/atgomes/publicfiles/issues"; //NOI18N
            ErrorDialog errorDlg = new ErrorDialog(new JFrame(), title, message);
            System.exit(0);
        }   
        
        ArrayList<String> list = new ArrayList<>();
        list.add(props.getProperty("Country","UK")); //NOI18N
        list.add(props.getProperty("Language","en")); //NOI18N
        
        // Log action
        Logger.getLogger("").log(Level.INFO, "Language properties loaded."); //NOI18N
        
        return list;

    }
    
    /**
     * Loads application properties from file.
     * If a properties file exists this methods loads all entries and populates 
     * a java.util.Properties object. If the file doesn't exist it will create 
     * one using default properties before populating said object.
     * 
     * @return Properties object containing all application properties.
     */
    public static Properties openOrCreatePropertiesFile(){
        InputStream is = null;
        Properties props = new Properties();
        try {
            File f = new File("passman.properties"); //NOI18N
            if(!f.createNewFile()){
                is = new FileInputStream( f );
                props.load(is);
            } else{
                OutputStream out = new FileOutputStream(f);
                SimpleDateFormat sdfDate = new SimpleDateFormat("yyyyMMdd"); //NOI18N
                Date now = new Date();
                String strDate = sdfDate.format(now);
                
                props.setProperty("Application.buildnumber", strDate); //NOI18N
                props.setProperty("Application.version", "1.0.0.${Application.buildnumber}"); //NOI18N
                props.setProperty("Application.title", "PassManJ ${Application.version}"); //NOI18N
                props.setProperty("Language", "pt"); //NOI18N
                props.setProperty("Country", "PT"); //NOI18N
                props.setProperty("Password.length", "12"); //NOI18N
                props.setProperty("Password.symbols", "true"); //NOI18N
                props.setProperty("Password.safesymbols", "true"); //NOI18N
                props.setProperty("Password.digits", "true"); //NOI18N
                props.setProperty("Password.uppercase", "true"); //NOI18N
                
                props.store(out, "File created at runtime!"); //NOI18N
                out.close();
            }
        }
        catch ( IOException e ) {
            is = null;
            // Log exception
            Logger.getLogger("").log(Level.SEVERE, "Application stopped due to exception: {0}",e.getClass().getName()); //NOI18N
            ErrorDialog errorDlg = new ErrorDialog(new JFrame(), e.getClass().getName(), e.getMessage());
            System.exit(0);
        }
        
        return(props);
    }
    
    /**
     * Retrieves application title from properties file.
     * This method calls {@link #openOrCreatePropertiesFile() } that retrieves
     * all properties associated with the application and then creates a string
     * using the title, version and buildnumber property fields.
     * 
     * @return string representing the application main window title
     */
    public static String getTitleFromProps(){
        Properties props = openOrCreatePropertiesFile();
        
        String title1 = props.getProperty("Application.title"); //NOI18N
        String title2 = props.getProperty("Application.version"); //NOI18N
        String title3 = props.getProperty("Application.buildnumber"); //NOI18N
        
        String finalTitle = title1.replace("${Application.version}", title2.replace("${Application.buildnumber}", title3)); //NOI18N
        
        return finalTitle;
    }
    
    /**
     * Verifies the existence of a database file in the default path.
     * This method verifies if a database file exists calling
     * {@link passman.db.SQLiteJDBC#createConnection() } if it doesn't. That
     * method will create the required database file and its tables.
     */
    public static void verifyDB(){
        File f = new File("./passman.s3db"); //NOI18N
        if(!f.exists()){
            SQLiteJDBC sqlite = new SQLiteJDBC();
            sqlite.createConnection();
        }
    }
    
    /**
     * Add entry to database.
     * This method adds an entry to the database using the values passed as
     * parameters. The values validity is verified and the password is properly
     * encrypted using the current user key and a newly generated random salt. 
     * A passman.model.Model object is created and saved to the database using
     * {@link passman.db.SQLiteJDBC#addItem2(passman.model.Model)} method.
     * 
     * @param label the entry label as string
     * @param username the entry username as string
     * @param plainTextPassword the entry passwords as string
     * @param comment the entry details as string
     * @return true if the entry was successfully saved or false otherwise
     */
    public static boolean addToDBFromUI(String label, String username, String plainTextPassword, String comment){
        boolean result = false;
        // Ensure no mandatory fields are submitted empty
        if(!label.isEmpty() && !username.isEmpty() && !plainTextPassword.isEmpty()){
            SQLiteJDBC sqlite = new SQLiteJDBC();
            if(sqlite.getItemID(label)<0){
                // get utf-8 bytes from string
                byte[] passUTF8 = plainTextPassword.getBytes(StandardCharsets.UTF_8);

                // gets the current user

                User currentUser = sqlite.getUser(CURRENT_USER);
                if(currentUser == null){
                    // Log event
                    Logger.getLogger("").log(Level.SEVERE, "It was not possible to retrieve user \"{0}\" from database.",CURRENT_USER); //NOI18N
                    ErrorDialog errDlg = new ErrorDialog(new JFrame(), "User not found", "User "+ CURRENT_USER +" was not found on the database."); //NOI18N
                } else{
                    // Use user password to encrypt password bytes
                    CryptModel cpMdl = Crypt.encrypt(currentUser.getSecurePassword(), null, passUTF8);
                    if(cpMdl.encryptedPassword == null){
                        // Log event
                        Logger.getLogger("").log(Level.SEVERE, "Encryption failed due to unknown error."); //NOI18N
                        ErrorDialog errDlg = new ErrorDialog(new JFrame(), "Encryption error", "Encryption failed due to unknown error."); //NOI18N
                    } else{
                        // save data to DB
                        Model newModel = new Model(label, username, cpMdl.encryptedPassword, cpMdl.salt, comment);
                        //sqlite.addItem(newModel);
                        sqlite.addItem2(newModel);
                        result = true;
                        // Log action
                        Logger.getLogger("").log(Level.INFO, "Entry with label {0} added to database.", newModel.getLabel());
                    }
                }
            } else{
                ErrorDialog errDlg = new ErrorDialog(new JFrame(), 
                    java.util.ResourceBundle.getBundle("passman/Bundle").getString("INVALIDLABEL"), java.util.ResourceBundle.getBundle("passman/Bundle").getString("INVALIDLABELMSG"));
            }
        } else{
            ErrorDialog errDlg = new ErrorDialog(new JFrame(), 
                    java.util.ResourceBundle.getBundle("passman/Bundle").getString("MANDATORYFIELDS"), java.util.ResourceBundle.getBundle("passman/Bundle").getString("MANDATORYFIELDSMSG"));
        }
        return result;
    }
    
    /**
     * Decrypts a password and returns it as a plain text string.
     * Using the salt passed as an argument and the user unique key retrieved
     * from the database it attempts to decrypt the password passed as an argument.
     * 
     * @param encryptedMessage password to decrypt
     * @param salt random byte array used to decrypt the associated password
     * @return decrypted password as a plain text string UTF-8 encoded
     */
    public static String getFromDBToUI(byte[] encryptedMessage, byte[] salt){
        // decrypts password using current user values
        SQLiteJDBC sqlite = new SQLiteJDBC();
        User currentUser = sqlite.getUser(CURRENT_USER);
        if(currentUser == null){
            // Log event
            Logger.getLogger("").log(Level.SEVERE, "It was not possible to retrieve user \"{0}\" from database.",CURRENT_USER); //NOI18N
            return(null);
        } else{
            byte[] decryptedPass = Crypt.decrypt(currentUser.getSecurePassword(), 
                salt, encryptedMessage);

            // convert decrypted byte array to String using UTF-8 encoding
            String plainTextPassword = new String(decryptedPass,StandardCharsets.UTF_8);
            
            return(plainTextPassword);
        }
    }
    
    /**
     * Utility used to facilitate application navigation.
     * This method updates the CardLayout manager showing the card associated 
     * with the string location passed as an argument.
     * 
     * This method always shows the graphic interfaces of user creation, login 
     * and language options but for everything else it verifies if an user is
     * currently logged in to show it.
     * 
     * @param mainPanel panel that layout manager uses as its base
     * @param location requested card represented as an unique string
     */
    public static void goToScreen(JPanel mainPanel, String location){
        if(location.equals("CREATEUSER") || location.equals("LOGIN") || location.equals("LANGUAGE")){ //NOI18N
            CardLayout card = (CardLayout)mainPanel.getLayout();
            card.show(mainPanel, location);
        } else{
            if(CURRENT_USER != ""){ //NOI18N
                CardLayout card = (CardLayout)mainPanel.getLayout();
                card.show(mainPanel, location);
            }
        }
    }
    
    /**
     * Sets the CURRENT_USER string.
     * CURRENT_USER is a passman.Utils string used to represent the user
     * currently logged in. This method is used to set it.
     * 
     * @param username string to set as the current user
     */
    public static void setCurrentUser(String username){
        CURRENT_USER = username;
    }
    
    /**
     * Gets the CURRENT_USER string.
     * CURRENT_USER is a passman.Utils string used to represent the user
     * currently logged in. This method is used to get it.
     * 
     * @return CURRENT_USER
     */
    public static String getCurrentUser(){
        return(CURRENT_USER);
    }
    
    /**
     * Compares the supplied username and password with data from the database 
     * in order to attempt a login.
     * This method verifies the existence of the supplied username in the
     * database. If it exists, this retrieves the associated salt byte array
     * and uses it to verify the validity of the supplied password using
     * {@link passman.db.Crypt#verifyPasswordValidity(java.lang.String, byte[], byte[]) }.
     * 
     * If the validity is confirmed then this proceeds to set the current user
     * as the string supplied.
     * 
     * @param username the username of the user trying to login
     * @param password the password of the user trying to login
     * @return true if the login was successful, false otherwise
     */
    public static boolean tryLogin(String username, String password){
        // fetches the user from DB
        SQLiteJDBC sqlite = new SQLiteJDBC();
        User compareUser = sqlite.getUser(username);
        if(compareUser != null){
            byte[] salt = compareUser.getSaltArray();
            byte[] secPassword = compareUser.getSecurePassword();
            byte[] result = Crypt.verifyPasswordValidity(password, salt, secPassword);

            if(result != null){
                // Sets the current user
                Utils.setCurrentUser(compareUser.getUsername());
                
                // Log action
                Logger.getLogger("").log(Level.INFO, "User {0} successfully logged in.",Utils.getCurrentUser()); //NOI18N
                return true;
            } else{
                // Log event
                Logger.getLogger("").log(Level.INFO, "User tried to login as {0} with a wrong password.", compareUser.getUsername()); //NOI18N
                return false;
            }
        } else{
            return false;
        }
    }
    
    /**
     * Utility used to enable/disable menus.
     * This method toggles all sub-menus of the menu supplied as parameter 
     * except for the language options menu which must be enabled at all times.
     * 
     * @param menu parent JMenu object to be used
     * @param entering boolean indicating if the logic preceding this method call
     * is either a successful login (true) or a logout (false)
     */
    public static void toggleMenus(JMenu menu, boolean entering){
        //if(entering){ // user logged in successfully
        Component[] menuComps = menu.getMenuComponents();
            for(Component comp : menuComps){
                if(!"languageMenuItem".equals(comp.getName())){ //NOI18N
                    comp.setEnabled(!comp.isEnabled());
                }
            }
    }

    /**
     * Terminates the access to the protected areas of the application.
     * This method nullifies the CURRENT_USER variable, clears the swing list 
     * and sets the login panel as the current screen. This renders access to
     * protected data impossible until the user logs in again.
     * 
     * @param jList1 swing list to clear
     * @param mainPanel CardLayout base panel
     */
    public static void logout(JList jList1, JPanel mainPanel){
        // Clears current user
        Utils.setCurrentUser(null);
        
        // Clears list items
        DefaultListModel<Model> listModel = new DefaultListModel();
        jList1.setModel(listModel);
        
        // Goes to login page
        Utils.goToScreen(mainPanel, "LOGIN"); //NOI18N
    }
    
    /**
     * Updates the database data associated with the supplied username.
     * Updates the entry represented by the username supplied changing its 
     * USERNAME field to the newUsername string passed as parameter.
     * 
     * @param password user password
     * @param username old username to be changed
     * @param newUsername new username to be set
     * @return 0 (zero) if the entry was successfully updated, 1 (one) if the
     * supplied password was wrong, 2 (two) if the new username if equal to the
     * old username (case is ignored), 3 (three) if the new username supplied
     * already exists or a negative integer if it was impossible to verify the
     * existence of the current user or other error occurred.
     */
    public static int changeUsername(String password, String username, String newUsername){
        int returnValue = -1;
        // fetches the user from DB
        SQLiteJDBC sqlite = new SQLiteJDBC();
        User compareUser = sqlite.getUser(username);
        if(compareUser != null){
            byte[] salt = compareUser.getSaltArray();
            byte[] secPassword = compareUser.getSecurePassword();
            byte[] result = Crypt.verifyPasswordValidity(password, salt, secPassword);

            if(result != null){
                // Check if new username is different from old one and doesn't yet exist on the DB
                if(username.equalsIgnoreCase(newUsername)){
                    returnValue = 2;
                } else{
                    if(sqlite.getUserID(newUsername) > -1){
                        returnValue = 3;
                    } else{
                        // Change username
                        sqlite.updateUsername(username, newUsername);
                        // Log event
                        Logger.getLogger("").log(Level.INFO, "Username {0} changed to {1}",new Object[]{username,newUsername});
                        returnValue = 0;
                    }
                }   
            } else{
                returnValue = 1;
            }
        }
        return returnValue;
    }
    
    /**
     * 
     * 
     * @param jBar
     * @param oldPassword
     * @param newPassword
     * @deprecated use {@link #changeSinglePassword(passman.model.User, passman.model.User, passman.model.Model, java.lang.String) }
     * instead.
     */
    @Deprecated
    public static void changePassword(JProgressBar jBar, String oldPassword, String newPassword){
        
        // fetches the user from DB
        SQLiteJDBC sqlite = new SQLiteJDBC();
        User compareUser = sqlite.getUser(getCurrentUser());
        if(compareUser != null){
            byte[] salt = compareUser.getSaltArray();
            byte[] secPassword = compareUser.getSecurePassword();
            byte[] result = Crypt.verifyPasswordValidity(oldPassword, salt, secPassword);

            if(result != null){
                // Creates new secure password and salt
                ArrayList<byte[]> list = Crypt.getSecurePassword(newPassword);
                User user = new User(getCurrentUser(), list.get(0), list.get(1));
                
                // Retrieves all saved entries
                List<Model> allItems = sqlite.getItems2();
                
                for(Model item : allItems){
                    // decrypt password
                    byte[] pass = Crypt.decrypt(compareUser.getSecurePassword(), 
                            item.getSalt(), item.getPassword());
                    
                    // encrypt password with new password/salt
                    CryptModel cpMdl = Crypt.encrypt(user.getSecurePassword(), null, pass);
                    
                    // update item in DB
                    Model newModel = new Model(item.getLabel(), item.getUsername(), cpMdl.encryptedPassword, cpMdl.salt, item.getComment());
                    
                    sqlite.updateItemPassword(newModel);
                }              
                
                // Change password
                sqlite.updatePassword(user);
                
                
            } else{
                // TRATAR DE DIZER QUE A PASSWORD ESTÁ ERRADA
            }
        }
    }
    
    /**
     * Updates the password associated with the supplied username.
     * Updates a database entry by changing both its secure password and salt 
     * byte arrays.
     * 
     * @param currentUser User object that represents the current user
     * @param newUser User object that represents the new user, more specifically
     * its secure password
     * @param item Model object to be updated
     * @param oldPassword string text representing the password supplied to 
     * allow this operation
     */
    public static void changeSinglePassword(User currentUser, User newUser, Model item, String oldPassword){
        // fetches the user from DB
        SQLiteJDBC sqlite = new SQLiteJDBC();
        if(currentUser != null){
            byte[] salt = currentUser.getSaltArray();
            byte[] secPassword = currentUser.getSecurePassword();
            byte[] result = Crypt.verifyPasswordValidity(oldPassword, salt, secPassword);

            if(result != null){
                // decrypt password
                byte[] pass = Crypt.decrypt(currentUser.getSecurePassword(), 
                        item.getSalt(), item.getPassword());

                // encrypt password with new password/salt
                CryptModel cpMdl = Crypt.encrypt(newUser.getSecurePassword(), null, pass);

                // update item in DB
                Model newModel = new Model(item.getLabel(), item.getUsername(), cpMdl.encryptedPassword, cpMdl.salt, item.getComment());

                sqlite.updateItemPassword(newModel);
            } else{
                // TRATAR DE DIZER QUE A PASSWORD ESTÁ ERRADA
            }
        }
    }
    
    /**
     * Adds a new entry to the user database.
     * 
     * @param username string to be saved in the database
     * @param password string to be encrypted and then saved in the database
     * @return 0 (zero) if the user was added successfully, 1 (one) if the 
     * username supplied already exists or a negative integer for unknown errors
     */
    public static int createNewUser(String username, String password){
        int result = -1;
        SQLiteJDBC sqlite = new SQLiteJDBC();
        // Checks if username already exists
        if(sqlite.getUserID(username) > -1){
            result = 1;
        } else{
            // Creates hash pass and user
            ArrayList<byte[]> list = Crypt.getSecurePassword(password);
            User user = new User(username, list.get(0), list.get(1));

            // adds user to DB
            sqlite.addUser(user);
            result = 0;
        }
        return(result);
    }
    
    /**
     * Utility used to clear the supplied text fields or similar components.
     * The fields are cleared by setting its content as an empty string.
     * @param componentList list containing the components which must be cleared
     */
    public static void clearTextFields(Component[] componentList){
        for(Component comp : componentList){
            if(comp.getClass().getName().endsWith("JTextField")){//NOI18N
                JTextField txt = (JTextField)comp;
                txt.setText("");//NOI18N
            }
            if(comp.getClass().getName().endsWith("JPasswordField")){//NOI18N
                JPasswordField pass = (JPasswordField)comp;
                pass.setText("");//NOI18N
            }
            if(comp.getClass().getName().endsWith("JTextArea")){ //NOI18N
                JTextArea txt = (JTextArea)comp;
                txt.setText("");//NOI18N
            }
        }
    }
    
    /**
     * Utility method used to update UI components upon changing in selection at
     * the swing list.
     * 
     * @param jList1 swing list containing passman.model.Model items
     * @param labelShow text field related to the label field of the selected item
     * @param usernameShow text field related to the username field of the selected item
     * @param passwordShow text field related to the password field of the selected item
     * @param commentShow text field related to the comment field of the selected item
     * @param toggleShowPassword button used to toggle password field visibility
     * @param removeEntryBtn button used to trigger the removal of the selected item
     * @param copyToClip  button used to copy the selected item's password to the clipboard
     */
    public static void presentSelectedEntry(JList jList1, JTextField labelShow, 
            JTextField usernameShow, JTextField passwordShow, 
            JTextArea commentShow, JToggleButton toggleShowPassword, JButton removeEntryBtn, JButton copyToClip){
        
        if(jList1.getSelectedIndex()>-1){
            // convert jList1 item to a Model item
            Model thisModel = (Model)jList1.getSelectedValue();
            
            // shows label, username and comments
            labelShow.setText(thisModel.getLabel());
            usernameShow.setText(thisModel.getUsername());
            commentShow.setText(thisModel.getComment());
            
            // only shows password if toggle is selected
            if(toggleShowPassword.isSelected()){
                String plainTextPassword = Utils.getFromDBToUI(thisModel.getPassword(),thisModel.getSalt());
                
                passwordShow.setText(plainTextPassword);
            } else{
                passwordShow.setText("*********"); //NOI18N
            }
            // Enable buttons
            toggleEnabledButtons(true, new Component[]{removeEntryBtn, toggleShowPassword, copyToClip});
        }
        else{
            // Disable buttons
            toggleEnabledButtons(false, new Component[]{removeEntryBtn, toggleShowPassword, copyToClip});
        }
    }
    
    /**
     * Utility used to toggle the enable property of the supplied buttons.
     * Enabled property of the supplied buttons is set to the value passed as 
     * parameter.
     * @param value boolean to set buttons enable property
     * @param buttons array containing buttons to affect
     */
    public static void toggleEnabledButtons(boolean value, Component[] buttons){
        for(Component button : buttons){
            if(button.getClass().getName().endsWith("JToggleButton")){//NOI18N
                JToggleButton btn = (JToggleButton)button;
                btn.setEnabled(value);
            } else{
                JButton btn = (JButton)button;
                btn.setEnabled(value);
            }
        }
    }
    
    /**
     * Loads password properties from file and generates a new random plain text
     * password.
     * @return plain text random password
     */
    public static String generateNewRandomPassword(){
        PasswordOptions pOpts = loadPasswordOptions();
        return PassGenerator.generate(pOpts.getpLength(), pOpts.isSymbols(), 
                pOpts.isSymbolsSafe(), pOpts.isDigits(), pOpts.isUpperCase());
    }
    
    /**
     * Deletes an account by removing all entries associated with the user and 
     * then removing the user.
     * Only deletes an account if it is the current account user has logged in as.
     * @param username value of the username field of the entry to be deleted
     * @param password password used to verify that the current user is able to
     * delete the entry represented by the username field supplied
     * @return true if the account was successfully deleted, false otherwise
     */
    public static boolean deleteAccount(String username, String password){
        if(username.equals(Utils.getCurrentUser())){
            // fetches the user from DB
            SQLiteJDBC sqlite = new SQLiteJDBC();
            User compareUser = sqlite.getUser(username);
            if(compareUser != null){
                byte[] salt = compareUser.getSaltArray();
                byte[] secPassword = compareUser.getSecurePassword();
                byte[] result = Crypt.verifyPasswordValidity(password, salt, secPassword);

                if(result != null){
                    // Delete all entries from pmj_entries datatable
                    List<Model> allItems = sqlite.getItems2();

                    for(Model item : allItems){
                        sqlite.removeItem2(item);
                    }

                    // Deletes user from pmj_users
                    sqlite.removeUser(compareUser);
                    return true;
                } else{
                    ErrorDialog errDlg = new ErrorDialog(new JFrame(), java.util.ResourceBundle.getBundle("passman/Bundle").getString("AUTHFAILED"), java.util.ResourceBundle.getBundle("passman/Bundle").getString("LOGINFAILEDMSG"));
                    return false;
                    // Log event
                    //Logger.getLogger("").log(Level.INFO, "User tried to login as {0} with a wrong password.", compareUser.getUsername()); //NOI18N

                }
            } else{
                ErrorDialog errDlg = new ErrorDialog(new JFrame(), java.util.ResourceBundle.getBundle("passman/Bundle").getString("AUTHFAILED"), java.util.ResourceBundle.getBundle("passman/Bundle").getString("LOGINFAILEDMSG"));
                return false;
            }
        } else{
            ErrorDialog errDlg = new ErrorDialog(new JFrame(), java.util.ResourceBundle.getBundle("passman/Bundle").getString("ACCESSDENIED"), java.util.ResourceBundle.getBundle("passman/Bundle").getString("ACCESSDENIEDMSG"));
            return false;
        }
    }
}
