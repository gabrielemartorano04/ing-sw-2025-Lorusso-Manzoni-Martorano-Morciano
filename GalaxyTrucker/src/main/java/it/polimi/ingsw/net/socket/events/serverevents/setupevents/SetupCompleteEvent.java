package it.polimi.ingsw.net.socket.events.serverevents.setupevents;

import it.polimi.ingsw.net.socket.client.ServerEventHandlerVisitor;
import it.polimi.ingsw.net.socket.events.serverevents.ServerEvent;
import it.polimi.ingsw.net.socket.events.serverevents.ServerEventType;

public class SetupCompleteEvent extends ServerEvent {
    private String message;
    public SetupCompleteEvent() {
        super(ServerEventType.SETUP_COMPLETE, true);
        message="All players ready";
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
