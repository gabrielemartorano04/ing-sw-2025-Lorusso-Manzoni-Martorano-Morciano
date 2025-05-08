package it.polimi.ingsw.net.socket.events.serverevents;

public class BuildOptionsEvent extends ServerEvent{
    private String message;

    public BuildOptionsEvent() {
        super(ServerEventType.BUILD_OPTIONS, false);
        message = "Build options requested";
    }

    @Override
    public String toString() {
        return message;
    }
}
