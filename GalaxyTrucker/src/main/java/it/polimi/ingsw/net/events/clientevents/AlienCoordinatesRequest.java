package it.polimi.ingsw.net.events.clientevents;

import it.polimi.ingsw.net.Client;
import it.polimi.ingsw.net.VirtualServer;
import it.polimi.ingsw.net.events.serverevents.ServerEvent;

public class AlienCoordinatesRequest extends ClientEvent {

    public AlienCoordinatesRequest() {}

    @Override
    public String getMessage() {
        return "Tell me where I can put aliens";
    }

    @Override
    public ServerEvent call(VirtualServer visitor, Client client) {
         return visitor.visit(this, client);
    }
}
