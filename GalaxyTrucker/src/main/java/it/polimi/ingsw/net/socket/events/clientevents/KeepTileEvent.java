package it.polimi.ingsw.net.socket.events.clientevents;

import it.polimi.ingsw.model.components.Component;
import it.polimi.ingsw.net.socket.events.Event;
import it.polimi.ingsw.net.socket.events.EventType;

public class KeepTileEvent extends ClientEvent {
    public KeepTileEvent() {
        super(ClientEventType.KEEP_TILE);
    }
}
