package it.polimi.ingsw.net.socket.events.clientevents.cardevents;

import it.polimi.ingsw.net.socket.events.clientevents.ClientEvent;
import it.polimi.ingsw.net.socket.events.clientevents.ClientEventType;
import it.polimi.ingsw.net.socket.events.serverevents.ServerEvent;
import it.polimi.ingsw.net.socket.server.clienteventhandler.ClientVisitor;

public class SendPlanetDecisionEvent extends ClientEvent {

    private int index;

    public SendPlanetDecisionEvent(int index) {
        super(ClientEventType.SEND_INFO_EVENT, false);
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    @Override
    public ServerEvent call(ClientVisitor visitor) {
        return visitor.visit(this);
    }
}
