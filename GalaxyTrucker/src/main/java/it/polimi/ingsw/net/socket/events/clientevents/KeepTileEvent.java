package it.polimi.ingsw.net.socket.events.clientevents;


public class KeepTileEvent extends ClientEvent {
    public KeepTileEvent() {
        super(ClientEventType.KEEP_TILE);
    }
    @Override
    public String getMessage() {
        return "I want to keep the selected tile";
    }
}
