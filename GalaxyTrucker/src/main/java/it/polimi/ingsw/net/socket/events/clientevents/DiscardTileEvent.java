package it.polimi.ingsw.net.socket.events.clientevents;

import it.polimi.ingsw.net.socket.events.Event;
import it.polimi.ingsw.net.socket.events.EventType;

public class DiscardTileEvent extends Event {
    public DiscardTileEvent() {
        super(EventType.DISCARD_TILE);
    }

}
