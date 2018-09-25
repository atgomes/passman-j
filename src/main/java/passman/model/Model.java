/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package passman.model;

/**
 *
 * @author P057736
 */
public class Model {
    
    private String label;
    private String username;
    private byte[] password;
    private byte[] salt;
    private String comment;
    
    // Constructor
    public Model(String lbl, String usr, byte[] pass, byte[] slt, String comm){
        label = lbl;
        username = usr;
        password = pass;
        salt = slt;
        comment = comm;
    }

    /**
     * @return the label
     */
    public String getLabel() {
        return label;
    }

    /**
     * @param label the label to set
     */
    public void setLabel(String label) {
        this.label = label;
    }

    /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return the password
     */
    public byte[] getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(byte[] password) {
        this.password = password;
    }
    
    /**
     * @return salt the salt
     */
    public byte[] getSalt() {
        return salt;
    }
    
    /**
     * @param salt the salt to set
     */
    public void setSalt(byte[] salt) {
        this.salt = salt;
    }

    /**
     * @return the comment
     */
    public String getComment() {
        return comment;
    }

    /**
     * @param comment the comment to set
     */
    public void setComment(String comment) {
        this.comment = comment;
    }
    
    @Override
    public String toString(){
        return this.label;
    }
    
    public boolean equals(Model mdl){
        return(this.getLabel().equalsIgnoreCase(mdl.getLabel()));
    }
    
}
