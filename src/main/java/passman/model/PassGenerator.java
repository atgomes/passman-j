/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package passman.model;

import java.util.Random;

/**
 *
 * @author P057736
 */
public class PassGenerator {
    
    /**
     * Overloaded version with no parameters
     * @return 
     */
    public static String generate(){
        // generates a random sized password
        return(generate(-1,true,true,true,true));
    }
    
    /**
     * Overloaded version with only one parameter (password length)
     * @param passwordLength
     * @return 
     */
    public static String generate(int passwordLength){
        return(generate(passwordLength,true,true,true,true));
    }
    
    /**
     * Overloaded version with only password length and safe symbols parameters
     * @param passwordLength
     * @param safeSymbols
     * @return 
     */
    public static String generate(int passwordLength, boolean safeSymbols){
        return(generate(passwordLength,true,safeSymbols,true,true));
    }
    
    /**
     * Generates random plain text password. This function can change the type 
     * of password generated according to the parameters received.
     * @param passwordLength determines the password length. If value provided is lower than 0 then length is random
     * @param needsSymbol determines if the password should have symbols
     * @param safeSymbols determines which symbols can be used. Safe symbols are limited to "-" and "_"
     * @param needsDigit determines if the password should have numbers
     * @param needsUpperCase determines if the password should have upper case letters
     * @return 
     */
    public static String generate(int passwordLength, boolean needsSymbol, boolean safeSymbols,
            boolean needsDigit, boolean needsUpperCase){
        
        // Creation of the random instance used to generate password
        Random randNumber = new Random();
        
        int passwordSize;
        if(passwordLength>0){
            passwordSize = passwordLength;
        }
        else {
            passwordSize = 9+randNumber.nextInt(15);
        }
        
        boolean hasDigit = !needsDigit;
        boolean hasUpperCase = !needsUpperCase;
        boolean hasLowCase = false;
        boolean hasSymbol = !needsSymbol;
        
        // Determine maximum quantity of symbols for a good reasonable password (20% of the pass size)
        //int maxSymbols = (int)Math.round(0.2*passwordSize);
        
        byte[] protoPass = new byte[passwordSize];
        
        int temp;
        
        for (int i = 0; i < protoPass.length; i++){
            
            // last four positions: needs to accomplish requisites if not done yet
            if(i>=protoPass.length-4){
                // checks validity for symbols
                if(!hasSymbol && needsSymbol){
                    protoPass[i] = 45;
                    hasSymbol = true;
                    continue;
                }
                // checks validity for digits
                if(!hasDigit && needsDigit){
                    protoPass[i] = (byte)(48+randNumber.nextInt(9));
                    hasDigit = true;
                    continue;
                }
                // checks validity for uppercase
                if(!hasUpperCase && needsUpperCase){
                    protoPass[i] = (byte)(65+randNumber.nextInt(25));
                    hasUpperCase = true;
                    continue;
                }
                // checks validity for lowcase
                if(!hasLowCase){
                    protoPass[i] = (byte)(65+randNumber.nextInt(25));
                    hasUpperCase = true;
                }
            }
            
            temp = 33+randNumber.nextInt(92);          
            
            if(temp == 44 || temp == 96){ // it's not an allowed symbol
                temp = 97+randNumber.nextInt(25);
            }
            
            // its a symbol
            if(temp<48 || (temp>57 && temp<65) || (temp>90 && temp<96) || temp>122){
                if(!needsSymbol){
                    temp = 97+randNumber.nextInt(25);
                    hasLowCase = true;
                }
                else{
                    if(safeSymbols){
                        if(randNumber.nextBoolean()){
                            temp = 95;
                        }
                        else{
                            temp = 45;
                        }
                    }
                    hasSymbol = true;
                }
            }
            
            //its a digit
            if(temp>47 && temp<58){
                if(!needsDigit){
                    temp = 97+randNumber.nextInt(25);
                    hasLowCase = true;
                }
                else{
                    hasDigit = true;
                }
            }
            
            // its a uppercase
            if(temp>64 && temp <91){
                if(!needsUpperCase){
                    temp = 97+randNumber.nextInt(25);
                    hasLowCase = true;
                }
                else{
                    hasUpperCase = true;
                }
            }
            
            // its lowcase
            if(temp>96 && temp<123){
                hasLowCase = true;
            }
            
            protoPass[i] = (byte)temp;
        }
        
        // constructs password as char array
        char[] semiPass = new char[passwordSize];
        for (int i = 0; i < protoPass.length; i++){ // converts byte[] to char[]
            semiPass[i] = (char)protoPass[i];
        }
        String finalPass = new String(semiPass); // converts char[] to String object
        return (finalPass);
    }
}
