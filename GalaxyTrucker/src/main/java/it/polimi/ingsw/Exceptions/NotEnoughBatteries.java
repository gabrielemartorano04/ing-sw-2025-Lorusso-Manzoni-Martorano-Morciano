package it.polimi.ingsw.Exceptions;

public class NotEnoughBatteries extends RuntimeException {
    public NotEnoughBatteries(String message) {
        super(message);
    }
}
