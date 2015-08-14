/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package passman.db;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.util.logging.Level;
import java.util.logging.Logger;
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
        
        return(salt);
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
            byte[] passBytes = password.getBytes(StandardCharsets.UTF_8); // Uses UTF-8 encoding to convert password to byte[]

            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(salt);
            byte[] bytes = md.digest(passBytes);
            
            list.add(bytes);
            list.add(salt);
        }catch(NoSuchAlgorithmException e){
            // Log exception
            Logger.getLogger("").log(Level.SEVERE, "Application stopped due to exception: {0}",e.getClass().getName());
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
     * @return the secure password if original is correct. Null otherwise.
     */
    public static byte[] verifyPasswordValidity(String password, byte[] salt, byte[] securePassword){
        String generatedPassword = null;
        byte[] bytes = null;
        try{
            // Uses UTF-8 encoding to convert String password to byte[]
            byte[] bytePass = password.getBytes(StandardCharsets.UTF_8);
            byte[] byteSalt = salt;

            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(byteSalt);
            bytes = md.digest(bytePass);
            // Uses UTF-8 encoding to convert byte[] to String password
            //generatedPassword = new String(bytes,StandardCharsets.UTF_8);
        } catch(NoSuchAlgorithmException e){
            // Log exception
            Logger.getLogger("").log(Level.SEVERE, "Application stopped due to exception: {0}",e.getClass().getName());
            ErrorDialog errDlg = new ErrorDialog(new JFrame(), e.getClass().getName(), e.getMessage());
            System.exit(0);
        }
        if(Arrays.equals(bytes, securePassword)){
            return bytes;
        }
        else{
            return null;
        }
    }
    
    /**
     * Encrypts byte array using AES encryption.
     * @param input byte array to encrypt
     * @return Crypt model object that contains the fields encoded and encrypted
     */
    public static CryptModel encrypt(byte[] input){
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
    
    public static CryptModel encrypt(byte[] password, byte[] salt, byte[] input){
        byte[] encrypted = null;
        salt = generateSalt(16);
        byte[] key = new byte[salt.length+password.length];
        try{
            for (int i = 0; i < key.length; ++i){
                key[i] = i < salt.length ? salt[i] : password[i - salt.length];
            }
            MessageDigest sha = MessageDigest.getInstance("SHA-1");
            key = sha.digest(key);
            key = Arrays.copyOf(key, 16);
            
            SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
            
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
            
            encrypted = cipher.doFinal(input);
            // Log action
            Logger.getLogger("").log(Level.INFO, "{0} bytes encryption successful.",encrypted.length);
        }catch (NoSuchAlgorithmException | BadPaddingException | InvalidKeyException | NoSuchPaddingException | IllegalBlockSizeException ex) {
            // Log exception
            Logger.getLogger("").log(Level.SEVERE, "Application stopped due to exception: {0}",ex.getClass().getName());
            ErrorDialog errDlg = new ErrorDialog(new JFrame(), ex.getClass().getName(), ex.getMessage());
            System.exit(0);
        }
        
        return new CryptModel(salt, encrypted);
    }
        
    /**
     * Decrypts byte array using an encoded key and AES encryption.
     * @param keyBytes encoded key created during encryption
     * @param encrypted encrypted byte array to decrypt
     * @return decrypted byte array as String
     */
    public static byte[] decrypt(byte[] keyBytes, byte[] encrypted){
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
        
        //return Base64.getEncoder().encodeToString(decrypted);
        return decrypted;
    }
    
    public static byte[] decrypt(byte[] password, byte[] salt, byte[] encrypted){
        byte[] decrypted = null;
        byte[] key = new byte[salt.length+password.length];
        try{
            for (int i = 0; i < key.length; ++i){
                key[i] = i < salt.length ? salt[i] : password[i - salt.length];
            }
            MessageDigest sha = MessageDigest.getInstance("SHA-1");
            key = sha.digest(key);
            key = Arrays.copyOf(key, 16);
            SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
            
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
            decrypted = cipher.doFinal(encrypted);
            
            // Log action
            Logger.getLogger("").log(Level.INFO, "{0} bytes decryption successful.",encrypted.length);
            
        } catch (NoSuchAlgorithmException | IllegalBlockSizeException | InvalidKeyException | NoSuchPaddingException | BadPaddingException ex) {
            // Log action
            Logger.getLogger("").log(Level.SEVERE, "Application stopped due to exception: {0}",ex.getClass().getName());
            ErrorDialog errDlg = new ErrorDialog(new JFrame(), ex.getClass().getName(), ex.getMessage());
            System.exit(0);
        }
        
        return decrypted;
    }
}
