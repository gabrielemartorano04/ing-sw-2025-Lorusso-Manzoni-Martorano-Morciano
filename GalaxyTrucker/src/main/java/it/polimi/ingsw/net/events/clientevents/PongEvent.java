package it.polimi.ingsw.net.events.clientevents;

import it.polimi.ingsw.net.Client;
import it.polimi.ingsw.net.VirtualServer;
import it.polimi.ingsw.net.events.serverevents.ServerEvent;

public class PongEvent extends ClientEvent {
    public PongEvent() {
        super();
    }
    @Override
    public ServerEvent call(VirtualServer visitor, Client client) {
        return visitor.visit(this, client);
    }
    @Override
    public String getMessage() {
        return "Pong";
    }
}
