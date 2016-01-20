package bank.internettoegang;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import bank.bankieren.IBank;
import bank.bankieren.IRekening;
import bank.bankieren.Money;
import fontys.observer.RemotePropertyListener;

import fontys.util.InvalidSessionException;
import fontys.util.NumberDoesntExistException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Bankiersessie extends UnicastRemoteObject implements
        IBankiersessie {

    private static final long serialVersionUID = 1L;
    private long laatsteAanroep;
    private int reknr;
    private IBank bank;
    private RemotePropertyListener ls;

    public Bankiersessie(int reknr, IBank bank) throws RemoteException {
        laatsteAanroep = System.currentTimeMillis();
        this.reknr = reknr;
        this.bank = bank;

    }

    public boolean isGeldig() {
        if(System.currentTimeMillis() - laatsteAanroep > GELDIGHEIDSDUUR) {
            if(ls != null) {
                try {
                    this.removeListener(ls);
                } catch (RemoteException ex) {
                }
            }
        }
        return System.currentTimeMillis() - laatsteAanroep < GELDIGHEIDSDUUR;
    }

    @Override
    public boolean maakOver(int bestemming, Money bedrag)
            throws NumberDoesntExistException, InvalidSessionException,
            RemoteException {

        updateLaatsteAanroep();

        if (reknr == bestemming) {
            throw new RuntimeException(
                    "source and destination must be different");
        }
        if (!bedrag.isPositive()) {
            throw new RuntimeException("amount must be positive");
        }

        return bank.maakOver(reknr, bestemming, bedrag);
    }

    private void updateLaatsteAanroep() throws InvalidSessionException {
        if (!isGeldig()) {
            throw new InvalidSessionException("session has been expired");
        }

        laatsteAanroep = System.currentTimeMillis();
    }

    @Override
    public IRekening getRekening() throws InvalidSessionException,
            RemoteException {

        updateLaatsteAanroep();

        return bank.getRekening(reknr);
    }

    @Override
    public void logUit() throws RemoteException {
        if (ls != null) {
            removeListener(ls);
        }
        UnicastRemoteObject.unexportObject(this, true);
    }

    public void setListener(RemotePropertyListener listener) throws RemoteException {
        if (isGeldig()) {
            this.ls = listener;
            IRekening rekening = bank.getRekening(reknr);
            rekening.addListener(listener, "saldo");
        } else {
            IRekening rekening = bank.getRekening(reknr);
            rekening.removeListener(listener, "saldo");
        }
    }

    public void removeListener(RemotePropertyListener listener) throws RemoteException {
        IRekening rekening = bank.getRekening(reknr);
        rekening.removeListener(listener, "saldo");
    }
}
