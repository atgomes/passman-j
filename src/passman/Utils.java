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
 *
 * @author Andre Gomes
 */
public class Utils {
    private static String CURRENT_USER = ""; //NOI18N
            
    public void refreshView(JList jList1, JPanel mainPanel){
        SQLiteJDBC sqlite = new SQLiteJDBC();
        List<Model> list = new ArrayList<>(sqlite.getItems2());

        DefaultListModel<Model> listModel = new DefaultListModel();
        list.stream().filter((obj) -> (!obj.getLabel().isEmpty() && !obj.getUsername().isEmpty() && obj.getPassword().length > 0)).forEach((obj) -> {
            listModel.addElement(obj);
        });
        jList1.setModel(listModel);
    }
    
    public void refreshView(JList jList1, JPanel mainPanel, List<Model> list){
        DefaultListModel<Model> listModel = new DefaultListModel();
        for(Model obj : list){
            listModel.addElement(obj);
        }
        jList1.setModel(listModel);
    }
    
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
            
            // Log action
            Logger.getLogger("").log(Level.INFO, "Password properties saved."); //NOI18N
        } catch (Exception e){
            // Log exception
            Logger.getLogger("").log(Level.SEVERE, "Application stopped due to exception: {0}",e.getClass().getName()); //NOI18N
            ErrorDialog errDlg = new ErrorDialog(new JFrame(), e.getClass().getName(), e.getMessage());
            System.exit(0);
        }
    }
    
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

                // Log action
                Logger.getLogger("").log(Level.INFO, "Password properties loaded."); //NOI18N
            } catch(Exception e){
                // Log exception
                Logger.getLogger("").log(Level.SEVERE, "Application stopped due to exception: {0}",e.getClass().getName()); //NOI18N
                ErrorDialog errorDlg = new ErrorDialog(new JFrame(), e.getClass().getName(), e.getMessage());
                System.exit(0);
            }
        }
        
        return pOpts;
    }
    
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
    
    public static String getTitleFromProps(){
        Properties props = openOrCreatePropertiesFile();
        
        String title1 = props.getProperty("Application.title"); //NOI18N
        String title2 = props.getProperty("Application.version"); //NOI18N
        String title3 = props.getProperty("Application.buildnumber"); //NOI18N
        
        String finalTitle = title1.replace("${Application.version}", title2.replace("${Application.buildnumber}", title3)); //NOI18N
        
        return finalTitle;
    }
    
    public static void verifyDB(){
        File f = new File("./passman.s3db"); //NOI18N
        if(!f.exists()){
            SQLiteJDBC sqlite = new SQLiteJDBC();
            sqlite.createConnection();
        }
    }
    
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
    
    public static void setCurrentUser(String username){
        CURRENT_USER = username;
    }
    
    public static String getCurrentUser(){
        return(CURRENT_USER);
    }
    
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
    
    public static void toggleMenus(JMenu menu, boolean entering){
        //if(entering){ // user logged in successfully
        Component[] menuComps = menu.getMenuComponents();
            for(Component comp : menuComps){
                if(!"languageMenuItem".equals(comp.getName())){ //NOI18N
                    comp.setEnabled(!comp.isEnabled());
                }
            }
    }
    
    public static void goWithSearch(){
        
    }
    
    public static void logout(JList jList1, JPanel mainPanel){
        // Clears current user
        Utils.setCurrentUser(null);
        
        // Clears list items
        DefaultListModel<Model> listModel = new DefaultListModel();
        jList1.setModel(listModel);
        
        // Goes to login page
        Utils.goToScreen(mainPanel, "LOGIN"); //NOI18N
    }
    
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
                        returnValue = 0;
                    }
                }   
            } else{
                returnValue = 1;
            }
        }
        return returnValue;
    }
    
    public static void changePassword(JProgressBar jBar, String oldPassword, String newPassword){
        jBar.setVisible(true);
        jBar.setMinimum(1);
        
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
                
                jBar.setMaximum(allItems.size());
                int i = 1;
                jBar.setValue(i);
                
                for(Model item : allItems){
                    // decrypt password
                    byte[] pass = Crypt.decrypt(compareUser.getSecurePassword(), 
                            item.getSalt(), item.getPassword());
                    
                    // encrypt password with new password/salt
                    CryptModel cpMdl = Crypt.encrypt(user.getSecurePassword(), null, pass);
                    
                    // update item in DB
                    Model newModel = new Model(item.getLabel(), item.getUsername(), cpMdl.encryptedPassword, cpMdl.salt, item.getComment());
                    
                    sqlite.updateItemPassword(newModel);
                    jBar.setValue(i);
                    ++i;
                }              
                
                // Change password
                sqlite.updatePassword(user);
                
                
            } else{
                // TRATAR DE DIZER QUE A PASSWORD ESTÁ ERRADA
            }
        }
    }
    
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
    
    public static String generateNewRandomPassword(){
        PasswordOptions pOpts = loadPasswordOptions();
        return PassGenerator.generate(pOpts.getpLength(), pOpts.isSymbols(), 
                pOpts.isSymbolsSafe(), pOpts.isDigits(), pOpts.isUpperCase());
    }
}
