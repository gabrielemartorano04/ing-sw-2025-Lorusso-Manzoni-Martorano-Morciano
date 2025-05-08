package it.polimi.ingsw.net.socket.events.serverevents;

import it.polimi.ingsw.net.socket.client.ServerEventHandlerVisitor;

public class AvailableComponentToSelectRequest extends ServerEvent{
    private String message;

    public AvailableComponentToSelectRequest() {
        super(ServerEventType.REQUEST, false);
        message = "Available components to select requested";
    }

    @Override
    public String toString() {
        return message;
    }

    @Override
    public void call (ServerEventHandlerVisitor visitor) {
        visitor.visit(this);
    }
}
