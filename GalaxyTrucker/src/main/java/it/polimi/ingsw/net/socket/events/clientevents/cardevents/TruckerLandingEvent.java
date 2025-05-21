package it.polimi.ingsw.net.socket.events.clientevents.cardevents;

import it.polimi.ingsw.net.socket.events.clientevents.ClientEvent;
import it.polimi.ingsw.net.socket.events.serverevents.ServerEvent;
import it.polimi.ingsw.net.socket.server.clienteventhandler.ClientVisitor;

public class TruckerLandingEvent extends ClientEvent {

    private boolean landing;

    public TruckerLandingEvent(boolean landing) {

        this.landing = landing;
    }

    public boolean getLanding() {
        return landing;
    }

    @Override
    public ServerEvent call(ClientVisitor visitor) {
        return visitor.visit(this);
    }
}
