package it.polimi.ingsw.net.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

import it.polimi.ingsw.net.socket.events.clientevents.AlienCoordinatesRequest;
import it.polimi.ingsw.net.socket.events.clientevents.ClientEvent;
import it.polimi.ingsw.net.socket.events.clientevents.SendAlienCoord;
import it.polimi.ingsw.net.socket.events.clientevents.ShowOtherShipEvent;
import it.polimi.ingsw.net.socket.events.clientevents.buildingevents.*;
import it.polimi.ingsw.net.socket.events.clientevents.cardevents.*;
import it.polimi.ingsw.net.socket.events.clientevents.setupevents.CreateGameEvent;
import it.polimi.ingsw.net.socket.events.clientevents.setupevents.LoginEvent;
import it.polimi.ingsw.net.socket.events.serverevents.ServerEvent;

public interface RmiServerInterface extends Remote {
    void registerClient(RmiClientInterface client) throws RemoteException;
    void receiveMessage(ClientEvent event, RmiClientInterface client) throws RemoteException;

    ServerEvent visit(CreateGameEvent event) throws RemoteException;

    ServerEvent visit(DiscardTileEvent event) throws RemoteException;

    ServerEvent visit(EndBuildingEvent event) throws RemoteException;

    ServerEvent visit(KeepTileEvent event) throws RemoteException;

    ServerEvent visit(LinkTileEvent event) throws RemoteException;

    ServerEvent visit(LoginEvent event) throws RemoteException;

    ServerEvent visit(SelectTileEvent event) throws RemoteException;

    ServerEvent visit(SelectKeptTileEvent event) throws RemoteException;

    ServerEvent visit(ShowStackEvent event) throws RemoteException;

    ServerEvent visit(TurnHourglassEvent event) throws RemoteException;

    ServerEvent visit(ShowOtherShipEvent event) throws RemoteException;

    ServerEvent visit(AlienCoordinatesRequest event) throws RemoteException;

    ServerEvent visit(SendAlienCoord event) throws RemoteException;

    ServerEvent visit(OkStartFlight event) throws RemoteException;

    ServerEvent visit(DrawCardEvent event) throws RemoteException;

    ServerEvent visit(HumanCoordEvent event) throws RemoteException;

    ServerEvent visit(PlotGoodEvent event) throws RemoteException;

    ServerEvent visit(PlanetDecisionEvent event) throws RemoteException;

    ServerEvent visit(RollDiceEvent event) throws RemoteException;

    ServerEvent visit(ActivateShieldEvent event) throws RemoteException;

    ServerEvent visit(ActivateCannonsEvent event) throws RemoteException;

    ServerEvent visit(ActivateEnginesEvent event) throws RemoteException;

    ServerEvent visit(TruckerLandingEvent event) throws RemoteException;

    ServerEvent visit(RearrangeEvent event) throws RemoteException;

    ServerEvent visit(SetWinDecision event) throws RemoteException;

    ServerEvent visit(RemoveGoodsEvent event) throws RemoteException;

    ServerEvent visit(RemoveAskingEvent event) throws RemoteException;

    ServerEvent visit(RemoveBatteriesEvent event) throws RemoteException;

    ServerEvent visit(TurnComponentEvent turnComponentEvent) throws RemoteException;

    ServerEvent visit(StackDroppedEvent event) throws RemoteException;

    ServerEvent visit(GainCosmicCreditsResponseEvent event) throws RemoteException;

    ServerEvent visit(WarzoneStartEvent event) throws RemoteException;

    ServerEvent visit(RemotionSeenEvent event) throws RemoteException;


}
