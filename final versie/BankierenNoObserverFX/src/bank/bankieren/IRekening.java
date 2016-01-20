package bank.bankieren;

import fontys.observer.RemotePropertyListener;
import java.io.Serializable;
import java.rmi.RemoteException;

public interface IRekening extends Serializable {
  int getNr();
  Money getSaldo();
  IKlant getEigenaar();
  int getKredietLimietInCenten();
  void addListener(RemotePropertyListener listener, String prop) throws RemoteException;
  void removeListener(RemotePropertyListener listener, String prop) throws RemoteException;
}

