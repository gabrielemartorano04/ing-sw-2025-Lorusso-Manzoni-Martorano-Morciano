package it.polimi.ingsw.net.socket.events.clientevents.cardevents;

import it.polimi.ingsw.net.socket.events.clientevents.ClientEvent;
import it.polimi.ingsw.net.socket.events.serverevents.ServerEvent;
import it.polimi.ingsw.net.VirtualServer;

public class OkStartFlight extends ClientEvent {
    private String message;
    public OkStartFlight() {
        message = "For me, flight can start";
    }
    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public ServerEvent call(VirtualServer visitor) {
        return visitor.visit(this);
    }
}
