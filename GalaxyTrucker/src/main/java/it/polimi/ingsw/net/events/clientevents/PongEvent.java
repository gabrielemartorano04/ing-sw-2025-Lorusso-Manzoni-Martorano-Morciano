package it.polimi.ingsw.net.events.clientevents;

import it.polimi.ingsw.net.VirtualServer;

public class PongEvent extends ClientEvent {
    public PongEvent() {
        super();
    }
    @Override
    public void call(VirtualServer visitor) {
        visitor.visit(this);
    }
    @Override
    public String getMessage() {
        return "Pong";
    }
}
