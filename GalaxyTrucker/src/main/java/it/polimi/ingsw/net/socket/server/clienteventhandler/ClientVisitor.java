package it.polimi.ingsw.net.socket.server.clienteventhandler;


import it.polimi.ingsw.controller.GameController;
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
    public ServerEvent visit(ShowStackEvent event);
    public ServerEvent visit(TurnHourglassEvent event);
    public ServerEvent visit(SendCoordEvent event);
    public ServerEvent visit(SendBooleanEvent event);
    public ServerEvent visit(SendCoordListEvent event);
    public ServerEvent visit(SendIndexesEvent event);
    public ServerEvent visit(SendIntEvent event);

    void setGameController(GameController gameController);
}
