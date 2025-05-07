package it.polimi.ingsw.net.socket.events.clientevents;

import it.polimi.ingsw.net.socket.events.Event;
import it.polimi.ingsw.net.socket.events.EventType;

public class DiscardTileEvent extends ClientEvent {
    public DiscardTileEvent() {
        super(ClientEventType.DISCARD_TILE);
    }

}
