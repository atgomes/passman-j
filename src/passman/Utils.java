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
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import passman.db.SQLiteJDBC;
import passman.model.Model;

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
}
