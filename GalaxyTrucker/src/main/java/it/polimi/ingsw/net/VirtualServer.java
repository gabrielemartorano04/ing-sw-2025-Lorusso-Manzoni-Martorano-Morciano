package it.polimi.ingsw.net;


import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.net.events.clientevents.*;
import it.polimi.ingsw.net.events.clientevents.buildingevents.*;
import it.polimi.ingsw.net.events.clientevents.cardevents.*;
import it.polimi.ingsw.net.events.clientevents.setupevents.CreateGameEvent;
import it.polimi.ingsw.net.events.clientevents.setupevents.LoginEvent;
import it.polimi.ingsw.net.events.clientevents.setupevents.ChoseToResume;
import it.polimi.ingsw.net.events.serverevents.ServerEvent;

/**
 * Interface of the visitor used to handle the events coming from the Client with the data used in the cards
 */

public interface VirtualServer {
    void setGameController(GameController event);



    ServerEvent visit(CreateGameEvent event, Client client);
    ServerEvent visit(DiscardTileEvent event, Client client);
    ServerEvent visit(EndBuildingEvent event, Client client);
    ServerEvent visit(KeepTileEvent event, Client client);
    ServerEvent visit(LinkTileEvent event, Client client);
    ServerEvent visit(LoginEvent event, Client client);
    ServerEvent visit(SelectTileEvent event, Client client);

    ServerEvent visit(SelectKeptTileEvent event, Client client);

    ServerEvent visit(ShowStackEvent event, Client client);
    ServerEvent visit(TurnHourglassEvent event, Client client);

    ServerEvent visit(ShowOtherShipEvent event);

    ServerEvent visit(AlienCoordinatesRequest event,Client client);

    ServerEvent visit(SendAlienCoord event, Client client);

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

    ServerEvent visit(TurnComponentEvent event);

    ServerEvent visit(StackDroppedEvent event);

    ServerEvent visit(GainCosmicCreditsResponseEvent event);

    ServerEvent visit(WarzoneStartEvent event);

    ServerEvent visit(RemotionSeenEvent event, Client client);

    ServerEvent visit(DeadTrucker event);

    ServerEvent visit(PongEvent pongEvent, Client client);

    ServerEvent visit(ChoseToResume event);
}
