/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bank.bankieren;

import CentraleP2.HoofdCentrale;
import bank.internettoegang.Balie;
import bank.internettoegang.IBalie;
import java.net.MalformedURLException;
import java.rmi.RemoteException;
import junit.framework.Assert;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Mnesymne
 */
public class IBalieTest {
    Bank myBank;

    //Correct AccountNaam
    String correctAccountNaamHenk;
    String correctAccountNaamPeter;

    @Before
    public void setUp() {
        //Maakt gebruiker aan om te gebruiken.
        myBank.openRekening("Henk", "Eindhoven");
        myBank.openRekening("Peter", "Eindhoven");
    }

    public IBalieTest() throws RemoteException, MalformedURLException {
        this.myBank = new Bank("ABNAMRO",new HoofdCentrale());
    }

    @Test
    public void testOpenRekening() throws RemoteException {
        /**
         * creatie van een nieuwe bankrekening; het gegenereerde
         * bankrekeningnummer is identificerend voor de nieuwe bankrekening en
         * heeft een saldo van 0 euro
         *
         * @param naam van de eigenaar van de nieuwe bankrekening
         * @param plaats de woonplaats van de eigenaar van de nieuwe
         * bankrekening
         * @param wachtwoord van het account waarmee er toegang kan worden
         * verkregen tot de nieuwe bankrekening
         * @return null zodra naam of plaats een lege string of wachtwoord
         * minder dan vier of meer dan acht karakters lang is en anders de
         * gegenereerde accountnaam(8 karakters lang) waarmee er toegang tot de
         * nieuwe bankrekening kan worden verkregen
         */

        //Balie van de Bank
        Balie myBalie = new Balie(myBank);

        //Correcte Gegevens
        String correctNaam = "Henk";
        String correctPlaats = "Eindhoven";
        String correctWachtwoord = "12345678";

        //Incorrecte Gegevens
        String kortWachtwoord = "123";
        String langWachtwoord = "1234567890";
        String ergLangWachtwoord = "12345678901234567890";
        String leegNaam = "";
        String leegWachtwoord = "";

        //Correcte accounts
        correctAccountNaamHenk = myBalie.openRekening(correctNaam, correctPlaats, correctWachtwoord);
        correctAccountNaamPeter = myBalie.openRekening("Peter", "Eindhoven", "87654321");

        //Correcte Invulling
        Assert.assertNotNull("Incorrecte Data", correctAccountNaamHenk);

        //Incorrecte Wachtwoorden
        Assert.assertNull("Kort wachtwoord error", myBalie.openRekening(correctNaam, correctPlaats, kortWachtwoord));
        Assert.assertNull("Lang wachtwoord error", myBalie.openRekening(correctNaam, correctPlaats, langWachtwoord));
        Assert.assertNull("Erg lang wachtwoord error", myBalie.openRekening(correctNaam, correctPlaats, ergLangWachtwoord));

        //Lege velden
        Assert.assertNull("Leeg naam error", myBalie.openRekening(leegNaam, correctPlaats, correctWachtwoord));
        Assert.assertNull("Leeg wachtwoord error", myBalie.openRekening(correctNaam, correctPlaats, leegWachtwoord));
    }

    @Test
    public void testBalieLogin() throws RemoteException {

        /**
         * er wordt een sessie opgestart voor het login-account met de naam
         * accountnaam mits het wachtwoord correct is
         *
         * @param accountnaam
         * @param wachtwoord
         * @return de gegenereerde sessie waarbinnen de gebruiker toegang krijgt
         * tot de bankrekening die hoort bij het betreffende login- account mits
         * accountnaam en wachtwoord matchen, anders null
         */
        //Ballie
        Balie myBalie = new Balie(myBank);

        //Correcte accounts
        correctAccountNaamHenk = myBalie.openRekening("Henk", "Eindhoven", "12345678");
        correctAccountNaamPeter = myBalie.openRekening("Peter", "Eindhoven", "87654321");

        //Correcte Gegevens
        String correctWachtwoord = "12345678";
        String leegWachtwoord = "";
        String nullWachtwoord = null;
        String incorrectWachtwoord = "incorrectWachtwoord";

        //Correcte Invulling
        Assert.assertNotNull("Correct wachtwoord error", myBalie.logIn(correctAccountNaamHenk, correctWachtwoord));

        //Incorrecte Invulling
        Assert.assertNull("Leeg wachtwoord error", myBalie.logIn(correctAccountNaamHenk, leegWachtwoord));
        Assert.assertNull("Null wachtwoord error", myBalie.logIn(correctAccountNaamHenk, nullWachtwoord));
        Assert.assertNull("Incorrect wachtwoord error", myBalie.logIn(correctAccountNaamHenk, incorrectWachtwoord));
        Assert.assertNull("Incorrect combinatie", myBalie.logIn(correctAccountNaamPeter, correctWachtwoord));

    }
}
