package it.polimi.ingsw.net.socket.server.servereventhandler.servereventhandler;

import it.polimi.ingsw.net.socket.events.clientevents.*;

/**
 * Interface of the visitor used to handle the events coming from the Client with the data used in the cards
 */

public interface ServerEventVisitor {
    public void visit(SendCoordEvent event);
    public void visit(SendBooleanEvent event);
    public void visit(SendCoordListEvent event);
    public void visit(SendIndexesEvent event);
    public void visit(SendIntEvent event);
}
