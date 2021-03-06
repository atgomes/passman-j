/*
 * Copyright (C) 2016 andre
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package passman.db;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import org.hamcrest.CoreMatchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.Mockito;
import passman.model.CryptModel;

/**
 *
 * @author andre
 */
public class CryptTest {
    
    public CryptTest() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of getSecurePassword method, of class Crypt.
     */
    @Test
    public void testGetSecurePassword() {
        System.out.println("getSecurePassword");
        String password = "testpassword";
        ArrayList<byte[]> result = Crypt.getSecurePassword(password);
        assertTrue(result.size() == 2);
        byte[] pos1 = result.get(0);
        byte[] pos2 = result.get(1);
        assertTrue(pos1.length > 0);
        assertTrue(pos2.length > 0);
    }
    
    /**
     * Test of verifyPasswordValidity method, of class Crypt.
     */
    @Test
    public void testVerifyPasswordValidity() {
        System.out.println("verifyPasswordValidity");
        String password1 = "passwordtotest";
        String password2 = "differentpassword";
        ArrayList<byte[]> testList = Crypt.getSecurePassword(password1);
        byte[] result =  Crypt.verifyPasswordValidity(password1, testList.get(1), testList.get(0));        
        assertNotNull(result);
        byte[] result2 =  Crypt.verifyPasswordValidity(password2, testList.get(1), testList.get(0));
        assertNull(result2);
    }
    
    /**
     * Test of encrypt method, of class Crypt.
     */
    @Test
    public void testEncrypt() {
        System.out.println("encrypt");
        //CryptModel test = Mockito.mock(CryptModel.class);
        
        String password1 = "passwordtotest";
        byte[] passUTF8 = password1.getBytes(StandardCharsets.UTF_8);
        
        ArrayList<byte[]> list = Crypt.getSecurePassword(password1);
        
        CryptModel cpMdl = Crypt.encrypt(list.get(0), null, passUTF8);
        
        assertNotNull(cpMdl);
    }
    
        /**
     * Test of decrypt method, of class Crypt.
     */
    @Test
    public void testDecrypt() {
        System.out.println("decrypt");
        //CryptModel test = Mockito.mock(CryptModel.class);
        
        String password1 = "passwordtotest";
        String dataToEncrypt = "datatoencrypt";
        
        byte[] dataUTF8 = dataToEncrypt.getBytes(StandardCharsets.UTF_8);
        
        ArrayList<byte[]> list = Crypt.getSecurePassword(password1);
        
        CryptModel cpMdl = Crypt.encrypt(list.get(0), null, dataUTF8);
        
        byte[] result = Crypt.decrypt(list.get(0), cpMdl.salt, cpMdl.encryptedPassword);
        
        assertNotNull(result);
        
        assertEquals(dataToEncrypt, new String(result,StandardCharsets.UTF_8));
    }  
}
