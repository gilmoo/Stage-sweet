/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bank.bankieren;

import CentraleP2.HoofdCentrale;
import fontys.util.NumberDoesntExistException;
import java.net.MalformedURLException;
import java.rmi.RemoteException;
import org.junit.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 *
 * @author Jur
 */
public class IBankTest {

    @Test
    public void openRekeningTest() throws RemoteException, MalformedURLException {
        //Maak bank aan
        IBank bank = new Bank("ABN",new HoofdCentrale());

        //Test lege naam
        Assert.assertEquals(-1, bank.openRekening("", "Eindhoven"));

        //Test lege plaats
        Assert.assertEquals(-1, bank.openRekening("Jan", ""));

        //Goede test
        Assert.assertTrue("Rekening wordt niet aangemaakt!", bank.openRekening("Jan", "Eindhoven") != -1);
        //Test met klant die al een rekening heeft
        Assert.assertTrue("Rekening wordt niet aangemaakt!", bank.openRekening("Jan", "Eindhoven") != -1);
    }

    @Test
    public void maakOverTest() throws NumberDoesntExistException, RemoteException, MalformedURLException {
        //Maak bank aan
        IBank bank = new Bank("ABN",new HoofdCentrale());

        //Maak rekeningen aan
        int rekening1, rekening2;
        rekening1 = bank.openRekening("Henk", "Eindhoven");
        rekening2 = bank.openRekening("Piet", "Weert");

        //Test overboeking naar eigen rekening
        try {
            bank.maakOver(rekening1, rekening1, new Money(1000, Money.EURO));
            fail("Bedrag mag niet worden overgemaakt naar eigen rekening!");
        } catch (Exception e) {
        }
        try {
            bank.maakOver(rekening2, rekening2, new Money(50, Money.EURO));
            fail("Bedrag mag niet worden overgemaakt naar eigen rekening!");
        } catch (Exception e) {
        }

        //Test foutief bedrag
        try {
            bank.maakOver(rekening1, rekening2, new Money(0, Money.EURO));
            fail("Bedrag moet hoger zijn dan 0!");
        } catch (Exception e) {
        }
        try {
            bank.maakOver(rekening1, rekening2, new Money(-100, Money.EURO));
            fail("Bedrag moet hoger zijn dan 0!");
        } catch (Exception e) {
        }

        //Overboeking die over het limiet heen gaat
        assertFalse("Bedrag mag niet overgemaakt worden", bank.maakOver(rekening1, rekening2, new Money(25000, Money.EURO)));

        //Test goede overboeking
        assertTrue("Bedrag moet wel worden overgemaakt", bank.maakOver(rekening1, rekening2, new Money(1000, Money.EURO)));
        //Controleer rekening saldo's
        assertEquals(bank.getRekening(rekening1).getSaldo().getCents(), -1000);
        assertEquals(bank.getRekening(rekening2).getSaldo().getCents(), 1000);
    }

    @Test
    public void getNameTest() throws RemoteException, MalformedURLException {
        //Banken aanmaken
        HoofdCentrale c = new HoofdCentrale();
        IBank bank1 = new Bank("ABN",c);
        IBank bank2 = new Bank("Rabo",c);
        
        //Controleer of de naam correct is
        assertEquals(bank1.getName(), "ABN");
        assertEquals(bank2.getName(), "Rabo");
    }
    
    @Test
    public void getRekeningTest() throws RemoteException, MalformedURLException {
        //Bank aanmaken
        IBank bank = new Bank("ABN",new HoofdCentrale());
        int rekeningNummer = bank.openRekening("Henk", "Eindhoven");
        //Controleer niet bestaand rekeningnummer
        Assert.assertNull(bank.getRekening(12));
        //Controleer bestaand rekeningnummer
        IRekening rekening = bank.getRekening(rekeningNummer);
        Assert.assertTrue(rekening != null);
    }
}
