package bank.bankieren;

import CentraleP2.IBankToCentrale;
import CentraleP2.ICentraleToBank;
import fontys.util.*;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Bank extends UnicastRemoteObject implements IBank, IBankToCentrale {

    /**
     *
     */
    private static final long serialVersionUID = -8728841131739353765L;
    private Map<Integer, IRekeningTbvBank> accounts;
    private ICentraleToBank myCentrale;
    private Collection<IKlant> clients;
    private int nieuwReknr;
    private String name;

    public Bank(String name, ICentraleToBank centrale) throws RemoteException {
        accounts = new HashMap<>();
        myCentrale = centrale;
        clients = new ArrayList<>();
        this.name = name;
        IBankToCentrale b = this;
        nieuwReknr = myCentrale.RegisterBank(name, b) * 10000000;
    }

    public int openRekening(String name, String city) {
        if (name.equals("") || city.equals("")) {
            return -1;
        }

        IKlant klant = getKlant(name, city);
        IRekeningTbvBank account = new Rekening(nieuwReknr, klant, Money.EURO);
        accounts.put(nieuwReknr, account);
        nieuwReknr++;
        return nieuwReknr - 1;
    }

    private IKlant getKlant(String name, String city) {
        for (IKlant k : clients) {
            if (k.getNaam().equals(name) && k.getPlaats().equals(city)) {
                return k;
            }
        }
        IKlant klant = new Klant(name, city);
        clients.add(klant);
        return klant;
    }

    public IRekening getRekening(int nr) {
        return accounts.get(nr);
    }

    public synchronized boolean maakOver(int source, int destination, Money money)
            throws NumberDoesntExistException {

        if (source == destination) {
            throw new RuntimeException(
                    "cannot transfer money to your own account");
        }
        if (!money.isPositive()) {
            throw new RuntimeException("money must be positive");
        }

        IRekeningTbvBank source_account = (IRekeningTbvBank) getRekening(source);
        if (source_account == null) {
            throw new NumberDoesntExistException("account " + source
                    + " unknown at " + name);
        }

        Money negative = Money.difference(new Money(0, money.getCurrency()),
                money);
        boolean success = source_account.muteer(negative);
        if (!success) {
            return false;
        } else {
            try {
                myCentrale.maakOver(destination, money);
                //Rollback
                source_account.muteer(money);
            } catch (RemoteException ex) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void muteerVanCentrale(int RekeningNR, Money saldo) throws RemoteException, NumberDoesntExistException {
        IRekeningTbvBank rekening = (IRekeningTbvBank) getRekening(RekeningNR);

        if (rekening != null) {
            if (saldo.isPositive()) {
                 rekening.muteer(saldo);
            }
        }
    }

}
