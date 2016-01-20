/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Centrale.server;

import bank.bankieren.Money;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author lino_
 */
public interface ICentrale extends Remote {
    /**
	 * voeg een nieuwe bank toe aan de lijst van banken
	 * 
	 * @param bank
	 * @param bankObject
	 */
    public void addBank(String bank, IBank1 bankObject) throws RemoteException;
    /**
	 * het bedrag van huidige rekening naar de gebruiker waar de param rekeningNummer overeen komt
	 * 
	 * @param rekeningNummer
         * @param bedrag
	 * 
	 */
    public void maakOverVanCentraleServer(int rekeningNummer, Money bedrag) throws RemoteException;
}
