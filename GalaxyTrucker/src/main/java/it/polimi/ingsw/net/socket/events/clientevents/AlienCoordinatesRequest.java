package it.polimi.ingsw.net.socket.events.clientevents;

import it.polimi.ingsw.net.socket.events.serverevents.ServerEvent;
import it.polimi.ingsw.net.VirtualServer;

public class AlienCoordinatesRequest extends ClientEvent {

    public AlienCoordinatesRequest() {}

    @Override
    public String getMessage() {
        return "Tell me where I can put aliens";
    }

    @Override
    public void call(VirtualServer visitor) {
         visitor.visit(this);
    }
}
