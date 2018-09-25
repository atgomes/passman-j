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
public class User {
    //private static int privateID;
    private String username;
    private byte[] securePassword;
    private byte[] saltArray;
    
    public User(String uName, byte[] sPass, byte[] salt){
        //privateID = pID;
        username = uName;
        securePassword = sPass;
        saltArray = salt;
    }

    /**
     * @return the privateID
     */
    //public int getPrivateID() {
    //    return privateID;
    //}

    /**
     * @param privateID the privateID to set
     */
    //public void setPrivateID(int privateID) {
    //   this.privateID = privateID;
    //}

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
     * @return the securePassword
     */
    public byte[] getSecurePassword() {
        return securePassword;
    }

    /**
     * @param securePassword the securePassword to set
     */
    public void setSecurePassword(byte[] securePassword) {
        this.securePassword = securePassword;
    }

    /**
     * @return the saltArray
     */
    public byte[] getSaltArray() {
        return saltArray;
    }

    /**
     * @param saltArray the saltArray to set
     */
    public void setSaltArray(byte[] saltArray) {
        this.saltArray = saltArray;
    }
}
