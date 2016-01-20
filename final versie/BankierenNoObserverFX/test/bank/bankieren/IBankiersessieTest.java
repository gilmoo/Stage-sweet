/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bank.bankieren;

import CentraleP2.HoofdCentrale;
import bank.internettoegang.Bankiersessie;
import bank.internettoegang.IBankiersessie;
import fontys.util.InvalidSessionException;
import fontys.util.NumberDoesntExistException;
import java.net.MalformedURLException;
import java.rmi.RemoteException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author nick
 */
public class IBankiersessieTest {

    Bankiersessie mySessie;
    Bank myBank;
    int rekeningHenk;
    int rekeningPeter;

    public IBankiersessieTest() throws RemoteException, MalformedURLException {

        myBank = new Bank("ABNAMRO",new HoofdCentrale());
        rekeningHenk = myBank.openRekening("Henk", "Eindhoven");
        rekeningPeter = myBank.openRekening("Peter", "Eindhoven");
    }

    @Test
    public void isGeldigTest() throws InterruptedException, RemoteException {
        /**
         * @returns true als de laatste aanroep van getRekening of maakOver voor
         * deze sessie minder dan GELDIGHEIDSDUUR geleden is en er geen
         * communicatiestoornis in de tussentijd is opgetreden, anders false
         */

        //Laatste actie binnen GELDIGHEIDSDUUR
        mySessie = new Bankiersessie(10000001, myBank);
        Assert.assertTrue("Correcte sessie error", mySessie.isGeldig());

        //Laatste actie op rand van GELDIGHEIDSDUUR
        mySessie = new Bankiersessie(10000001, myBank);
        Thread.sleep(990);
        Assert.assertTrue("GELDIGHEIDSDUUR -1 tijd error", mySessie.isGeldig());
        Thread.sleep(22);
        Assert.assertFalse("GELDIGHEIDSDUUR +1 tijd error", mySessie.isGeldig());

        //Laatste actie buiten GELDIGHEIDSDUUR
        Thread.sleep(3000);
        Assert.assertFalse("Correcte sessie error", mySessie.isGeldig());
    }

