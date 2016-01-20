/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bank.internettoegang;

import bank.bankieren.Bank;
import bank.bankieren.IRekening;
import bank.bankieren.Money;
import bank.bankieren.Rekening;
import fontys.util.InvalidSessionException;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;
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
public class BankiersessieTest {
    
    Bankiersessie bankiersessie = null;
    Bank bank;
    
    public BankiersessieTest() {
        try {
            bankiersessie = new Bankiersessie(100000000, bank);
        } catch (RemoteException ex) {
            Logger.getLogger(BankiersessieTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        bank = new Bank("Rabobank");
        bank.openRekening("Lino", "Eindhoven");
        bank.openRekening("Ruthger", "Eindhoven");
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of isGeldig method, of class Bankiersessie.
     */
    @Test
    public void testIsGeldig() {
        /**
         * @returns true als de laatste aanroep van getRekening of maakOver voor
         * deze sessie minder dan GELDIGHEIDSDUUR geleden is en er geen
         * communicatiestoornis in de tussentijd is opgetreden, anders false
         */
        try {
            bankiersessie = new Bankiersessie(100000000, bank);
        } catch (RemoteException ex) {
            Logger.getLogger(BankiersessieTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        //Actie is gedaan binnen de geldigheidsduur
        assertTrue(bankiersessie.isGeldig());
        
        try {
            //Actie is niet binnen de geldigheidsduur
            Thread.sleep(601);
        } catch (InterruptedException ex) {
            Logger.getLogger(BankiersessieTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        assertFalse(bankiersessie.isGeldig());
    }

    /**
     * Test of maakOver method, of class Bankiersessie.
     */
    @Test
    public void testMaakOver() throws Exception,RuntimeException,InvalidSessionException {
        
        /**
         * @param bestemming rekeningsnummer van de ontvanger
         * @param bedrag over te maken bedrag
         * @return true als de overmaking is gelukt, anders false
         * exception treedt op wanneer NumberDoesntExistException als bestemming onbekend is
         * exception treedt op wanneer InvalidSessionException als sessie niet meer geldig is
         */
        
        try {
            bankiersessie = new Bankiersessie(100000000, bank);
        } catch (RemoteException ex) {
            Logger.getLogger(BankiersessieTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        try{
            //Overmaken met de juiste gegevens
            assertTrue(bankiersessie.maakOver(100000001, new Money(500, Money.EURO)));
        }catch(Exception e){}
        
        try{
            //Overmaken naar eigen rekenning
            assertEquals(false,bankiersessie.maakOver(100000000, new Money(500, Money.EURO)));
        }catch(Exception e){}
        try{
            //Overmaken naar met een negative bedrag
            assertEquals(false,bankiersessie.maakOver(100000001, new Money(-500, Money.EURO)));
        }catch(Exception e){}
        
        try {
            //Overmaken buiten geldigheidduur
            Thread.sleep(601);
            assertTrue(bankiersessie.maakOver(100000001, new Money(500, Money.EURO)));
        } catch (Exception e) {}
        
        
    }

    /**
     * Test of getRekening method, of class Bankiersessie.
     */
    @Test
    public void testGetRekening() throws Exception {
        /**
         * @return de rekeninggegevens die horen bij deze sessie
         * exception treedt op wanneer InvalidSessionException als de sessieId niet geldig of
         * verlopen is
         * exception treedt op wanneer RemoteException
         */
        
        try {
            bankiersessie = new Bankiersessie(100000000, bank);
        } catch (RemoteException ex) {
            Logger.getLogger(BankiersessieTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try {
            //Overmaken buiten geldigheidduur
            Thread.sleep(601);
            assertTrue(null == bankiersessie.getRekening());
        } catch (Exception e) {}
        
        try {
            //Overmaken buiten geldigheidduur
            assertTrue(null != bankiersessie.getRekening());
        } catch (Exception e) {}
        
    }

    /**
     * Test of logUit method, of class Bankiersessie.
     */
    @Test
    public void testLogUit() throws Exception {
        
    }
    
}
