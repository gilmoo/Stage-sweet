/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CentraleP2;

import bank.bankieren.Bank;
import bank.bankieren.Money;
import fontys.util.NumberDoesntExistException;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author Mnesymne
 */
public interface ICentraleToBank extends Remote {
    
    
    /**
     * Geeft naam van de Bank mee, en de Bank zelf. Wordt een check gedaan of de bank nog niet aangemeld is bij de centrale, zo niet word 
     * deze aan de lijst toegevoegd. 
     * @param Bank De instantie van de Bank, deze is nodig voor acties te kunnen doen
     * @param myBank De naam van de Bank, word meegegeven om te checked of deze al aangemeld is, en gebruikt om in de hashmap te zoeken.
     * @return int Nummer waar alle rekeningen van de bank mee moeten beginnen.
     * @throws RemoteException 
     */
    public int RegisterBank(String Bank, IBankToCentrale myBank) throws RemoteException;
    
    /**
     * Geeft een RekeningNummer en saldo mee, RekeningNR wordt gechecked bij welke bank de Rekening hoort.
     * Daarna wordt de maakOver Methode bij de des betreffende bank aangeroepen.
     * @param RekeningNR als int, RekeningNR van doel rekening, eerste nummer is Identifier.
     * @param saldo als Money moet positief zijn.
     * @return True als Rekening bestaat, en aanroep gelukt is, anders word er false returned.
     * @throws RemoteException
     * @throws NumberDoesntExistException
     */
    public void maakOver(int RekeningNR,Money saldo) throws RemoteException,NumberDoesntExistException;
    
    
}
