package it.polimi.ingsw.net.socket.events.serverevents;

import it.polimi.ingsw.net.socket.client.ServerEventHandlerVisitor;

public class GenericErrorEvent extends ServerEvent{
    private String message;

    public GenericErrorEvent(String message, boolean val) {
        super(ServerEventType.GENERIC_ERROR, val);
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public void call(ServerEventHandlerVisitor visitor) {
        visitor.visit(this);
    }
}
