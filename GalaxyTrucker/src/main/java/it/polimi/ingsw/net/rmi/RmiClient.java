package it.polimi.ingsw.net.rmi;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RmiClient{
    private RmiServer server;

    public RmiClient(RmiServer server){
        this.server=server;
    }

    public static void main(String[] args) throws RemoteException, NotBoundException {
        final String serverName = "GTServer";
        Registry registry = LocateRegistry.getRegistry(args[0], 1234);
        RmiServer server = (RmiServer) registry.lookup(serverName);
        new RmiClient(server).run();
    }

    public void run() throws RemoteException {

    }
}
