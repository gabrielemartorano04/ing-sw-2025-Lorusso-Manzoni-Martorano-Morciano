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

public class RmiServer extends UnicastRemoteObject implements RmiServerInterface, ClientHandler {
    private final ServerController controller;
    private TruckerController truckerController;

    public RmiServer(ServerController controller) throws RemoteException {
        this.controller = controller;
    }

    public void addClient(ClientHandler client){
        this.controller.addClient(client);
    }

    @Override
    public void setTruckerController(TruckerController truckerController) {
        this.truckerController = truckerController;
    }

    @Override
    public TruckerController getTruckerController() {
        return truckerController;
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


    @Override
    public void sendEvent(ServerEvent event) {
    }


    @Override
    public void registerClient(RmiClientInterface client) throws RemoteException {

    }

    @Override
    public void receiveMessage(ClientEvent event, RmiClientInterface client) throws RemoteException {

    }

    @Override
    public ServerEvent visit(CreateGameEvent event) throws RemoteException {
        return null;
    }

    @Override
    public ServerEvent visit(DiscardTileEvent event) throws RemoteException {
        return null;
    }

    @Override
    public ServerEvent visit(EndBuildingEvent event) throws RemoteException {
        return null;
    }

    @Override
    public ServerEvent visit(KeepTileEvent event) throws RemoteException {
        return null;
    }

    @Override
    public ServerEvent visit(LinkTileEvent event) throws RemoteException {
        return null;
    }

    @Override
    public ServerEvent visit(LoginEvent event) throws RemoteException {
        return null;
    }

    @Override
    public ServerEvent visit(SelectTileEvent event) throws RemoteException {
        return null;
    }

    @Override
    public ServerEvent visit(SelectKeptTileEvent event) throws RemoteException {
        return null;
    }

    @Override
    public ServerEvent visit(ShowStackEvent event) throws RemoteException {
        return null;
    }

    @Override
    public ServerEvent visit(TurnHourglassEvent event) throws RemoteException {
        return null;
    }

    @Override
    public ServerEvent visit(ShowOtherShipEvent event) throws RemoteException {
        return null;
    }

    @Override
    public ServerEvent visit(AlienCoordinatesRequest event) throws RemoteException {
        return null;
    }

    @Override
    public ServerEvent visit(SendAlienCoord event) throws RemoteException {
        return null;
    }

    @Override
    public ServerEvent visit(OkStartFlight event) throws RemoteException {
        return null;
    }

    @Override
    public ServerEvent visit(DrawCardEvent event) throws RemoteException {
        return null;
    }

    @Override
    public ServerEvent visit(HumanCoordEvent event) throws RemoteException {
        return null;
    }

    @Override
    public ServerEvent visit(PlotGoodEvent event) throws RemoteException {
        return null;
    }

    @Override
    public ServerEvent visit(PlanetDecisionEvent event) throws RemoteException {
        return null;
    }

    @Override
    public ServerEvent visit(RollDiceEvent event) throws RemoteException {
        return null;
    }

    @Override
    public ServerEvent visit(ActivateShieldEvent event) throws RemoteException {
        return null;
    }

    @Override
    public ServerEvent visit(ActivateCannonsEvent event) throws RemoteException {
        return null;
    }

    @Override
    public ServerEvent visit(ActivateEnginesEvent event) throws RemoteException {
        return null;
    }

    @Override
    public ServerEvent visit(TruckerLandingEvent event) throws RemoteException {
        return null;
    }

    @Override
    public ServerEvent visit(RearrangeEvent event) throws RemoteException {
        return null;
    }

    @Override
    public ServerEvent visit(SetWinDecision event) throws RemoteException {
        return null;
    }

    @Override
    public ServerEvent visit(RemoveGoodsEvent event) throws RemoteException {
        return null;
    }

    @Override
    public ServerEvent visit(RemoveAskingEvent event) throws RemoteException {
        return null;
    }

    @Override
    public ServerEvent visit(RemoveBatteriesEvent event) throws RemoteException {
        return null;
    }

    @Override
    public ServerEvent visit(TurnComponentEvent turnComponentEvent) throws RemoteException {
        return null;
    }

    @Override
    public ServerEvent visit(StackDroppedEvent event) throws RemoteException {
        return null;
    }

    @Override
    public ServerEvent visit(GainCosmicCreditsResponseEvent event) throws RemoteException {
        return null;
    }

    @Override
    public ServerEvent visit(WarzoneStartEvent event) throws RemoteException {
        return null;
    }

    @Override
    public ServerEvent visit(RemotionSeenEvent event) throws RemoteException {
        return null;
    }
}




