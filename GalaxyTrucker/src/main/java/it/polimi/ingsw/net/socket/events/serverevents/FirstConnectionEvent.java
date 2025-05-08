package it.polimi.ingsw.net.socket.events.serverevents;

import it.polimi.ingsw.model.PlayerColors;
import it.polimi.ingsw.net.socket.client.ServerEventHandlerVisitor;

import java.util.EnumSet;

public class FirstConnectionEvent extends ServerEvent {
    private final String message;
    public FirstConnectionEvent(String message) {
        super(ServerEventType.FIRST_CONNECTION, false);
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
