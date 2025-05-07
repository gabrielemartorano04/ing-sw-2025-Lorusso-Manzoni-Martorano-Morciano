package it.polimi.ingsw.net.socket.events.serverevents;

public class IndexesRequest extends ServerEvent{
    public IndexesRequest() {
        super(ServerEventType.REQUEST);
    }
    @Override
    public String getMessage() {
        return "List of indexes requested";
    }
}
