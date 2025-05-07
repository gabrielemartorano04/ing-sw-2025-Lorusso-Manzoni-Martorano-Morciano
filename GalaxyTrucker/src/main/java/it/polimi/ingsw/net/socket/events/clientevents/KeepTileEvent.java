package it.polimi.ingsw.net.socket.events.clientevents;

import it.polimi.ingsw.model.components.Component;
import it.polimi.ingsw.net.socket.events.Event;
import it.polimi.ingsw.net.socket.events.EventType;

public class KeepTileEvent extends Event {
    public KeepTileEvent() {
        super(EventType.KEEP_TILE);
    }
}
