package it.polimi.ingsw.net.socket.events.serverevents;

public class AvailableComponentToSelectRequest {
    private String message;
    public AvailableComponentToSelectRequest() {
        message = "Available components to select requested";
    }

    @Override
    public String toString() {
        return message;
    }
}
