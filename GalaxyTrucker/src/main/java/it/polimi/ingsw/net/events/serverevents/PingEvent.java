package it.polimi.ingsw.net.events.serverevents;

import it.polimi.ingsw.net.ServerEventHandlerVisitor;

public class PingEvent extends ServerEvent {
    public PingEvent() {
        super(true,true);
    }
    @Override
    public void call (ServerEventHandlerVisitor visitor) {
        visitor.visit(this);
    }
}
