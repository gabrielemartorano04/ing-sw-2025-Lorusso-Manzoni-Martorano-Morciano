package it.polimi.ingsw.exceptions;

public class NotEnoughBatteries extends RuntimeException {
    public NotEnoughBatteries(String message) {
        super(message);
    }
}
