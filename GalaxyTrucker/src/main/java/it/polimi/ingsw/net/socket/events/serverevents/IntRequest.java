package it.polimi.ingsw.net.socket.events.serverevents;

public class IntRequest extends ServerEvent{
    public IntRequest() {
        super(ServerEventType.REQUEST, false);

    }
    @Override
    public String getMessage() {
        return "Number requested";
    }
}
