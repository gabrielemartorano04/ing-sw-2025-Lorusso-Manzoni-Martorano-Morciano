package it.polimi.ingsw.utilities.exceptions;

public class ServerErrorEvent extends RuntimeException {
    public ServerErrorEvent(String message) {
        super(message);
    }
}
