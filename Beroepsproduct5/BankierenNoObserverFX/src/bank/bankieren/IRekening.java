package bank.bankieren;

import fontys.observer.RemotePublisher;
import java.io.Serializable;

public interface IRekening extends Serializable,RemotePublisher {
  int getNr();
  Money getSaldo();
  IKlant getEigenaar();
  int getKredietLimietInCenten();
  
}

