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
    //private int pLenght = 16;
    
    public static String generate(){
        boolean hasDigit = true;
        boolean hasUpperCase = true;
        boolean hasLowCase = false;
        boolean hasSymbol = true;

        Random randNumber = new Random();
        int sizePass = 9 + randNumber.nextInt(16); // The password size will be between 9 and 24 chars
        
        // Determine maximum quantity of symbols for a good reasonable password (20% of the pass size)
        int maxSymbols = (int)Math.round(0.2*sizePass);
        
        byte[] protoPass = new byte[sizePass];
        randNumber.nextBytes(protoPass);
        for (int i = 0; i < protoPass.length; i++){
            // keeps trying till a good value is found
            while (protoPass[i] < 33 || protoPass[i] > 125 || protoPass[i] == 34 ||
                    protoPass[i] == 39 || protoPass[i] == 44 || 
                    protoPass[i] == 94 || protoPass[i] == 96){ // out of limits
            
                protoPass[i] = (byte)(33+randNumber.nextInt(92)); //generates values between 33 and 125 (see ascii table)
            }
            // its a symbol
            if ((protoPass[i] > 32 && protoPass[i] < 48) || (protoPass[i] > 57 && protoPass[i] < 65)
    || (protoPass[i] > 90 && protoPass[i] < 97))
            {
                maxSymbols -= 1;
                // no more symbols available, replaces with a lower case char
                if (maxSymbols < 0)
                {
                    protoPass[i] = (byte)(97+randNumber.nextInt(25)); // generates value between 97 and 122
                }
                else
                {
                    hasSymbol = true; // checks that array has symbol
                }
            }
            // its lowcase
            if ((protoPass[i] > 96 && protoPass[i] < 123) && !hasLowCase)
            {
                hasLowCase = true; // checks that array has lowcase
            }
            // its uppercase
            if ((protoPass[i] > 64 && protoPass[i] < 91) && !hasUpperCase)
            {
                hasUpperCase = true; // checks that array has uppercase
            }
            // its a digit
            if ((protoPass[i] > 47 && protoPass[i] < 58) && !hasDigit)
            {
                hasDigit = true; // checks that array has digit
            }
            // In the last 4 positions it checks if all security need were met
            if (i >= (protoPass.length - 4))
            {
                if (!hasLowCase) { // if it still hasnt low case it inserts one
                    protoPass[i] = (byte)(97+randNumber.nextInt(25)); // generates value between 97 and 122
                    continue; // skips to next iteration
                }
                if (!hasUpperCase){ // if it still hasnt uppercase it inserts one
                    protoPass[i] = (byte)(65+randNumber.nextInt(25));
                    continue; // skips to next iteration
                }
                if (!hasDigit){ // if it still hasnt digit it inserts one
                    protoPass[i] = (byte)(48+randNumber.nextInt(9));
                    continue; // skips to next iteration
                }
                if (!hasSymbol) { // if it still hasnt symbol it inserts one
                    switch (randNumber.nextInt(2)){ // selects randomly between three symbosl (see ascii table)
                        case 0:
                            protoPass[i] = 64; // @ sign
                            break;
                        case 1:
                            protoPass[i] = 35; // # sign
                            break;
                        default:
                            protoPass[i] = 33; // ! point
                            break;
                    }
                }
            }
        }
        
        char[] semiPass = new char[sizePass];
        for (int i = 0; i < protoPass.length; i++){ // converts byte[] to char[]
            semiPass[i] = (char)protoPass[i];
        }
        String finalPass = new String(semiPass); // converts char[] to String object
        return (finalPass);
    }
}
