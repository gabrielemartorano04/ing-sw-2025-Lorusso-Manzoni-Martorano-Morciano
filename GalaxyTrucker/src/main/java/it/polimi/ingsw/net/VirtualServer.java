package it.polimi.ingsw.net;


import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.net.events.clientevents.*;
import it.polimi.ingsw.net.events.clientevents.buildingevents.*;
import it.polimi.ingsw.net.events.clientevents.cardevents.*;
import it.polimi.ingsw.net.events.clientevents.setupevents.CreateGameEvent;
import it.polimi.ingsw.net.events.clientevents.setupevents.LoginEvent;
import it.polimi.ingsw.net.events.clientevents.setupevents.ChoseToResume;

/**
 * Interface of the visitor used to handle the events coming from the Client with the data used in the cards
 */

public interface VirtualServer {
    public void visit(CreateGameEvent event);
    public void visit(DiscardTileEvent event);
    public void visit(EndBuildingEvent event);
    public void visit(KeepTileEvent event);
    public void visit(LinkTileEvent event);
    public void visit(LoginEvent event);
    public void visit(SelectTileEvent event);

    public void visit(SelectKeptTileEvent event);

    public void visit(ShowStackEvent event);
    public void visit(TurnHourglassEvent event);

    void setGameController(GameController event);

    void visit(ShowOtherShipEvent event);

    void visit(GeneriErrorFromClient event);

    void visit(AlienCoordinatesRequest event);

    void visit(SendAlienCoord event);

    void visit(OkStartFlight event);

    void visit(HumanCoordEvent event);

    void visit(PlotGoodEvent event);

    void visit(PlanetDecisionEvent event);

    void visit(RollDiceEvent event);

    void visit(ActivateShieldEvent event);

    void visit(ActivateCannonsEvent event);

    void visit(ActivateEnginesEvent event);

    void visit(DrawCardEvent event);

    void visit(TruckerLandingEvent event);

    void visit(RearrangeEvent event);

    void visit(SetWinDecision event);

    void visit(RemoveGoodsEvent event);

    void visit(RemoveAskingEvent event);

    void visit(RemoveBatteriesEvent event);

    void visit(TurnComponentEvent event);

    void visit(StackDroppedEvent event);

    void visit(GainCosmicCreditsResponseEvent event);

    void visit(WarzoneStartEvent event);

    void visit(RemotionSeenEvent event);

    void visit(DeadTrucker event);

    void visit(PongEvent pongEvent);

    void visit(ChoseToResume event);
}
