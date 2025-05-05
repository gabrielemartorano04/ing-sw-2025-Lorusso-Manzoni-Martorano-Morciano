package it.polimi.ingsw.net.socket.events.clientevents;

import it.polimi.ingsw.net.socket.events.Event;
import it.polimi.ingsw.net.socket.events.EventType;

public class SendIntEvent extends Event {
    private int value;
    public SendIntEvent(final int value) {
        super(EventType.SEND_INFO_EVENT);
        this.value = value;
    }
}
