package it.polimi.ingsw.net.rmi.server;

import it.polimi.ingsw.controller.ServerController;
import it.polimi.ingsw.controller.TruckerController;
import it.polimi.ingsw.net.ClientHandler;
import it.polimi.ingsw.net.rmi.RmiClientInterface;
import it.polimi.ingsw.net.rmi.RmiServerInterface;
import it.polimi.ingsw.net.socket.events.clientevents.AlienCoordinatesRequest;
import it.polimi.ingsw.net.socket.events.clientevents.ClientEvent;
import it.polimi.ingsw.net.socket.events.clientevents.SendAlienCoord;
import it.polimi.ingsw.net.socket.events.clientevents.ShowOtherShipEvent;
import it.polimi.ingsw.net.socket.events.clientevents.buildingevents.*;
import it.polimi.ingsw.net.socket.events.clientevents.cardevents.*;
import it.polimi.ingsw.net.socket.events.clientevents.setupevents.CreateGameEvent;
import it.polimi.ingsw.net.socket.events.clientevents.setupevents.LoginEvent;
import it.polimi.ingsw.net.socket.events.serverevents.ServerEvent;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class RmiServer extends UnicastRemoteObject {
    private final ServerController controller;

    public RmiServer(ServerController controller) throws RemoteException {
        this.controller = controller;
    }

    public void connect(ClientHandler client){
        this.controller.addClient(client);
    }


    public String sayHello(String name) throws RemoteException {
        return "Hello, " + name + "!";
    }

    public static void bind(ServerController serverController) {
        try {
            RmiServer server = new RmiServer(serverController);
            Registry registry = LocateRegistry.createRegistry(1099); // Porta default RMI
            registry.rebind("GalaxyTruckerRmiServer", server);
            System.out.println("RMI Server bound under name 'GalaxyTruckerRmiServer'");
        } catch (RemoteException e) {
            System.err.println("Error starting RMI server: " + e.getMessage());
            e.printStackTrace();
        }
    }

}




