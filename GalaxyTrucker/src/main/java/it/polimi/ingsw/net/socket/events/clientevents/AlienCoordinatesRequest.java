package it.polimi.ingsw.net.socket.events.clientevents;

import it.polimi.ingsw.net.socket.events.serverevents.ServerEvent;
import it.polimi.ingsw.net.socket.server.clienteventhandler.ClientVisitor;

public class AlienCoordinatesRequest extends ClientEvent {

    public AlienCoordinatesRequest() {
        super(ClientEventType.REQUEST,false);

    }

    @Override
    public String getMessage() {
        return "Tell me where I can put aliens";
    }

    @Override
    public ServerEvent call(ClientVisitor visitor) {
        return visitor.visit(this);
    }
}