    @Test
    public void maakOverTest() throws RemoteException, InterruptedException {
        /**
         * er wordt bedrag overgemaakt van de bankrekening met het nummer bron
         * naar de bankrekening met nummer bestemming
         *
         * @param bron
         * @param bestemming is ongelijk aan rekeningnummer van deze
         * bankiersessie
         * @param bedrag is groter dan 0
         * @return <b>true</b> als de overmaking is gelukt, anders <b>false</b>
         * @throws NumberDoesntExistException als bestemming onbekend is
         * @throws InvalidSessionException als sessie niet meer geldig is
         */

        //Correct Bedrag
        Money correctBedrag = new Money(100, Money.EURO);

        //Incorrect bedrag
        Money incorrectBedrag = new Money(-100, Money.EURO);
        Money nulBedrag = new Money(0, Money.EURO);

        //Incorrecte bestemmingen
        int eigenRekening = rekeningHenk;
        int bronRekening = rekeningPeter;

        //Correcte invulling
        mySessie = new Bankiersessie(rekeningHenk, myBank);
        try {
            Assert.assertTrue("Correcte maakOver error", mySessie.maakOver(bronRekening, correctBedrag));
        } catch (InvalidSessionException | NumberDoesntExistException | IllegalArgumentException ex) {
            fail(ex.getMessage());
        }

        //Overmaken naar eigen rekening
        mySessie = new Bankiersessie(rekeningHenk, myBank);
        try {
            mySessie.maakOver(eigenRekening, correctBedrag);
            fail("EigenRekening: Geen runtime Exception");
        } catch (RuntimeException ex) {
            Assert.assertTrue(true);
        } catch (InvalidSessionException | NumberDoesntExistException ex) {
            fail(ex.getMessage());
        }

        //IllegalArgumentException test
        mySessie = new Bankiersessie(rekeningHenk, myBank);
        try {
            mySessie.maakOver(Integer.parseInt("123$5678"), correctBedrag);
            fail("EigenRekening: Geen runtime Exception");
        } catch (IllegalArgumentException ex) {
            Assert.assertTrue(true);
        } catch (InvalidSessionException | NumberDoesntExistException | RuntimeException ex) {
            fail(ex.getMessage());
        }

        //nulBedrag
        mySessie = new Bankiersessie(rekeningHenk, myBank);
        try {
            mySessie.maakOver(bronRekening, nulBedrag);
            fail("nulBedrag: Geen runtime Exception");
        } catch (InvalidSessionException | NumberDoesntExistException ex) {
            fail(ex.getMessage());
        } catch (RuntimeException ex) {
            Assert.assertTrue(true);
        }

        //Negatief bedrag
        mySessie = new Bankiersessie(rekeningHenk, myBank);
        try {
            mySessie.maakOver(bronRekening, incorrectBedrag);
            fail("NegatiefBedrag: Geen runtime Exception");
        } catch (InvalidSessionException | NumberDoesntExistException ex) {
            fail(ex.getMessage());
        } catch (RuntimeException ex) {
            Assert.assertTrue(true);
        }

        //Rekening bestaat niet
        mySessie = new Bankiersessie(rekeningHenk, myBank);
        try {
            mySessie.maakOver(10, correctBedrag);
            fail("Niet bestaande rekening: Geen NumberDoesntExistException");
        } catch (InvalidSessionException | RuntimeException ex) {

            fail(ex.getMessage());
        } catch (NumberDoesntExistException ex) {
            Assert.assertTrue(true);
        }

        //Sessie verlopen
        mySessie = new Bankiersessie(rekeningHenk, myBank);
        Thread.sleep(1500);
        try {
            mySessie.maakOver(bronRekening, incorrectBedrag);
            fail("NegatiefBedrag: Geen runtime Exception");
        } catch (RuntimeException | NumberDoesntExistException ex) {
            fail(ex.getMessage());
        } catch (InvalidSessionException ex) {
            Assert.assertTrue(true);
        }
    }

    @Test
    public void getRekeningTest() throws RemoteException, InterruptedException {
        /**
         * @return de rekeninggegevens die horen bij deze sessie
         * @throws InvalidSessionException als de sessieId niet geldig of
         * verlopen is
         * @throws RemoteException
         */

        //Bestaande sessie
        mySessie = new Bankiersessie(rekeningHenk, myBank);
        try {
            Assert.assertEquals("Bestaande sessie Error", mySessie.getRekening().getNr(), rekeningHenk);
        } catch (InvalidSessionException ex) {
            fail(ex.getMessage());
        }

        //Bestaande sessie _ GELDIGHEIDSDUUR -1
        mySessie = new Bankiersessie(rekeningHenk, myBank);
        Thread.sleep(990);
        try {
            Assert.assertEquals("Bestaande sessie Error", mySessie.getRekening().getNr(), rekeningHenk);
        } catch (InvalidSessionException ex) {
            fail(ex.getMessage());
        }

        //Ongeldige sessie _ GELDIGHEIDSDUUR +1
        mySessie = new Bankiersessie(rekeningHenk, myBank);
        Thread.sleep(1001);
        try {
            mySessie.getRekening().getNr();
            fail("getRekening: Geen InvalidSessionException GELDIGHEIDSDUUR +1");
        } catch (InvalidSessionException ex) {
            Assert.assertTrue(true);
        }

        //Ongeldige sessie _ GELDIGHEIDSDUUR x 2
        mySessie = new Bankiersessie(rekeningHenk, myBank);
        Thread.sleep(2000);
        try {
            mySessie.getRekening().getNr();
            fail("getRekening: Geen InvalidSessionException GELDIGHEIDSDUUR +1");
        } catch (InvalidSessionException ex) {
            Assert.assertTrue(true);
        }
    }
}
