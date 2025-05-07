package it.polimi.ingsw.net.socket.events.clientevents;

import it.polimi.ingsw.net.socket.events.Event;
import it.polimi.ingsw.net.socket.events.EventType;

public class SendIntEvent extends ClientEvent {
    private int value;
    public SendIntEvent(final int value) {
        super(ClientEventType.SEND_INFO_EVENT);
        this.value = value;
    }
}
