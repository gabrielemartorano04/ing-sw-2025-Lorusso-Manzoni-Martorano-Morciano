package it.polimi.ingsw.net.socket.server.clienteventhandler;


import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.net.socket.events.clientevents.AlienCoordinatesRequest;
import it.polimi.ingsw.net.socket.events.clientevents.GeneriErrorFromClient;
import it.polimi.ingsw.net.socket.events.clientevents.SendAlienCoord;
import it.polimi.ingsw.net.socket.events.clientevents.ShowOtherShipEvent;
import it.polimi.ingsw.net.socket.events.clientevents.buildingevents.*;
import it.polimi.ingsw.net.socket.events.clientevents.cardevents.*;
import it.polimi.ingsw.net.socket.events.clientevents.setupevents.CreateGameEvent;
import it.polimi.ingsw.net.socket.events.clientevents.setupevents.LoginEvent;
import it.polimi.ingsw.net.socket.events.serverevents.ServerEvent;

/**
 * Interface of the visitor used to handle the events coming from the Client with the data used in the cards
 */

public interface ClientVisitor {
    public ServerEvent visit(CreateGameEvent event);
    public ServerEvent visit(DiscardTileEvent event);
    public ServerEvent visit(EndBuildingEvent event);
    public ServerEvent visit(KeepTileEvent event);
    public ServerEvent visit(LinkTileEvent event);
    public ServerEvent visit(LoginEvent event);
    public ServerEvent visit(SelectTileEvent event);

    public ServerEvent visit(SelectKeptTileEvent event);

    public ServerEvent visit(ShowStackEvent event);
    public ServerEvent visit(TurnHourglassEvent event);

    void setGameController(GameController event);

    ServerEvent visit(ShowTilesListEvent event);

    ServerEvent visit(ShowOtherShipEvent event);

    ServerEvent visit(GeneriErrorFromClient event);

    ServerEvent visit(AlienCoordinatesRequest event);

    ServerEvent visit(SendAlienCoord event);

    ServerEvent visit(OkStartFlight event);

    ServerEvent visit(HumanCoordEvent event);

    ServerEvent visit(PlotGoodEvent event);

    ServerEvent visit(PlanetDecisionEvent event);

    ServerEvent visit(RollDiceEvent event);

    ServerEvent visit(ActivateShieldEvent event);

    ServerEvent visit(ActivateCannonsEvent event);

    ServerEvent visit(ActivateEnginesEvent event);

    ServerEvent visit(DrawCardEvent event);

    ServerEvent visit(TruckerLandingEvent event);

    ServerEvent visit(RearrangeEvent event);

    ServerEvent visit(SetWinDecision event);

    ServerEvent visit(RemoveGoodsEvent event);

    ServerEvent visit(RemoveAskingEvent event);

    ServerEvent visit(RemoveBatteriesEvent event);
}
