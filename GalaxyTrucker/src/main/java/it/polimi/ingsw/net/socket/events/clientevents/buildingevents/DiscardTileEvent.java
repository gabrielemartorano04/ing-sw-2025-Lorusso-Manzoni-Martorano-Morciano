package it.polimi.ingsw.net.socket.events.clientevents.buildingevents;

import it.polimi.ingsw.net.socket.events.clientevents.ClientEvent;
import it.polimi.ingsw.net.socket.events.clientevents.ClientEventType;

public class DiscardTileEvent extends ClientEvent {
    public DiscardTileEvent() {
        super(ClientEventType.DISCARD_TILE, false);
    }
    @Override
    public String getMessage() {
        return "I want to discard the selected tile";
    }

}
