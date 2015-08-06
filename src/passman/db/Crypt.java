/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package passman.db;

import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.swing.JFrame;
import passman.model.CryptModel;
import passman.model.ErrorDialog;


/**
 *
 * @author Andre Gomes
 */
public class Crypt {
    
    private static final SecureRandom RANDOM = new SecureRandom();
    
    /**
     * Salt generator that uses a SecureRandom object.
     * @param length salt length in bytes
     * @return Base64 encoded byte array with salt
     */      
    private static byte[] generateSalt(int length){
        byte[] salt = new byte[length];
        RANDOM.nextBytes(salt);
        
        return(Base64.getEncoder().encode(salt));
    }
    
    /**
     * Generates a secure hashed and salted password (SHA-256).
     * @param password
     * @return byte[] list with hashed password and the salt used
     */
    public static ArrayList<byte[]> getSecurePassword(String password){
        ArrayList<byte[]> list = new ArrayList<>();
        try{
            byte[] salt = generateSalt(16);
            byte[] passBytes = Base64.getDecoder().decode(password); // Uses Base64 encoding to convert password to byte[]

            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(salt);
            byte[] bytes = md.digest(passBytes);
            
            list.add(bytes);
            list.add(salt);
        }catch(NoSuchAlgorithmException e){
            ErrorDialog errDlg = new ErrorDialog(new JFrame(), e.getClass().getName(), e.getMessage());
            System.exit(0);
        }
        
        return list;
        
    }
    
    /**
     * Verifies password validity by hashing and salting the user submitted 
     * password and comparing it with the stored secure password.
     * @param password user submitted string password
     * @param salt salt used in the stored secure password
     * @param securePassword stored secure password
     * @return true if the password is correct, false otherwise
     */
    public static boolean verifyPasswordValidity(String password, byte[] salt, byte[] securePassword){
        String generatedPassword = null;
        try{
            // Uses Base64 encoding to convert String password to byte[]
            byte[] bytePass = Base64.getDecoder().decode(password);
            byte[] byteSalt = salt;

            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(byteSalt);
            byte[] bytes = md.digest(bytePass);
            // Uses Base64 encoding to convert byte[] to String password
            generatedPassword = Base64.getEncoder().encodeToString(bytes);
        } catch(NoSuchAlgorithmException e){
            
        }
        return(generatedPassword.equals(Base64.getEncoder().encodeToString(securePassword)));
    }
    
    /**
     * Encrypts byte array using an user submitted password hashed and salted 
     * and AES encryption.
     * @param keyBytes secure password used to encrypt input
     * @param input byte array to encrypt
     * @return Crypt model object that contains the fields encoded and encrypted
     */
    public static CryptModel encrypt(byte[] keyBytes, byte[] input){
        byte[] encrypted = null;
        byte[] encoded = null;
        try{
            SecretKey key = KeyGenerator.getInstance("AES").generateKey();
            encoded = key.getEncoded();
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");

            cipher.init(Cipher.ENCRYPT_MODE, key);
            encrypted = cipher.doFinal(input);
            
        } catch(NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e){
            System.out.println(e.getClass().getName()+e.getMessage());
        }
        
        return new CryptModel(encoded, encrypted);
    }
    
    /**
     * Decrypts byte array using an encoded key and AES encryption.
     * @param keyBytes encoded key created during encryption based on an user submitted password
     * @param encrypted encrypted byte array to decrypt
     * @return decrypted byte array as String
     */
    public static String decrypt(byte[] keyBytes, byte[] encrypted){
        byte[] decrypted = null;
        try{
            SecretKey key = new SecretKeySpec(keyBytes, "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");

            cipher.init(Cipher.DECRYPT_MODE, key);
            decrypted = new byte[cipher.getOutputSize(encrypted.length)];
            decrypted = cipher.doFinal(encrypted);
            
        }catch(NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e){
            ErrorDialog errDlg = new ErrorDialog(new JFrame(), e.getClass().getName(), e.getMessage());
            System.exit(0);
        }
        
        return Base64.getEncoder().encodeToString(decrypted);
    }
}
