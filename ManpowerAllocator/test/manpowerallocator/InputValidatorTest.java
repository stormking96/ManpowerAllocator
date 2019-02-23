/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package manpowerallocator;

import java.util.ArrayList;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author storm
 */
public class InputValidatorTest {
    
    public InputValidatorTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of validateName method, of class InputValidator.
     */
    @Test
    public void testValidateName() 
    {
        System.out.println("validateName");
        ArrayList names = new ArrayList();
        names.add("Matt");
        names.add("matt");
        names.add(" matt");
        names.add("matt ");
        names.add("1matt");
        names.add("Matt2");
        names.add("*Matt");
        names.add("Ma8tt");
        String name = (String)names.get(7);
        boolean expResult = false;
        //boolean result = InputValidator.validateName(name);
        //assertEquals(expResult, result);
    }

    /**
     * Test of validateDate method, of class InputValidator.
     */
    @Test
    public void testValidateDate() {
        System.out.println("validateDate");
        ArrayList dates = new ArrayList();
        //should pass
        dates.add("05/28/1996");
        dates.add("01/01/2001");
        dates.add("01/01/2000");
        dates.add("12/31/1999");
        
        //should fail
        dates.add("05/05/05");
        dates.add("2018/05/05");
        dates.add("18/05/1996");
        dates.add("00/01/2001");
        dates.add("01/00/1990");
        dates.add(" 05/04/1998");
        dates.add("05/04/1998 ");
        dates.add("99/99/1999");
        
        String date = (String)dates.get(3);
        boolean expResult = true;
        //boolean result = InputValidator.validateDate(date);
        //assertEquals(expResult, result);
       
    }

    /**
     * Test of validateEmployeeID method, of class InputValidator.
     */
    @Test
    public void testValidateEmployeeID() {
        System.out.println("validateEmployeeID");
        String id = "123";
        boolean expResult = false;
        //boolean result = InputValidator.validateEmployeeID(id);
        //assertEquals(expResult, result);
    }
    
}
