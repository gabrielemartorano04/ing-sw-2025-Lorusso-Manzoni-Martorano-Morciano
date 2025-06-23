package it.polimi.ingsw.net.events.serverevents.setupevents;

import it.polimi.ingsw.net.ServerEventHandlerVisitor;
import it.polimi.ingsw.net.events.serverevents.ServerEvent;

public class FirstConnectionEvent extends ServerEvent {
    private final String message;
    public FirstConnectionEvent(String message) {
        super(false);
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
