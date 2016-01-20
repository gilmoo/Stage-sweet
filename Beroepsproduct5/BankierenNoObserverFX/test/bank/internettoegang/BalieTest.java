/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bank.internettoegang;

import bank.bankieren.Bank;
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
public class BalieTest {
    private Bank bank = null;
    private Balie balie = null;
    public BalieTest() {
        
        
        try {
            balie = new Balie(bank);
        } catch (RemoteException ex) {
            Logger.getLogger(BalieTest.class.getName()).log(Level.SEVERE, null, ex);
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
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of openRekening method, of class Balie.
     */
    @Test
    public void testOpenRekening() {
        /**
         *
         * @param naam van de eigenaar van de gebruiker
         * @param plaats de woonplaats van de gebruiker
         * @param wachtwoord van de gebruiker hiermee kan hij inloggen op het system
         * @return null zodra naam of plaats een lege string of wachtwoord
         * minder dan vier of meer dan acht karakters lang is en anders de
         * gegenereerde accountnaam(8 karakters lang) waarmee er toegang tot de
         * nieuwe bankrekening kan worden verkregen
         */
        
        try {
            balie = new Balie(bank);
        } catch (RemoteException ex) {
            Logger.getLogger(BalieTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        //Rekening aanmaken met een lege naam
        assertEquals(null, balie.openRekening("", "Eindhoven", "Test"));
        
        //Rekening aanmaken met een lege plaatsnaam
        assertEquals(null, balie.openRekening("Lino", "", "Test"));
        
        //Rekening aanmaken met een wachtwoord van een lengte 
        assertEquals(null, balie.openRekening("Lino", "Eindhoven", "T"));
        
        //Rekening aanmaken met een wachtwoord van negen lengte 
        assertEquals(null, balie.openRekening("Lino", "Eindhoven", "Tttttttttt"));
        
        //Rekening aanmaken met de juiste informatie 
        assertTrue(balie.openRekening("Lino", "Eindhoven", "Test") != null);
        
        
    }

    /**
     * Test of logIn method, of class Balie.
     */
    @Test
    public void testLogIn() throws Exception {
        /**
         * @param accountnaam
         * @param wachtwoord
         * @return een sessie wordt aangemaakt wanneer de gebruiker is ingelogd met de juiste inlognaam en wachtwoord 
         * en null wordt gereturneerd wanneer de loginnaam of wachtwoord niet kloppen
         */
        IBankiersessie sessie = new Bankiersessie(100000000, bank);
        try {
            balie = new Balie(bank);
        } catch (RemoteException ex) {
            Logger.getLogger(BalieTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        String gebruikernaam = balie.openRekening("Lino", "Eindhoven", "Test");
        try {
            //Login met de juiste gegevens
            assertEquals(sessie, balie.logIn(gebruikernaam, "Test"));
        } catch (Exception e) {}
        
        try {
            //Login met de verkeerde gebruikernaam
            assertEquals(null, balie.logIn("Lino", "Test"));
        } catch (Exception e) {
        }
        
        try{
            //Login met de verkeerde wachtwoord
        assertEquals(null, balie.logIn(gebruikernaam, "T"));
        }catch (Exception e) {}
        
    }
    
}
