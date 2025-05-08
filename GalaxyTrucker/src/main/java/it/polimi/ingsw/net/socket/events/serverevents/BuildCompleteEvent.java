package it.polimi.ingsw.net.socket.events.serverevents;

public class BuildCompleteEvent {
    private String message;

    public BuildCompleteEvent() {
        message = "Build completed";
    }

    @Override
    public String toString() {
        return message;
    }
}
