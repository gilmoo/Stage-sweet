/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CentraleP2;

import bank.bankieren.Bank;
import bank.bankieren.Money;
import fontys.util.NumberDoesntExistException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

/**
 *
 * @author Mnesymne
 */
public class HoofdCentrale extends UnicastRemoteObject implements ICentraleToBank {

    private ArrayList<IBankToCentrale> allBanks;
    private static Registry registry = null;
    private int port = 1098;

    public HoofdCentrale() throws RemoteException, MalformedURLException {
        registry = java.rmi.registry.LocateRegistry.createRegistry(port);
        registry.rebind("Centrale", this);
        allBanks = new ArrayList();
    }

    @Override
    public int RegisterBank(String Bank, IBankToCentrale myBank) throws RemoteException {
        boolean contains = false;
        for (IBankToCentrale b : allBanks) {
            if (b.getName().equals(myBank.getName())) {
                contains = true;
            }
        }

        if (!contains) {
            allBanks.add(myBank);
            return allBanks.indexOf(myBank) + 1;
        }
        return -1;

    }

    @Override
    public void maakOver(int RekeningNR, Money saldo) throws RemoteException, NumberDoesntExistException {
        int bankNR = (RekeningNR / 10000000) - 1;
        IBankToCentrale b = allBanks.get(bankNR);
        if (b != null) {
            b.muteerVanCentrale(RekeningNR, saldo);
        }
    }
}
