package it.polimi.ingsw.net.rmi.server;

import it.polimi.ingsw.controller.ServerController;
import it.polimi.ingsw.controller.TruckerController;
import it.polimi.ingsw.model.Trucker;
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
import java.rmi.server.UnicastRemoteObject;

public class ClientHandlerRmi extends UnicastRemoteObject implements RmiServerInterface, ClientHandler {
    private final ServerController controller;
    private final RmiClientInterface client;
    private TruckerController truckerController;

    public ClientHandlerRmi(ServerController controller, RmiClientInterface client) throws RemoteException {
        this.controller = controller;
        this.client = client;
        controller.addClient(this);  // come fai con Socket
    }

    public void receiveMessage(ClientEvent event) {
        // controller.handleEvent(event, this);
    }

    @Override
    public void setTruckerController(TruckerController truckerController) {
        this.truckerController = truckerController;
    }

    @Override
    public TruckerController getTruckerController() {
        return truckerController;
    }

    @Override
    public void sendEvent(ServerEvent event) {
        try {
            client.receiveMessage(event);  // chiamata remota
        } catch (RemoteException e) {
            System.err.println("Errore invio evento via RMI: " + e.getMessage());
            controller.removeClient(this);
        }
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




