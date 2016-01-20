/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Centrale.server;

import bank.bankieren.Money;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

/**
 *
 * @author lino_
 */
public class OverboekCentrale extends UnicastRemoteObject implements ICentrale {

    private ArrayList<IBank1> banken;
    private int bankNr;
    
    
    public OverboekCentrale() throws RemoteException{
        this.banken = new ArrayList<>();
        this.bankNr = 0;
        
    }
    @Override
    public void addBank(String bank, IBank1 bankObject) throws RemoteException {
        if(bankObject != null){
            if(!banken.contains(bankObject)){
                banken.add(bankObject);
            }
        }
    }

    @Override
    public void maakOverVanCentraleServer(int rekeningNummer, Money bedrag) throws RemoteException {
        this.bankNr = (rekeningNummer - 10000000) - 1;
        IBank1 iBank = banken.get(bankNr);
        if(iBank != null){
            iBank.maakOverNaarBank(rekeningNummer, bedrag);
        }
    }
    
}
