package it.polimi.ingsw.net.socket.events.clientevents;

import it.polimi.ingsw.net.socket.events.Event;
import it.polimi.ingsw.net.socket.events.EventType;

public class ShowStackEvent extends ClientEvent {
    private int index;
    public ShowStackEvent(final int index) {
        super(ClientEventType.SHOW_STACK);
        this.index = index;
    }
    public int getIndex() {
        return index;
    }
}
