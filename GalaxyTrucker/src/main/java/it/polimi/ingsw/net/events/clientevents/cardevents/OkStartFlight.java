package it.polimi.ingsw.net.events.clientevents.cardevents;

import it.polimi.ingsw.net.events.clientevents.ClientEvent;
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
    public void call(VirtualServer visitor) {
         visitor.visit(this);
    }
}
