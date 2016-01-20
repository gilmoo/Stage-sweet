/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bank.bankieren;

import fontys.util.NumberDoesntExistException;
import junit.framework.Assert;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author lino_
 */
public class BankTest {
    
    public BankTest() {
        /**
         * @param name van de bank
         * 
         * De map worden van accounts worden geinitiliseerd
         * List van klanten worden geinitiliseerd
         * Rekening heeft nu een waarde van 100000000
         * fields naam heeft de waarde van parameter naam
         * 
         */
        Bank bank = new Bank("Rabobank");
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
     * Test of openRekening method, of class Bank.
     */
    @Test
    public void testOpenRekening() {
        /**
         * @param name van de eigenaar
         * @param city van de eigenaar
         * 
         * @return rekeningnummer wanneer de naam en plaats niet leeg zijn. waneer naam of plaatsnaam leeg zijn
         * dan wordt er -1 gereturneerd
         * 
         */
        
        //Maak nieuwe bank aan
        Bank bank = new Bank("RaboBank");
        //Rekening aan maken met een lege naam
        System.out.println("openRekening -- Lege naam");
        Assert.assertEquals(-1, bank.openRekening("", "Eindhoven"));
        //Rekening aan maken met een legen plaatsnaam
        System.out.println("openRekening -- Lege plaatsnaam");
        Assert.assertEquals(-1, bank.openRekening("Lino", ""));
        //Rekening aan maken met de juiste gegevens
        System.out.println("openRekening -- corect");
        Assert.assertEquals(100000000, bank.openRekening("Lino", "Eindhoven"));
        
        
    }

    /**
     * Test of getRekening method, of class Bank.
     */
    @Test
    public void testGetRekening() {
        /**
        * @param nr rekeningsnummer van eigenaar
        * @return IRekening wordt gereturn wanneer de parameter nr overeenkomt
        */
        //Maak nieuwe bank aan
        Bank bank = new Bank("Rabobank");
        //Maak een nieuwe rekening
        bank.openRekening("Lino", "Eindhoven");
        
        Assert.assertTrue(bank.getRekening(100000000) != null);
        
        Assert.assertNull(bank.getRekening(0));
    }

    /**
     * Test of maakOver method, of class Bank.
     */
    @Test
    public void testMaakOver() throws Exception,NumberDoesntExistException,RuntimeException  {
        
        /**
        * @param source rekeningnummer van de eigenaar
        * @param destination rekeningnummer van de ontvanger
        * @param money het bedrag die overgemaakt moet worden in centen
        * 
        * @return true wanneer de rekeningnummer van de eigenaar klopt, wanneer de rekeningnummer van de ontvanger bestaat
        * en wanneer het bedrag niet negatief zijn
        * exception wordt getrigerd wanneer de rekeningnummer van de eigenaar en de ontvanger hetzlefde zijn.
        * ook wordt een exception getrigerd wanneer het over te maken bedrag negative is
        */
        Bank bank = new Bank("Rabobank");
        bank.openRekening("Lino", "Eindhoven");
        bank.openRekening("Ruthger", "Eindhoven");
        try{
            //Overmaken naar eigen rekening
            System.out.println("maakOver--naar eigen rekening");
            assertEquals(true, bank.maakOver(100000000, 100000000, new Money(300, Money.EURO)));
            fail("Overmaken naar eigen rekening is niet mogelijk"); 
        }catch (Exception e){}
        
        try{
            //Overmaken met een negative bedrag
            System.out.println("maakOver--Negative bedrag");
            assertEquals(true, bank.maakOver(100000000, 100000001, new Money(-300, Money.EURO)));
            fail("Negative bedrag mag niet overgemaakt");
        }catch(Exception e){}
        
        try{
            //Overmaken met positive bedrag
            System.out.println("maakOver--Positief bedrag over");
            assertEquals(true, bank.maakOver(100000000, 100000001, new Money(300, Money.EURO)));
        }catch(Exception e){}
        
        try{
            //Overmaken met niet bestaande source rekening
            System.out.println("maakOver--niet bestaan source rekening");
            assertEquals(false, bank.maakOver(1, 100000001, new Money(300, Money.EURO)));
            fail("Negative bedrag mag niet overgemaakt");
        }catch(Exception e){}
        
        try{
            //Overmaken met niet bestaande destination rekening
            System.out.println("maakOver--niet bestaan dest rekening");
            assertEquals(false, bank.maakOver(100000000, 1, new Money(300, Money.EURO)));
            fail("Negative bedrag mag niet overgemaakt");
        }catch(Exception e){}
        
    }

    /**
     * Test of getName method, of class Bank.
     */
    @Test
    public void testGetName() {
        /**
        * return de naam van de eigenaar
        */
        Bank bank = new Bank("Rabobank");
        bank.openRekening("Lino", "Eindhoven");
        //Bestaande naam ophalen
        Assert.assertEquals("Rabobank", bank.getName());
    }
    
}
