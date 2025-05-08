package it.polimi.ingsw.net.socket.events.serverevents;

public class BuildOptionsEvent {
    private String message;

    public BuildOptionsEvent() {
        message = "Build options requested";
    }

    @Override
    public String toString() {
        return message;
    }
}
