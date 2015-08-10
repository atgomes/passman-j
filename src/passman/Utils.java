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
import javax.swing.JList;
import javax.swing.JPanel;
import passman.db.Crypt;
import passman.db.SQLiteJDBC;
import passman.model.CryptModel;
import passman.model.Model;
import passman.model.User;

/**
 *
 * @author Andre Gomes
 */
public class Utils {
    public void refreshView(JList jList1, JPanel mainPanel){
        SQLiteJDBC sqlite = new SQLiteJDBC();
        List<Model> list = new ArrayList<>(sqlite.getItems());

        DefaultListModel<Model> listModel = new DefaultListModel();
        for(Model obj : list){
            listModel.addElement(obj);
        }
        jList1.setModel(listModel);
        
        CardLayout card = (CardLayout)mainPanel.getLayout();
        card.show(mainPanel, "card4");
    }
    
    public void saveParamChanges(String language, String country){
        try{
            Properties props = new Properties();
            props.setProperty("Country", country);
            props.setProperty("Language", language);
            File f = new File("passman.properties");
            OutputStream out = new FileOutputStream(f);
            props.store(out,"Language properties");
        } catch (Exception e){
            e.printStackTrace();
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
        catch ( Exception e ) { is = null; }

        try {
            // Try loading properties from the file (if found)
            props.load( is );
        }
        catch ( Exception e ) { }
        
        ArrayList<String> list = new ArrayList<>();
        list.add(props.getProperty("Country","UK"));
        list.add(props.getProperty("Language","en"));
        
        //return props.getProperty("Language","en");
        return list;

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
        // TODO: change from fixed user to real current user
        SQLiteJDBC sqlite = new SQLiteJDBC();
        User currentUser;
        currentUser = sqlite.getUser("admin");
        if(currentUser == null){
            System.out.println("Coudn't get admin user from DB.");
        }
        
        // use user password and salt to encrypt password bytes
        CryptModel cpMdl = Crypt.encrypt(currentUser.getSecurePassword(), currentUser.getSaltArray(), passUTF8);
        if(cpMdl.encryptedPassword == null){
            System.out.println("Something went wrong while encrypting.");
        }
        
        // save data to DB
        Model newModel = new Model(label, username, cpMdl.encryptedPassword, cpMdl.salt, comment);
        sqlite.addItem(newModel);
        
        System.out.println("[ENTRY]Encrypted password size: "+newModel.getPassword().length + " bytes");
    }
    
    public static String getFromDBToUI(byte[] encryptedMessage){
        // decrypts password using current user values
        SQLiteJDBC sqlite = new SQLiteJDBC();
        User currentUser = sqlite.getUser("admin");
        if(currentUser == null){
            System.out.println("Coudn't get admin user from DB.");
        }

        byte[] decryptedPass = Crypt.decrypt(currentUser.getSecurePassword(), 
                currentUser.getSaltArray(), encryptedMessage);

        // convert decrypted byte array to String using UTF-8 encoding
        String plainTextPassword = new String(decryptedPass,StandardCharsets.UTF_8);
        System.out.println(plainTextPassword);
        
        return(plainTextPassword);
    }
}
