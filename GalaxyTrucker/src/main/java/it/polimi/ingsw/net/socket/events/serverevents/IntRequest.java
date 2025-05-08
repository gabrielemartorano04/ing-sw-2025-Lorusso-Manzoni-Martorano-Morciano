package it.polimi.ingsw.net.socket.events.serverevents;

import it.polimi.ingsw.net.socket.client.ServerEventHandlerVisitor;

public class IntRequest extends ServerEvent{
    public IntRequest() {
        super(ServerEventType.REQUEST, false);

    }
    @Override
    public String getMessage() {
        return "Number requested";
    }

    @Override
    public void call (ServerEventHandlerVisitor visitor) {
        visitor.visit(this);
    }
}
