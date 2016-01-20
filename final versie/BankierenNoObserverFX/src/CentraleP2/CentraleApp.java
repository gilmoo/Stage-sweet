/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CentraleP2;

import java.net.MalformedURLException;
import java.rmi.RemoteException;

/**
 *
 * @author Mnesymne
 */
public class CentraleApp {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws RemoteException, MalformedURLException {
       HoofdCentrale centrale = new HoofdCentrale();
    }
    
}
