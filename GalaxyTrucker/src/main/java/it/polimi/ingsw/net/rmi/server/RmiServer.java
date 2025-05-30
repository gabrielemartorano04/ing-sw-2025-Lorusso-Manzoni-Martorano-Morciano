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
        this.controller.addClient(client);
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
}





