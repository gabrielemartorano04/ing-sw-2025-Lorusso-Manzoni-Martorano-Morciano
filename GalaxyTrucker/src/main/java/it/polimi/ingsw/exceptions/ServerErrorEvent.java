package it.polimi.ingsw.exceptions;

public class ServerErrorEvent extends RuntimeException {
    public ServerErrorEvent(String message) {
        super(message);
    }
}
