/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package passman.db;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Random;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.ShortBufferException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import passman.model.CryptModel;


/**
 *
 * @author P057736
 */
public class Crypt {
    
    private static final SecureRandom RANDOM = new SecureRandom();
      
    private static byte[] generateSalt(int length){
        //Random random = new SecureRandom();
        byte[] salt = new byte[length];
        RANDOM.nextBytes(salt);
        
        return(Base64.getEncoder().encode(salt));
    }
    
    public static byte[] generateIV(int length){
        byte[] iv = new byte[length];
        RANDOM.nextBytes(iv);
        
        return(iv);
    }
    
    public static ArrayList<byte[]> getSecurePassword(String password){
        ArrayList<byte[]> list = new ArrayList<>();
        String generatedPassword = null;
        try{
            byte[] salt = generateSalt(16);
            byte[] passBytes = Base64.getDecoder().decode(password);

            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(salt);
            byte[] bytes = md.digest(passBytes);
            //generatedPassword = bytes;
            
            list.add(bytes);
            list.add(salt);
        }catch(NoSuchAlgorithmException e){
            
        }
        
        return list;
        
    }
    
    public static boolean verifyPasswordValidity(String password, byte[] salt, byte[] securePassword){
        String generatedPassword = null;
        try{
            //byte[] byteSalt = Base64.getDecoder().decode(salt);
            byte[] bytePass = Base64.getDecoder().decode(password);
            byte[] byteSalt = salt;
            //byte[] bytePass = password;

            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(byteSalt);
            byte[] bytes = md.digest(bytePass);
            generatedPassword = Base64.getEncoder().encodeToString(bytes);
        } catch(NoSuchAlgorithmException e){
            
        }
        return(generatedPassword.equals(Base64.getEncoder().encodeToString(securePassword)));
    }
    
    public static CryptModel encrypt(byte[] keyBytes, byte[] ivBytes, byte[] input){
        byte[] encrypted = null;
        int enc_len = -1;
        byte[] encoded = null;
        try{
            SecretKey key = KeyGenerator.getInstance("AES").generateKey();
            encoded = key.getEncoded();
            //SecretKeySpec key = new SecretKeySpec(keyBytes, "AES");
            IvParameterSpec ivSpec = new IvParameterSpec(ivBytes);
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");

            cipher.init(Cipher.ENCRYPT_MODE, key);
            /*encrypted= new byte[cipher.getOutputSize(input.length)];
            enc_len = cipher.update(input, 0, input.length, encrypted, 0);
            enc_len += cipher.doFinal(encrypted, enc_len);*/
            //encryptedPassword = Base64.encode(encrypted);
            encrypted = cipher.doFinal(input);
            
        } catch(NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e){
            System.out.println(e.getClass().getName()+e.getMessage());
        }
        
        return new CryptModel(encoded, ivBytes, encrypted, enc_len);
    }
    
    public static String decrypt(byte[] keyBytes, byte[] ivBytes, byte[] encrypted, int enc_len){
        String decryptedPassword = null;
        try{
            SecretKey key = new SecretKeySpec(keyBytes, "AES");
            //SecretKeySpec key = new SecretKeySpec(keyBytes, "AES");
            //IvParameterSpec ivSpec = new IvParameterSpec(ivBytes);
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");

            cipher.init(Cipher.DECRYPT_MODE, key);
            //cipher.doFinal()
            /*byte[] decrypted = new byte[cipher.getOutputSize(enc_len)];
            int dec_len = cipher.update(encrypted, 0, enc_len, decrypted, 0);
            dec_len += cipher.doFinal(decrypted, dec_len);
            decryptedPassword = Base64.getEncoder().encodeToString(decrypted);*/
            encrypted = cipher.doFinal(encrypted);
            
        }catch(NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e){
            
        }
        
        return Base64.getEncoder().encodeToString(encrypted);
    }
}
