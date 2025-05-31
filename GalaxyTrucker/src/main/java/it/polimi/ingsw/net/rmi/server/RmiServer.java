package it.polimi.ingsw.net.rmi.server;

import it.polimi.ingsw.controller.ServerController;
import it.polimi.ingsw.net.Client;
import it.polimi.ingsw.net.ClientHandler;
import it.polimi.ingsw.net.events.clientevents.ClientEvent;
import it.polimi.ingsw.net.events.serverevents.ServerEvent;
import it.polimi.ingsw.net.rmi.RmiServerInterface;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class RmiServer extends UnicastRemoteObject implements RmiServerInterface {
    private final ServerController controller;

    public RmiServer(ServerController controller) throws RemoteException {
        super();
        this.controller = controller;
    }

    @Override
    public void connect(ClientHandler client) throws RemoteException {
        this.controller.addClient(new Client(client));
    }

    @Override
    public String sayHello(String name) throws RemoteException {
        return "Hello, " + name + "!";
    }

    public static void bind(ServerController serverController) {
        try {
            RmiServer server = new RmiServer(serverController);
            Registry registry = LocateRegistry.createRegistry(1099);
            registry.rebind("MyService", server);
            System.out.println("RMI Server bound under name 'MyService'");
        } catch (RemoteException e) {
            System.err.println("Error starting RMI server: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void handleIncomingEvent(ClientEvent event, ClientHandler client) throws RemoteException {
        //TODO: metodo che il client chiama in modo remoto per invocare il visitor del server
        controller.launchThreadEvent(controller.generateSourceForRmi(event, client.getId()));
    }

    public void handleOutgoingEvent (ServerEvent event) throws RemoteException {
        //TODO:metodo che il server chiama per invocare il visitor del client
        // client.handleInComingEvent(event);
    }

}





