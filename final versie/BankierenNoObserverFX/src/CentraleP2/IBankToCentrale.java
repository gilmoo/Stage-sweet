/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CentraleP2;

import bank.bankieren.Money;
import fontys.util.NumberDoesntExistException;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author Mnesymne
 */
public interface IBankToCentrale extends Remote {
    
    /**
     * Geeft een RekeningNummer en saldo mee, RekeningNR wordt gechecked bij welke bank de Rekening hoort.
     * Daarna wordt de maakOver Methode bij de des betreffende bank aangeroepen.
     * @param RekeningNR als int, RekeningNR van doel rekening, eerste nummer is Identifier.
     * @param saldo als Money moet positief zijn.
     * @return True als Rekening bestaat, en aanroep gelukt is, anders word er false returned.
     * @throws RemoteException
     * @throws NumberDoesntExistException
     */
    public void muteerVanCentrale(int RekeningNR,Money saldo) throws RemoteException,NumberDoesntExistException;
    
    String getName() throws RemoteException;
}
