package it.polimi.ingsw.net.socket.events.clientevents.cardevents;

import it.polimi.ingsw.net.socket.events.clientevents.ClientEvent;
import it.polimi.ingsw.net.socket.events.clientevents.ClientEventType;
import it.polimi.ingsw.net.socket.events.serverevents.ServerEvent;
import it.polimi.ingsw.net.socket.server.clienteventhandler.ClientVisitor;

public class SendIntEvent extends ClientEvent {
    private int value;
    public SendIntEvent(final int value) {
        super(ClientEventType.SEND_INFO_EVENT);
        this.value = value;
    }
    public int getValue() {
        return value;
    }
    @Override
    public String getMessage() {
        return "Value sent to the server: " + value;
    }

    @Override
    public ServerEvent call(ClientVisitor visitor) {
        return visitor.visit(this);
    }
}
