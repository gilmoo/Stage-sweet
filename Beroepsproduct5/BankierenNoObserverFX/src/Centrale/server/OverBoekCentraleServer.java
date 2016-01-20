/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Centrale.server;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 *
 * @author lino_
 */
public class OverBoekCentraleServer {
    private static Registry registry = null;
    private int port = 0;
    private static final String bindingName = "OverBoekCentrale";
    private OverboekCentrale centrale = null;
    
    public static void main(String[] args) throws RemoteException{
        OverBoekCentraleServer overBoekCentraleServer = new OverBoekCentraleServer();
        overBoekCentraleServer.startServer();
    }
    
    public void startServer() throws RemoteException {
        this.centrale = new Centrale.server.OverboekCentrale();
        this.port = 1099;
        this.registry = LocateRegistry.createRegistry(port);
        this.registry.rebind(bindingName, centrale);
    }
    
}
