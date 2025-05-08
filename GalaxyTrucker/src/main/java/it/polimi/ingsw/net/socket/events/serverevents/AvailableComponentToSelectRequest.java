package it.polimi.ingsw.net.socket.events.serverevents;

public class AvailableComponentToSelectRequest extends ServerEvent{
    private String message;

    public AvailableComponentToSelectRequest() {
        super(ServerEventType.REQUEST, false);
        message = "Available components to select requested";
    }

    @Override
    public String toString() {
        return message;
    }
}
