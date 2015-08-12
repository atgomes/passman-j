/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package passman;

import java.awt.CardLayout;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import passman.db.Crypt;
import passman.db.SQLiteJDBC;
import passman.model.CryptModel;
import passman.model.ErrorDialog;
import passman.model.Model;
import passman.model.User;

/**
 *
 * @author Andre Gomes
 */
public class Utils {
    private static String CURRENT_USER = "";
            
    public void refreshView(JList jList1, JPanel mainPanel){
        SQLiteJDBC sqlite = new SQLiteJDBC();
        List<Model> list = new ArrayList<>(sqlite.getItems2());

        DefaultListModel<Model> listModel = new DefaultListModel();
        for(Model obj : list){
            listModel.addElement(obj);
        }
        jList1.setModel(listModel);
    }
    
    public void saveParamChanges(String language, String country){
        try{
            // Load current parameters
            Properties props = loadProps();
            // Set desired parameters
            props.setProperty("Country", country);
            props.setProperty("Language", language);
            
            // Save properties to file
            File f = new File("passman.properties");
            OutputStream out = new FileOutputStream(f);
            props.store(out,"Language properties");
        } catch (Exception e){
            ErrorDialog errDlg = new ErrorDialog(new JFrame(), e.getClass().getName(), e.getMessage());
            System.exit(0);
        }
    }
    
    public static ArrayList<String> loadParams(){
        Properties props = new Properties();
        InputStream is = null;

        // First try loading from the current directory
        try {
            File f = new File("passman.properties");
            is = new FileInputStream( f );
        }
        catch ( Exception e ) {
            is = null;
            ErrorDialog errDlg = new ErrorDialog(new JFrame(), e.getClass().getName(), e.getMessage());
            System.exit(0);
        }

        try {
            // Try loading properties from the file (if found)
            props.load( is );
        }
        catch ( Exception e ){
            ErrorDialog errDlg = new ErrorDialog(new JFrame(), e.getClass().getName(), e.getMessage());
            System.exit(0);
        }
        
        ArrayList<String> list = new ArrayList<>();
        list.add(props.getProperty("Country","UK"));
        list.add(props.getProperty("Language","en"));
        
        return list;

    }
    
    public static Properties loadProps(){
        Properties props = new Properties();
        InputStream is = null;

        // First try loading from the current directory
        try {
            File f = new File("passman.properties");
            is = new FileInputStream( f );
        }
        catch ( Exception e ) {
            is = null;
            ErrorDialog errDlg = new ErrorDialog(new JFrame(), e.getClass().getName(), e.getMessage());
            System.exit(0);
        }

        try {
            // Try loading properties from the file (if found)
            props.load( is );
        }
        catch ( Exception e ) {
            ErrorDialog errDlg = new ErrorDialog(new JFrame(), e.getClass().getName(), e.getMessage());
            System.exit(0);
        }
        
        //return props.getProperty("Language","en");
        return props;
    }
    
    public static String getTitleFromProps(){
        Properties props = loadProps();
        
        String title1 = props.getProperty("Application.title");
        String title2 = props.getProperty("Application.version");
        String title3 = props.getProperty("Application.buildnumber");
        
        String finalTitle = title1.replace("${Application.version}", title2.replace("${Application.buildnumber}", title3));
        
        return finalTitle;
    }
    
    public static void verifyDB(){
        File f = new File("./passman.s3db");
        if(!f.exists()){
            SQLiteJDBC sqlite = new SQLiteJDBC();
            sqlite.createConnection();
        }
    }
    
    public static void addToDBFromUI(String label, String username, String plainTextPassword, String comment){
        // get utf-8 bytes from string
        byte[] passUTF8 = plainTextPassword.getBytes(StandardCharsets.UTF_8);
        
        // gets the current user
        SQLiteJDBC sqlite = new SQLiteJDBC();
        User currentUser = sqlite.getUser(CURRENT_USER);
        if(currentUser == null){
            System.out.println("Coudn't get " + CURRENT_USER + " user from DB.");
        }
        
        // use user password and salt to encrypt password bytes
        CryptModel cpMdl = Crypt.encrypt(currentUser.getSecurePassword(), currentUser.getSaltArray(), passUTF8);
        if(cpMdl.encryptedPassword == null){
            System.out.println("Something went wrong while encrypting.");
        }
        
        // save data to DB
        Model newModel = new Model(label, username, cpMdl.encryptedPassword, cpMdl.salt, comment);
        //sqlite.addItem(newModel);
        sqlite.addItem2(newModel);
    }
    
    public static String getFromDBToUI(byte[] encryptedMessage, byte[] salt){
        // decrypts password using current user values
        SQLiteJDBC sqlite = new SQLiteJDBC();
        User currentUser = sqlite.getUser(CURRENT_USER);
        if(currentUser == null){
            System.out.println("Coudn't get "+ CURRENT_USER +" user from DB.");
        }

        byte[] decryptedPass = Crypt.decrypt(currentUser.getSecurePassword(), 
                salt, encryptedMessage);

        // convert decrypted byte array to String using UTF-8 encoding
        String plainTextPassword = new String(decryptedPass,StandardCharsets.UTF_8);
        System.out.println(plainTextPassword);
        
        return(plainTextPassword);
    }
    
    public static void goToScreen(JPanel mainPanel, String location){
        if(location.equals("CREATEUSER") || location.equals("LOGIN") || location.equals("LANGUAGE")){
            CardLayout card = (CardLayout)mainPanel.getLayout();
            card.show(mainPanel, location);
        } else{
            if(CURRENT_USER != ""){
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
}
