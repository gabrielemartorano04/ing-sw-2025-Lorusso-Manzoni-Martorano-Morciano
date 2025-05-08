package it.polimi.ingsw.net.socket.events.clientevents.cardevents;

import it.polimi.ingsw.net.socket.events.clientevents.ClientEvent;
import it.polimi.ingsw.net.socket.events.clientevents.ClientEventType;

public class SendIntEvent extends ClientEvent {
    private int value;
    public SendIntEvent(final int value) {
        super(ClientEventType.SEND_INFO_EVENT, true);
        this.value = value;
    }
    public int getValue() {
        return value;
    }
    @Override
    public String getMessage() {
        return "Value sent to the server: " + value;
    }
}
