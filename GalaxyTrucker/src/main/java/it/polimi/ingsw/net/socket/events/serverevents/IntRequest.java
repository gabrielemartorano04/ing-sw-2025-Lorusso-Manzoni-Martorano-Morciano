package it.polimi.ingsw.net.socket.events.serverevents;

public class IntRequest extends ServerEvent{
    public IntRequest() {
        super(ServerEventType.REQUEST);

    }
    @Override
    public String getMessage() {
        return "Number requested";
    }
}
