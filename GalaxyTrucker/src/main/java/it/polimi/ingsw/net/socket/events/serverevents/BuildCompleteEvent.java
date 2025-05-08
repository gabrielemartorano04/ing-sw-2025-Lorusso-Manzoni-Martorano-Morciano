package it.polimi.ingsw.net.socket.events.serverevents;

public class BuildCompleteEvent extends ServerEvent{
    private String message;

    public BuildCompleteEvent() {
        super(ServerEventType.END_BUILDING, true);
        message = "Build completed";
    }

    @Override
    public String toString() {
        return message;
    }
}
