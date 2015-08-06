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
public class CryptModel {
    public final byte[] encodedKey;
    public final byte[] encryptedPassword;
    
    
    public CryptModel(byte[] encKey, byte[] encBs){
        encodedKey = encKey;
        encryptedPassword = encBs;
    }
}
