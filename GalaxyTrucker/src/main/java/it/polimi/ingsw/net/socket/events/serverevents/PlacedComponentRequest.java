package it.polimi.ingsw.net.socket.events.serverevents;

public class PlacedComponentRequest extends ServerEvent{
    private String message;

    public PlacedComponentRequest() {
        super(ServerEventType.REQUEST, false);
        message = "Placed component requested";
    }

    @Override
    public String toString() {
        return message;
    }
}
