package it.polimi.ingsw.net.socket.events.clientevents.buildingevents;


import it.polimi.ingsw.net.socket.events.clientevents.ClientEvent;
import it.polimi.ingsw.net.socket.events.clientevents.ClientEventType;

public class KeepTileEvent extends ClientEvent {
    public KeepTileEvent() {
        super(ClientEventType.KEEP_TILE, false);
    }
    @Override
    public String getMessage() {
        return "I want to keep the selected tile";
    }
}
