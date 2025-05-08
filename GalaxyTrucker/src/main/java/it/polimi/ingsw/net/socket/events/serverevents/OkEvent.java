package it.polimi.ingsw.net.socket.events.serverevents;

import it.polimi.ingsw.net.socket.client.ServerEventHandlerVisitor;

public class OkEvent extends ServerEvent {
    private final String message;
    public OkEvent(String message, boolean forAll) {
        super(ServerEventType.OK, forAll);
        this.message = message;
    }
    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public void call (ServerEventHandlerVisitor visitor) {
        visitor.visit(this);
    }
}
