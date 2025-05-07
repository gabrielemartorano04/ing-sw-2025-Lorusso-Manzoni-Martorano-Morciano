package it.polimi.ingsw.net.socket.server.clienteventhandler;


import it.polimi.ingsw.net.socket.events.clientevents.*;
import it.polimi.ingsw.net.socket.events.serverevents.DisconnectedUserEvent;
import it.polimi.ingsw.net.socket.events.serverevents.TurnHourglassError;

/**
 * Interface of the visitor used to handle the events coming from the Client with the data used in the cards
 */

public interface ClientVisitor {
    public void visit(CreateGameEvent event);
    public void visit(DiscardTileEvent event);
    public void visit(EndBuildingEvent event);
    public void visit(KeepTileEvent event);
    public void visit(LinkTileEvent event);
    public void visit(LoginEvent event);
    public void visit(SelectTileEvent event);
    public void visit(ShowStackEvent event);
    public void visit(TurnHourglassEvent event);
    public void visit(SendCoordEvent event);
    public void visit(SendBooleanEvent event);
    public void visit(SendCoordListEvent event);
    public void visit(SendIndexesEvent event);
    public void visit(SendIntEvent event);
}
