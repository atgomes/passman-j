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
public class PasswordOptions {
    private int pLength;
    private boolean symbols;
    private boolean symbolsSafe;
    private boolean digits;
    private boolean upperCase;
    
    public PasswordOptions(){
        pLength = 12;
        symbols = true;
        symbolsSafe = true;
        digits = true;
        upperCase = true;
    }
    
    public PasswordOptions(int p_length, boolean symb, boolean s_safe, boolean digt, boolean up_case){
        pLength = p_length;
        symbols = symb;
        symbolsSafe = s_safe;
        digits = digt;
        upperCase = up_case;
    }    

    /**
     * @return the pLength
     */
    public int getpLength() {
        return pLength;
    }

    /**
     * @param pLength the pLength to set
     */
    public void setpLength(int pLength) {
        this.pLength = pLength;
    }

    /**
     * @return the symbols
     */
    public boolean isSymbols() {
        return symbols;
    }

    /**
     * @param symbols the symbols to set
     */
    public void setSymbols(boolean symbols) {
        this.symbols = symbols;
    }

    /**
     * @return the symbolsSafe
     */
    public boolean isSymbolsSafe() {
        return symbolsSafe;
    }

    /**
     * @param symbolsSafe the symbolsSafe to set
     */
    public void setSymbolsSafe(boolean symbolsSafe) {
        this.symbolsSafe = symbolsSafe;
    }

    /**
     * @return the digits
     */
    public boolean isDigits() {
        return digits;
    }

    /**
     * @param digits the digits to set
     */
    public void setDigits(boolean digits) {
        this.digits = digits;
    }

    /**
     * @return the upperCase
     */
    public boolean isUpperCase() {
        return upperCase;
    }

    /**
     * @param upperCase the upperCase to set
     */
    public void setUpperCase(boolean upperCase) {
        this.upperCase = upperCase;
    }
    
}
