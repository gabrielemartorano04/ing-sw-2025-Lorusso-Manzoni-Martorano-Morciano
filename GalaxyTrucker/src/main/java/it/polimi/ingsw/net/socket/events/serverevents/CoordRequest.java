package it.polimi.ingsw.net.socket.events.serverevents;

public class CoordRequest extends ServerEvent{
    public CoordRequest() {
        super(ServerEventType.REQUEST);
    }
    @Override
    public String getMessage() {
        return "Coordinates requested";
    }
}
