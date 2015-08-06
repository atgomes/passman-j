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
    public final byte[] keyBytes;
    public final byte[] ivBytes;
    public final byte[] encrypted;
    public final int enc_len;
    
    
    public CryptModel(byte[] kBs, byte[] ivBs, byte[] encBs, int eLen){
        keyBytes = kBs;
        ivBytes = ivBs;
        encrypted = encBs;
        enc_len = eLen;
    }
}
