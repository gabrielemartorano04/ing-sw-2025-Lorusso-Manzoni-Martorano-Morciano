package it.polimi.ingsw.net.control;


import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.net.events.clientevents.*;
import it.polimi.ingsw.net.events.clientevents.buildingevents.*;
import it.polimi.ingsw.net.events.clientevents.cardevents.*;
import it.polimi.ingsw.net.events.clientevents.setupevents.CreateGameEvent;
import it.polimi.ingsw.net.events.clientevents.setupevents.LoginEvent;
import it.polimi.ingsw.net.events.clientevents.setupevents.ChooseToResume;
import it.polimi.ingsw.net.events.serverevents.ServerEvent;


/**
 * Interface of the visitor used to handle the events coming from the Client with the data used in the cards
 */

public interface VirtualServer {

    /**
     * Sets the controller managing the game logic.
     */
    void setGameController(GameController event);

    /**
     * Handles a CreateGameEvent from a client.
     */
    ServerEvent visit(CreateGameEvent event, Client client);

    /**
     * Handles a DiscardTileEvent from a client.
     */
    ServerEvent visit(DiscardTileEvent event, Client client);

    /**
     * Handles an EndBuildingEvent from a client.
     */
    ServerEvent visit(EndBuildingEvent event, Client client);

    /**
     * Handles a KeepTileEvent from a client.
     */
    ServerEvent visit(KeepTileEvent event, Client client);

    /**
     * Handles a LinkTileEvent from a client.
     */
    ServerEvent visit(LinkTileEvent event, Client client);

    /**
     * Handles a LoginEvent from a client.
     */
    ServerEvent visit(LoginEvent event, Client client);

    /**
     * Handles a SelectTileEvent from a client.
     */
    ServerEvent visit(SelectTileEvent event, Client client);

    /**
     * Handles a SelectKeptTileEvent from a client.
     */
    ServerEvent visit(SelectKeptTileEvent event, Client client);

    /**
     * Handles a ShowStackEvent from a client.
     */
    ServerEvent visit(ShowStackEvent event, Client client);

    /**
     * Handles a TurnHourglassEvent from a client.
     */
    ServerEvent visit(TurnHourglassEvent event, Client client);

    /**
     * Handles a ShowOtherShipEvent from a client.
     */
    ServerEvent visit(ShowOtherShipEvent event);

    /**
     * Handles a RemoveTile action from a client
     */
    ServerEvent visit(RemoveTile event, Client client);

    /**
     * Handles an AlienCoordinatesRequest from a client.
     */
    ServerEvent visit(AlienCoordinatesRequest event,Client client);

    /**
     * Handles a SendAlienCoord event from a client.
     */
    ServerEvent visit(SendAlienCoord event, Client client);

    /**
     * Handles an OkStartFlight event from a client.
     */
    ServerEvent visit(OkStartFlight event);

    /**
     * Handles a HumanCoordEvent from a client.
     */
    ServerEvent visit(HumanCoordEvent event);

    /**
     * Handles a PlotGoodEvent from a client.
     */
    ServerEvent visit(PlotGoodEvent event);

    /**
     * Handles a PlanetDecisionEvent from a client.
     */
    ServerEvent visit(PlanetDecisionEvent event);

    /**
     * Handles a RollDiceEvent from a client.
     */
    ServerEvent visit(RollDiceEvent event);

    /**
     * Handles an ActivateShieldEvent from a client.
     */
    ServerEvent visit(ActivateShieldEvent event);

    /**
     * Handles an ActivateCannonsEvent from a client.
     */
    ServerEvent visit(ActivateCannonsEvent event);

    /**
     * Handles an ActivateEnginesEvent from a client.
     */
    ServerEvent visit(ActivateEnginesEvent event);

    /**
     * Handles a DrawCardEvent from a client.
     */
    ServerEvent visit(DrawCardEvent event);

    /**
     * Handles a TruckerLandingEvent from a client.
     */
    ServerEvent visit(TruckerLandingEvent event);

    /**
     * Handles a RearrangeEvent from a client.
     */
    ServerEvent visit(RearrangeEvent event);

    /**
     * Handles a RemoveGoodsEvent from a client.
     */
    ServerEvent visit(RemoveGoodsEvent event);

    /**
     * Handles a RemoveAskingEvent from a client.
     */
    ServerEvent visit(RemoveAskingEvent event);

    /**
     * Handles a RemoveBatteriesEvent from a client.
     */
    ServerEvent visit(RemoveBatteriesEvent event);

    /**
     * Handles a TurnComponentEvent from a client.
     */
    ServerEvent visit(TurnComponentEvent event);

    /**
     * Handles a StackDroppedEvent from a client.
     */
    ServerEvent visit(StackDroppedEvent event);

    /**
     * Handles a GainCosmicCreditsResponseEvent from a client.
     */
    ServerEvent visit(GainCosmicCreditsResponseEvent event);

    /**
     * Handles a WarzoneStartEvent from a client.
     */
    ServerEvent visit(WarzoneStartEvent event);

    /**
     * Handles a RemotionSeenEvent from a client.
     */
    ServerEvent visit(RemotionSeenEvent event, Client client);

    /**
     * Applies the big Shoot in the current card
     */
    ServerEvent visit(ApplyBigShoot event);

    /**
     * Handles a DeadTrucker event.
     */
    ServerEvent visit(DeadTrucker event);

    /**
     * Handles a PongEvent from a client.
     */
    ServerEvent visit(PongEvent pongEvent, Client client);

    /**
     * Handles a ChooseToResume event.
     */
    ServerEvent visit(ChooseToResume event);

    /**
     * Handles a WithdrawRequest from a client.
     */
    ServerEvent visit(WithdrawRequest withdrawRequest);
}



