package it.polimi.ingsw.net.socket.events.serverevents;

import java.net.Socket;

public class CoordListRequest extends ServerEvent{
    public CoordListRequest() {
        super(ServerEventType.REQUEST);
    }
    @Override
    public String getMessage() {
        return "List of coordinates requested";
    }
}
